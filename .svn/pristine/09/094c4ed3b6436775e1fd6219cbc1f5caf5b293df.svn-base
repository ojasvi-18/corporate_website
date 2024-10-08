package com.zillious.corporate_website.ui.security;

public enum ZilliousSecurityRequestType {

    DEFAULT_SAFE_STRING("DefaultSafeText", "Invalid Safe Text", 2048),

    CORPORATE_USER_NAME("CorporateUserName", "Invalid Corporate Username", 2048),

    REPORTING_PARAM("DefaultSafeTextWithAnyText", "Invalid Reporting Param", 2048),

    DEFAULT_SAFE_STRING_WITH_ANY_TEXT("DefaultSafeTextWithAnyText", "Invalid Safe Text Any", 2048),

    DEFAULT_SAFE_STRING_WITH_HASH("DefaultSafeTextWithHash", "Invalid Safe Text Hash", 2048),

    DEFAULT_UNSAFE_STRING("UnsafeXssText", "Invalid Text", 102400),

    DEFAULT_UNSAFE_HTML("UnsafeXssText", "Invalid Html", 102400),

    DEFAULT_UNSAFE_XML("UnsafeXssText", "Invalid XML", 102400),

    POSITIVE_INTEGER("PositiveInteger", "Invalid Positive Integer", 50),

    INTEGER("Integer", "Invalid Integer", 50),

    ALPHANUMERIC("AlphaNumeric", "Invalid AlphaNumeric", 100),

    NUMBER("Number", "Invalid Number", 50),

    BASE64("Base64Text", "Invalid Base64", 1024000),

    EMAIL("Email", "Invalid Email", 100),

    MULTIPLE_EMAILS("MultiEmails", "Invalid Multiple Emails", 1000),

    URL("URL", "Invalid URL", 2048),

    PASSWORD("Password", "Invalid Password", 20),

    USER_ROLE("UserRole", "Invalid User", 1),

    USER_NAME("DefaultSafeText", "Invalid Name", 100),

    COMPANY_NAME("CompanyName", "Invalid Company Name", 300),

    ENUM_INTEGER("PositiveInteger", "Invalid Positive Integer", 10),

    ENUM_ALPHA("DefaultSafeText", "Invalid Alphanumeric", 100),

    ID_IATA("AlphaNumeric", "Invalid Alphanumeric", 5),

    ID_INTEGER("Integer", "Invalid Integer", 20),

    ID_POSITIVE_INTEGER("PositiveInteger", "Invalid Positive Integer", 20),

    ID_ALPHA("AlphaNumeric", "Invalid Alpha", 25),

    ADDRESS_NAME("DefaultSafeText", "Invalid Address", 150),

    STREET_NAME("StreetName", "Invalid Street Name", 200),

    ADDRESS_PINCODE("DefaultSafeText", "Invalid Pincode", 20),

    ADDRESS_PHONE("DefaultSafeText", "Invalid Phone", 100),

    ADDRESS_LAT_LONG("DefaultSafeText", "Invalid Latitude / Longitude", 15),

    DATE_TIME("DateTime", "Invalid Date / Time", 20),

    INPUT_CHECKBOX("InputCheckbox", "Invalid Checkbox", 5),

    ALPHA_WITH_SPACE("AlphaWithSpace", "Invalid Alphabets", 100),

    JSON_STRING("Json", "Invalid Json", 2048),

    IP_ADDRESS("IPAddress", "Invalid IP Address ", 20)

    ;

    private String m_validationRule;
    private int    m_defaultMaxLength;
    private String m_errorMessage;

    ZilliousSecurityRequestType(String validationRule, String errorMessage, int defaultMaxLen) {
        m_validationRule = validationRule;
        m_defaultMaxLength = defaultMaxLen;
        m_errorMessage = errorMessage;
    }

    ZilliousSecurityRequestType(String validationRule, String errorMessage) {
        this(validationRule, errorMessage, -1);
    }

    public String getValidationRule() {
        return m_validationRule;
    }

    public int getDefaultMaxLength() {
        return m_defaultMaxLength;
    }

    public String getErrorMessage() {
        return m_errorMessage;
    }
}