package com.zillious.corporate_website.portal.ui.model;

import com.google.gson.JsonObject;
import com.zillious.corporate_website.utils.StringUtility;

public class ContactInformation {

    private String      m_contactNo;
    private ContactType m_contactType;

    public ContactInformation() {
    }

    public ContactInformation(String contNo, String contTypeStr) {
        m_contactNo = contNo;
        m_contactType = ContactType.deserialize(contTypeStr);
    }

    public ContactInformation(ContactType type) {
        m_contactType = type;
        m_contactNo = new String();
    }

    public static ContactInformation deserialize(String contactSerial, String delimRegex) {
        ContactInformation contact = new ContactInformation();
        String[] tokens = contactSerial.split(delimRegex);
        int idx = 0;
        if (idx < tokens.length) {
            contact.setContactType(ContactType.deserialize(tokens[idx++]));
        }

        if (idx < tokens.length) {
            contact.setContactNo(tokens[idx++]);
        } else {
            contact.setContactNo("");
        }
        return contact;
    }

    public ContactType getContactType() {
        return m_contactType;
    }

    /**
     * flattens the Contact information object into delim separated string
     * values
     * 
     * @param delimlevel
     * @return
     */
    public String serialize(int delimlevel) {
        StringBuffer buf = new StringBuffer();
        String delim = StringUtility.getDelim(delimlevel);
        buf.append(getContactType().getSerial()).append(delim);
        buf.append(getContactNo());
        return buf.toString();
    }

    public enum ContactType {
        PERSONAL('P', "PersonalContact"), OFFICE('F', "OfficeContact"), OTHER('O', "OtherContact");

        ;

        private String m_serial;
        private String m_displayName;

        ContactType(char serial, String displayName) {
            m_serial = "" + serial;
            m_displayName = displayName;
        }

        // get serial
        public String getSerial() {
            return m_serial;
        }

        public String getdisplayName() {
            return m_displayName;
        }

        public static ContactType deserialize(String value) {
            ContactType[] contacttype = ContactType.values();
            for (ContactType ctype : contacttype) {
                if (ctype.getSerial().equals(value)) {
                    return ctype;
                }
            }
            return null;
        }

    }

    public void setContactType(ContactType contactType) {
        m_contactType = contactType;
    }

    /**
     * @return the contactNo
     */
    public String getContactNo() {
        return m_contactNo;
    }

    /**
     * @param contactNo the contactNo to set
     */
    public void setContactNo(String contactNo) {
        m_contactNo = contactNo;
    }

    public JsonObject getAsJson() {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", getContactType().getSerial());
        obj.addProperty("number", getContactNo());
        return obj;
    }

}
