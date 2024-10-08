package com.zillious.corporate_website.ui.navigation;

/**
 * @author Nishant
 * 
 */
public enum WebsiteExceptionType {
    DUPLICATE_CONTACT_REQUEST("The request for this query has already been received by the system."),

    INVALID_QUERY("Invalid query"),

    CAPTCHA_FAIL("Could not verify the captcha information. Please try after sometime."),

    OTHER("All other unaccounted errors"),

    NEWSLETTER_SUBSCRIBED("We have detected an existing subscription against the email id."),

    ACTIVE_REQUEST_PRESENT("We have detected an existing active request. Please discontinue that first."),

    INVALID_ACCESS("The link is not directly accessible"),

    USER_AUTH_FAILED("Invalid User account or password!"),

    USER_DISABLED("User account is disabled!"),

    USER_LOGIN_INFO("User Login info could not be added!"),

    CANNOTRETURNPAGE("This action should not be returning an html page"),

    INVALID_PROFILE("profile details missing necessary information"),

    INVALID_REQUEST("necessary information missing"),

    SYSTEM_ERROR("Please contact the administrator"),

    MISSING_USER("User information not found"),

    LEAVE_REQUEST_NOT_FOUND("Leave request not found in the system"),

    SAME_USER("Users are same"),

    MISSINGDATA("Insufficient data to perform the required operation."), WRONG_DATES("Please check the dates."),

    EXCEPTION_ERROR("Error occur in process either contect admisntrator or try again"),

    INVALID_Action_REQUEST("Not a valid user for requested acion"),

    INSUFFICIENT_RECIPIENTS("Not enough users to send e-mail to"), ;

    private String m_desc;

    WebsiteExceptionType(String desc) {
        m_desc = desc;
    }

    public String getDesc() {
        return m_desc;
    }

    public void setDesc(String desc) {
        m_desc = desc;
    }

}
