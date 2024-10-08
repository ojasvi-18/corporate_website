package com.zillious.corporate_website.ui.beans;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.util.Base64;
import org.apache.log4j.Logger;

import com.zillious.corporate_website.data.ContactDTO;
import com.zillious.corporate_website.ui.beans.MessageBean.MessageType;
import com.zillious.corporate_website.ui.navigation.DBUtil;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.ui.navigation.WebsitePages;
import com.zillious.corporate_website.ui.resources.QueryTypes;
import com.zillious.corporate_website.ui.security.ZilliousSecurityRequestType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;
import com.zillious.corporate_website.utils.DateUtility;
import com.zillious.corporate_website.utils.EmailSender;
import com.zillious.corporate_website.utils.StringUtility;
import com.zillious.lang.ResourceManager;

/**
 * @author Nishant
 * 
 */
public class AudienceBean {
    private static Logger      s_logger      = Logger.getLogger(AudienceBean.class);
    private static EmailSender s_emailSender = EmailSender.getInstance();

    public static WebsitePages subscribeToNewsLetter(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, String[] queryParams) {
        String ipAddress = request.getRemoteAddr();
        if (queryParams != null && queryParams.length > 0 && "addSubscription".equals(queryParams[0].trim())) {
            ResourceManager manager = new ResourceManager(I18NBean.getLocaleBundleFromRequest(request));
            String message = null;
            String email = null;
            String cs = null;
            try {
                email = request.getParameter("Email id to subscribe to the mailing list", "email",
                        ZilliousSecurityRequestType.EMAIL);
                cs = request.getParameter("Hidden field to check if it is a valid hit", "cs",
                        ZilliousSecurityRequestType.DEFAULT_SAFE_STRING);
                if (email == null && (cs == null || !"sub".equals(cs))) {
                    throw new WebsiteException(WebsiteExceptionType.INVALID_ACCESS);
                }
                boolean status = DBUtil.addEmailToSubscriptionList(email, ipAddress);
                if (status) {
                    message = "{'status': 'success'}";
                }
            } catch (Exception e) {
                if (e instanceof WebsiteException) {
                    WebsiteException we = (WebsiteException) e;
                    if (we.getType() == WebsiteExceptionType.NEWSLETTER_SUBSCRIBED) {
                        message = "{'status': 'error', 'message':'"
                                + manager.transformText("error", we.getType().name(), we.getType().getDesc()) + "'}";
                    } else if (we.getType() == WebsiteExceptionType.INVALID_ACCESS) {
                        message = "{'status': 'error', 'message':'"
                                + manager.transformText("error", we.getType().name(), we.getType().getDesc()) + "'}";
                    }

                }
                if (message == null) {
                    message = "{'status': 'error', 'message':'"
                            + manager
                                    .transformText("error", "newsletter",
                                            "There has been an error in subscribing to the newsletter. Please contact the administrator")
                            + "'}";
                    s_emailSender.sendNewsletterSubscriptionAlert(email, ipAddress, false);
                }
                s_logger.error("Error while saving the email to the newsletter subscription list", e);
            }
            try {
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(message);
                response.flushBuffer();
            } catch (Exception e) {
                s_logger.error("Error while writing the json respone", e);

            }
            return WebsitePages.NO_PAGE;
        }
        return WebsitePages.TERMS_PAGE;
    }

