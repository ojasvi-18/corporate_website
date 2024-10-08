package com.zillious.corporate_website.ui.beans;

import java.net.URLEncoder;

import com.zillious.corporate_website.ui.security.ZilliousSecurityRequestType;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.utils.HtmlUtility;
import com.zillious.corporate_website.utils.StringUtility;

public class MessageBean {
    public static final String  EXCEPTION_REF_ID              = "exceptionRefId";
    public static final String  PROFILE_SYNC_EXCEPTION_REF_ID = "profileExceptionRefId";

    public static void setException(ZilliousSecurityWrapperRequest request, Throwable t) {
        StringBuilder b = new StringBuilder();
        b.append(t.getClass().getName()).append(": ").append(t.getMessage());
        b.append(StringUtility.getNewLineString());
        StackTraceElement[] elems = t.getStackTrace();
        for (StackTraceElement elem : elems) {
            b.append(" at ").append(elem.toString()).append(StringUtility.getNewLineString());
        }
        b.append(StringUtility.getNewLineString());
        request.setAttribute(MessageType.EXCEPTION_TRACE.name(), b.toString());

    }

    public static void setMessage(ZilliousSecurityWrapperRequest request, MessageType type, String message) {
        request.setAttribute(type.name(), message);
    }

    public static String getMessage(ZilliousSecurityWrapperRequest request, MessageType type) {
        String msg = (String) request.getAttribute(type.name());
        if (msg == null) {
            msg = HtmlUtility.encodeForHTML(request.getParameter("Message Parameter", type.name(),
                    ZilliousSecurityRequestType.DEFAULT_SAFE_STRING, 500), null);
        }
        return msg;
    }

    public static String appendMessages(ZilliousSecurityWrapperRequest request, String url) {
        for (MessageType mType : MessageType.values()) {
            String msg = StringUtility.trimAndEmptyIsNull((String) request.getAttribute(mType.name()));
            if (msg != null) {
                try {
                    msg = URLEncoder.encode(msg, "UTF-8");
                } catch (Exception e) {
                }
                url += ((url.indexOf("?") == -1) ? "?" : "&") + mType.name() + "=" + msg;
            }
        }
        return url;
    }

    public static enum MessageType {
        MSG_ERROR, MSG_SUCCESS, MSG_VALIDATION_ERROR, EXCEPTION_TRACE, PROFILE_SYNC_TRACE;
    }

    public static enum Message {
        ACCESS_RESTRICTED("Access not allowed! If you are new user please register with us!"),

        AUTH_KEY_INVALID("Invalid User or expired Authorization Key!"),

        AUTHENTICATION_ERROR("Error in authentication!"),

        AUTHENTICATION_FAILED("User Authentication Failed."),

        DATA_INSUFFICIENT("Insufficient data provided!"),

        EMAIL_INVALID("Email invalid!"),

        EMAIL_VERIFICATION_PENDING("Email verification pending!"),

        EMAIL_VERIFICATION_CODE_SENT("Verification Code has been sent to the email address."),

        EMAIL_VERIFIED("User Email Verified."),

        EMAIL_VERIFICATION_ERROR("Invalid email/ verification code or email already verified!"),

        EMAIL_ALREADY_EXISTS("Given email already exists!"),

        EMAIL_OR_PASSWORD_INVALID("Email or password invalid!"),

        INVALID_REMEMBER_ME_CODE("Remember Me Code invalid"),

        DOMAIN_NAME_NOT_EXIST("Domain name does not exist!"),

        GCM_SEND_ERROR("Could not send message to GCM"),

        USERNAME_OR_PASSWORD_INVALID("UserName or password invalid!"),

        FILE_ALREADY_EXISTS("File Name is already used."),

        FILE_DOES_NOT_EXIST("File does not exist."),

        FILE_ALREADY_TOO_MANY("You have exceeded the maximum number of file uploads permited."),

        FILE_TOO_BIG("Your file size exceeds maximum limit of "),

        IP_RESTRICTED("IP restricted: "),

        INFO_SAVED("Information saved!"),

        MOBILE_ALREADY_EXISTS("Given mobile no. already exists!"),

        NAME_INVALID("Name invalid!"),

        PASSWORD_INVALID_ERROR("Password is not a valid 8 character long password!"),

        PASSWORD_EXPIRED("Your password has expired. Please update it to start using your account!"),

        PASSWORD_MISMATCH_ERROR("New Password and Confirm Password does not match!"),

        PASSWORD_SAME("New password should not be equal to the current password. Please enter a different password."),

        PASSWORD_EXISTS_IN_HISTORY("New Password should not be among last three passwords!"),

        PASSWORD_SENT_TO_MAIL("Password reset and sent to your email."),

        PASSWORD_RESET_INSTRUCTION_SENT_TO_MAIL("Password Reset instructions have been sent to your email address."),

        PASSWORD_RESET_SUCCESSFUL("Congratulations! Your password has been changed successfully."),

        PASSWORD_RESET_ERROR("Error in reseting password!"),

        PASSWORD_RESET_FAILED("Unable to reset password"),

        PASSWORD_UPDATE("Please update your password!"),

        PROFILE_UPDATE_ERROR("Error in updating user profile!"),

        TOO_MANY_FAILED_ATTEMPTS("Account currently disabled for failed attempts!"),

        USER_DISABLED("Account currently disabled!"),

        USER_DEACTIVATED("Account currently deactivated!"),

        USER_NOT_EXIST("User does not Exist!"),

        USER_REGISTERED("User registered successfully!"),

        USER_REGISTRATION_ERROR("Error in registering new user!"),

        WRONG_CURRRENT_PASSWORD("Authentication failed. Current password provided by you is not currect!"),

        DATE_FORMAT_INVALID("Invalid Date format"),

        WRONG_CAPTCHA_ENTRY("Captcha entry does not match!"),

        CAPTCHA_VERIFICATION_ERROR("Error in verifying captcha entry!"),

        LOGOUT_SUCCESSFUL("Logout Successfully!"),

        ;

        ;

        private String m_msgContent;

        Message(String msgContent) {
            m_msgContent = msgContent;
        }

        public String getMsgContent() {
            return m_msgContent;
        }
    }

    public static void setMessageInRequest(ZilliousSecurityWrapperRequest zillousRequest, String msg) {
        String comments = StringUtility.trimAndEmptyIsNull(msg);
        if (comments != null) {
            MessageBean.setMessage(zillousRequest, MessageType.MSG_ERROR, comments);
        }
    }
}
