package com.zillious.corporate_website.ui.beans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import com.zillious.commons.db.UserDB;
import com.zillious.corporate_website.app.WebsiteApplication;
import com.zillious.corporate_website.data.AttendanceFromDeviceDTO;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.ui.navigation.WebsitePages;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;
import com.zillious.corporate_website.utils.ConfigStore;
import com.zillious.corporate_website.utils.EmailSender;
import com.zillious.corporate_website.utils.StringUtility;

public class AttendanceBean {

    private static final String EXCLUDED_DEVICEID = "EXCLUDED_DEVICEID";
    private static Logger       s_logger          = Logger.getLogger(AttendanceBean.class);

    public static WebsitePages uploadAttendance(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, String[] queryParams) {

        String ipAddress = request.getRemoteAddr();
        s_logger.info("Request to upload attendance from ipaddress:" + ipAddress + " at " + new Date());
        try {
            if (!WebsiteApplication.isValidIPAddress(ipAddress)) {
                throw new RuntimeException("IP Address not authorised: " + ipAddress);
            }
            if (!ServletFileUpload.isMultipartContent(request)) {
                throw new RuntimeException("Request is not MultipartContent");
            }

            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setSizeThreshold(51200);
            ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory);
            upload.setFileSizeMax(2 * 1024 * 1024);
            List<FileItem> items = (List<FileItem>) upload.parseRequest(request);

            FileItem dataItem = null;
            String extension = null;

            String email = null;
            String password = null;

            for (FileItem itm : items) {
                if (itm.isFormField()) {
                    if ("email".equalsIgnoreCase(itm.getFieldName())) {
                        email = StringUtility.trimAndEmptyIsNull(itm.getString());
                    } else if ("password".equalsIgnoreCase(itm.getFieldName())) {
                        password = StringUtility.trimAndEmptyIsNull(itm.getString());
                    }
                } else {
                    try {
                        s_logger.info("Userid in the request: " + email);
                        User user = UserDB.authenticateUser(null, email, password);
                        if (user == null || (!user.getUserRole().isAdministrator() && !user.getUserRole().isSuper())) {
                            throw new WebsiteException(WebsiteExceptionType.USER_AUTH_FAILED);
                        }
                    } catch (Exception e) {
                        s_logger.error("Error in finding the user with credentials: email=" + email + ", password="
                                + password);
                        addMessageToResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized user");
                        return WebsitePages.NO_PAGE;
                    }

                    dataItem = itm;
                    if (dataItem.getSize() > (WebsiteBean.MAX_SIZE_PER_FILE_IN_KB * 1024 * 100)) {
                        throw new Exception("File size too big! only " + WebsiteBean.MAX_SIZE_PER_FILE_IN_KB
                                + "KB allowed");
                    }

                    String name = dataItem.getName();
                    if (name != null) {
                        extension = name.substring(name.lastIndexOf(".") + 1).toLowerCase();
                    }
                    s_logger.debug("Extension of the file: " + extension);
                }
            }
            if (!"csv".equalsIgnoreCase(extension) && !"txt".equalsIgnoreCase(extension)) {
                throw new Exception("Only csv and txt extension files allowed!");
            }

            // Parse file
            InputStream ins = null;
            InputStreamReader isr = null;
            BufferedReader reader = null;

            Set<String> deviceUserIds = UserDB.getDeviceUserIds();
            try {
                int lineNo = 0;
                String line = null;

                ins = dataItem.getInputStream();
                isr = new InputStreamReader(ins);
                reader = new BufferedReader(isr);

                List<AttendanceFromDeviceDTO> recordsToUpload = new ArrayList<AttendanceFromDeviceDTO>();
                Set<String> notPresent = new HashSet<String>();
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    String[] tokens = StringUtility.splitNullForEmpty(line, "[,]");
                    if (tokens == null || tokens.length != 4) {
                        continue;
                    }
                    lineNo++;
                    tokens[1] = String.valueOf(Integer.parseInt(tokens[1]));
                    if (deviceUserIds.contains(tokens[1])) {
                        recordsToUpload.add(new AttendanceFromDeviceDTO(tokens[1], tokens[0], tokens[2], tokens[3]));
                    } else {
                        notPresent.add(tokens[1]);
                    }
                }

                if (recordsToUpload.isEmpty()) {
                    addMessageToResponse(response, HttpServletResponse.SC_OK, "Number of lines read: " + lineNo);
                    return WebsitePages.NO_PAGE;
                }

                int status = UserDB.addAttendance(recordsToUpload, ipAddress);
                if (status != -1) {
                    addMessageToResponse(response, HttpServletResponse.SC_OK, "Number of lines read: " + status);
                } else {
                    addMessageToResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                            "Problem in adding attendance records");
                }

                if (notPresent.size() > 0) {

                    Set<String> excludedDeviceIds = StringUtility.splitToStringSet(
                            ConfigStore.getStringValue(EXCLUDED_DEVICEID, null), ",");
                    if (excludedDeviceIds != null && !excludedDeviceIds.isEmpty()) {
                        notPresent.removeAll(excludedDeviceIds);
                    }

                    if (!notPresent.isEmpty()) {
                        EmailSender sender = EmailSender.getInstance();
                        sender.sendDeviceUsersNotPresent(notPresent);
                    }

                }

            } catch (Exception e) {
                throw e;
            } finally {
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (Throwable t) {
                    }
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Throwable t) {
                    }
                }
                if (ins != null) {
                    try {
                        ins.close();
                    } catch (Throwable t) {
                    }
                }
            }
        } catch (Exception e) {
            s_logger.error("Error while serving the request from the ipaddress: " + ipAddress, e);
            try {
                addMessageToResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            } catch (Exception e1) {
                s_logger.error("Error while sending the error status back to client", e1);
            }
        }

        return WebsitePages.NO_PAGE;
    }

    private static void addMessageToResponse(ZilliousSecurityWrapperResponse response, int code, String errorMessage)
            throws IOException {
        response.setStatus(code);
        response.getOutputStream().write(errorMessage.getBytes());
    }
}