    public static WebsitePages addContactRequest(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, String[] queryParams) {
        String remoteAddr = request.getRemoteAddr();
        if (queryParams != null && queryParams.length > 0) {
            ResourceManager manager = new ResourceManager(I18NBean.getLocaleBundleFromRequest(request));
            if ("addContactRequest".equals(queryParams[0].trim())) {
                QueryTypes queryType = null;
                String email = null;
                String name = null;
                String phone = null;
                String contactMessage = null;
                try {
                    String query = request.getParameter("Query type for the contact", "query",
                            ZilliousSecurityRequestType.DEFAULT_SAFE_STRING);
                    queryType = QueryTypes.getQueryType(query);
                    String ipAddress = remoteAddr;
                    // boolean isValidateCaptcha =
                    // CaptchaBean.validateCaptcha(request, ipAddress);
                    boolean isValidateCaptcha = true;
                    if (!isValidateCaptcha) {
                        throw new WebsiteException(WebsiteExceptionType.CAPTCHA_FAIL);
                    }
                    email = request.getParameter("Email id of the person requesting contact", "email",
                            ZilliousSecurityRequestType.EMAIL);
                    name = request.getParameter("Name of the person requesting contact", "name",
                            ZilliousSecurityRequestType.DEFAULT_SAFE_STRING);
                    phone = request.getParameter("Phone number of the person requesting contact", "phone",
                            ZilliousSecurityRequestType.DEFAULT_SAFE_STRING);
                    contactMessage = request.getParameter("Contact message", "message",
                            ZilliousSecurityRequestType.DEFAULT_SAFE_STRING);

                    if (queryType == null) {
                        s_logger.info("Parameters obtained from the request: " + email + ", " + name + ", " + phone
                                + ", " + contactMessage + ", " + ipAddress);
                        s_logger.info("query obtained from the ui: " + query);
                        throw new WebsiteException(WebsiteExceptionType.INVALID_QUERY);
                    }
                    DBUtil.addContactRequest(email, name, phone, queryType, contactMessage, ipAddress);
                    MessageBean.setMessage(request, MessageType.MSG_SUCCESS, manager.transformText("contact",
                            "success", "Your Request has been received. We will get in touch with you shortly!"));

                } catch (Exception e) {
                    String message2 = null;
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    if (e instanceof WebsiteException) {
                        WebsiteException websiteException = (WebsiteException) e;
                        WebsiteExceptionType type = websiteException.getType();
                        if (type == WebsiteExceptionType.DUPLICATE_CONTACT_REQUEST) {
                            message2 = manager.transformText("error", ((WebsiteException) e).getType().name(),
                                    websiteException.getMessage()) + " : " + (websiteException.getSupport());
                        } else {
                            message2 = manager.transformText("error", ((WebsiteException) e).getType().name(),
                                    websiteException.getMessage());
                            s_emailSender.prepareContactInquiryMailAndSend(queryType, email, name, phone,
                                    contactMessage, sw.toString(), null, null);
                        }
                    } else {
                        message2 = manager
                                .transformText(
                                        "error",
                                        "contact",
                                        "There has been a problem, please try again after sometime. If the problem persists, please contact the system administrator");
                        s_emailSender.prepareContactInquiryMailAndSend(queryType, null, null, null, null,
                                sw.toString(), null, null);
                    }
                    if (message2 != null) {
                        MessageBean.setMessage(request, MessageType.MSG_ERROR, message2);
                    }
                    s_logger.error("Error while adding a new contact request", e);
                }
            } else if ("ack".equals(queryParams[0].trim())) {
                if (queryParams.length > 1) {
                    String encodedToken = queryParams[1];
                    try {
                        String decodedString = new String(Base64.decodeBase64(encodedToken));
                        s_logger.info("Decoded Token: " + decodedString);
                        String[] tokens = decodedString.split("\\$", -1);
                        QueryTypes queryType = QueryTypes.getQueryType(tokens[0]);
                        String emailId = tokens[1];
                        DBUtil.ackContactReq(queryType, emailId);
                        MessageBean.setMessage(request, MessageType.MSG_SUCCESS, "Successfully acknowledged");
                    } catch (Exception e) {
                        StringWriter sw = new StringWriter();
                        PrintWriter pw = new PrintWriter(sw);
                        e.printStackTrace(pw);
                        s_emailSender.sendErrorMail("Error while marking a contact request as acknowledged",
                                sw.toString(), ("ip: " + remoteAddr + ", content: " + encodedToken));
                        MessageBean.setMessage(request, MessageType.MSG_ERROR, "Could not acknowledge");
                    }
                }
            }
        }
        return WebsitePages.CONTACT_PAGE;
    }

    public static void main(String[] args) {
        String decodedString = new String(Base64.decodeBase64("MCRoYXJzaC5hemFkQGdtYWlsLmNvbQ=="));
        System.out.println("Decoded Token: " + decodedString);
    }

    public static void sendContactInquiryReport(String serverName) {
        try {
            List<ContactDTO> inquiryDTOs = DBUtil.fetchUnacknowledgedInquries();
            if (inquiryDTOs == null) {
                return;
            }
            s_emailSender.prepareDigestContactInquiryMailAndSend(inquiryDTOs, serverName);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            s_emailSender.sendErrorMail("Error while sending Digest contact enquiry status email", sw.toString(), null);
        }
    }

    public static void sendNewsletterSubscriptionReport(boolean isFirstRun) {
        try {
            List<ContactDTO> subscriptionDTOS = DBUtil.fetchNewsletterSubscritionAlerts(isFirstRun);
            if (subscriptionDTOS == null) {
                return;
            }
            s_emailSender.prepareDigestNewsletterMailAndSend(subscriptionDTOS);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            s_emailSender.sendErrorMail("Error while sending Digest contact enquiry status email", sw.toString(), null);
        }
    }

    public static WebsitePages setupIndexPagePopup(ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response, String[] queryParams) {
        String ipAddress = request.getRemoteAddr();
        if (queryParams != null && queryParams.length > 0) {
            if ("addContentRequest".equals(queryParams[0])) {
                String content = null;
                try {
                    content = request.getParameter("HTML Content for popup-up", "content",
                            ZilliousSecurityRequestType.DEFAULT_UNSAFE_HTML);
                    Date startDate = DateUtility
                            .parseDateInDDMMMYY(request.getParameter("Start date for the popup to show", "start_date",
                                    ZilliousSecurityRequestType.ALPHANUMERIC));
                    Date endDate = DateUtility.parseDateInDDMMMYY(request.getParameter(
                            "End date for the popup to show", "end_date", ZilliousSecurityRequestType.ALPHANUMERIC));
                    if (content != null && startDate != null && endDate != null) {
                        DBUtil.addPopupContentRequest(content, ipAddress,
                                DateUtility.getCalendarWithTimeStampZeroes(startDate),
                                DateUtility.getCalendarWithTimeStampZeroes(endDate));
                        MessageBean.setMessage(request, MessageType.MSG_SUCCESS,
                                "Your Request has been received. We will get in touch with you shortly!");
                        s_emailSender.preparePopupRequestMailAndSend(content, null, request, response);
                    }
                } catch (WebsiteException e) {
                    String message2 = null;
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    if (e instanceof WebsiteException) {
                        WebsiteException websiteException = e;
                        WebsiteExceptionType type = websiteException.getType();
                        if (type == WebsiteExceptionType.ACTIVE_REQUEST_PRESENT) {
                            message2 = websiteException.getMessage() + " : " + (websiteException.getSupport());
                        } else {
                            message2 = websiteException.getMessage();
                            s_emailSender.preparePopupRequestMailAndSend(content, sw.toString(), null, null);
                        }
                    } else {
                        message2 = "There has been a problem, please try again after sometime. If the problem persists, please contact the system administrator";
                        s_emailSender.preparePopupRequestMailAndSend(content, sw.toString(), null, null);
                    }
                    if (message2 != null) {
                        MessageBean.setMessage(request, MessageType.MSG_ERROR, message2);
                    }
                    s_logger.error("Error while saving the email to the newsletter subscription list", e);

                }
            } else if ("ack".equals(queryParams[0])) {
                if (queryParams.length > 1) {
                    try {
                        Boolean isApprove = Boolean.valueOf(queryParams[1]);
                        if (isApprove) {
                            DBUtil.approveCurrentlyActivePopupRequest(true);
                        } else {
                            DBUtil.approveCurrentlyActivePopupRequest(false);
                        }
                        return WebsitePages.HOME_PAGE;
                    } catch (Exception e) {
                        String message = e.getMessage() + ", " + StringUtility.join(queryParams, ",");
                        MessageBean.setMessage(request, MessageType.MSG_ERROR, message);
                    }
                }
            }
        }
        return WebsitePages.SETUP_POPUP_PAGE;
    }

    public static String getPopupContent() {
        return DBUtil.getPopupContent();
    }

    public static void deactivatePopupRequests() {
        DBUtil.deactivatePopupRequests();
    }
}
