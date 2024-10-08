package com.zillious.corporate_website.portal.ui.dto;

import java.util.HashMap;
import java.util.Map;

import com.zillious.corporate_website.portal.ui.Gender;
import com.zillious.corporate_website.portal.ui.model.Address;
import com.zillious.corporate_website.portal.ui.model.ContactInformation;
import com.zillious.corporate_website.portal.ui.model.User;
import com.zillious.corporate_website.portal.ui.model.ContactInformation.ContactType;
import com.zillious.corporate_website.utils.StringUtility;
import com.zillious.corporate_website.portal.ui.model.UserProfile;

/**
 * @author ashish.bhatia
 *
 */
public class UserProfileChanger extends UserProfile {

    private static final long                    serialVersionUID = -2057216468575896874L;

    private int                                  m_oldid;

    protected String                             m_oldname;

    private Address                              m_oldaddress;

    /**
     * date of birth in DD-MM-YYYY
     */
    private String                               m_olddob;

    private String                               m_oldbloodgp;

    private String                               m_oldpassportNumber;

    private String                               m_olddesignation;

    private Gender                               m_oldgender;

    private String                               m_oldcontactSerial;

    private Map<ContactType, ContactInformation> m_oldcontactInfo;

    private String                               m_oldpictureKey;

    public int getOldid() {
        return m_oldid;
    }

    public void setOldid(int oldid) {
        m_oldid = oldid;
    }

    public String getOldname() {
        return m_oldname;
    }

    public void setOldname(String oldname) {
        m_oldname = oldname;
    }

    public Address getOldaddress() {
        return m_oldaddress;
    }

    public void setOldaddress(Address oldaddress) {
        m_oldaddress = oldaddress;
    }

    public String getOlddob() {
        return m_olddob;
    }

    public void setOlddob(String olddob) {
        m_olddob = olddob;
    }

    public String getOldbloodgp() {
        return m_oldbloodgp;
    }

    public void setOldbloodgp(String oldbloodgp) {
        m_oldbloodgp = oldbloodgp;
    }

    public String getOldpassportNumber() {
        return m_oldpassportNumber;
    }

    public void setOldpassportNumber(String oldpassportNumber) {
        m_oldpassportNumber = oldpassportNumber;
    }

    public String getOlddesignation() {
        return m_olddesignation;
    }

    public void setOlddesignation(String olddesignation) {
        m_olddesignation = olddesignation;
    }

    public Gender getOldgender() {
        return m_oldgender;
    }

    public void setOldgender(Gender oldgender) {
        m_oldgender = oldgender;
    }

    public String getOldcontactSerial() {
        return m_oldcontactSerial;
    }

    public void setOldcontactSerial(String oldcontactSerial) {
        m_oldcontactSerial = oldcontactSerial;
    }

    public Map<ContactType, ContactInformation> getOldcontactInfo() {
        return m_oldcontactInfo;
    }

    public void setOldcontactInfo(Map<ContactType, ContactInformation> oldcontactInfo) {
        m_oldcontactInfo = oldcontactInfo;
    }

    public String getOldpictureKey() {
        return m_oldpictureKey;
    }

    public void setOldpictureKey(String oldpictureKey) {
        m_oldpictureKey = oldpictureKey;
    }

    // ------------------------------------------------------------------------------------------------------------............................

    public String generateProfileChangeMailContent(User userToBeUpdated, User profileUpdator) {

        StringBuilder content = new StringBuilder();
        content.append("Hi ").append(StringUtility.trimAndNullIsDefault(userToBeUpdated.getUserProfile().getName(),
                userToBeUpdated.getEmail()));

        String newLineString = StringUtility.getNewLineString();
        content.append(newLineString);
        content.append(newLineString);

        content.append("Please find changes in your Profile below:");
        content.append(newLineString);
        content.append(newLineString);

        UserProfileChanger userProfileChangeTracker = this;

        int count = 0;

        if (StringUtility.trimAndEmptyIsNull(userProfileChangeTracker.getOldbloodgp()) != null) {
            content.append("Blood Group has been changed from  " + userProfileChangeTracker.getOldbloodgp() + " to : "
                    + userProfileChangeTracker.getbloodgp());
            content.append(newLineString);
            count++;
        }

        if (StringUtility.trimAndEmptyIsNull(userProfileChangeTracker.getOlddob()) != null) {
            content.append("DOB has been changed from  " + userProfileChangeTracker.getOlddob() + " to : "
                    + userProfileChangeTracker.getdob());
            content.append(newLineString);
            count++;
        }

        if (StringUtility.trimAndEmptyIsNull(userProfileChangeTracker.getOldname()) != null) {
            content.append("Name has been changed from  " + userProfileChangeTracker.getOldname() + " to : "
                    + userProfileChangeTracker.getName());
            content.append(newLineString);
            count++;
        }

        if (userProfileChangeTracker.getOldgender() != null
                && StringUtility.trimAndEmptyIsNull(userProfileChangeTracker.getOldgender().name()) != null) {
            content.append("Gender has been changed from  " + userProfileChangeTracker.getOldgender().name() + " to : "
                    + userProfileChangeTracker.getGender().name());
            content.append(newLineString);
            count++;

        }

        if (StringUtility.trimAndEmptyIsNull(userProfileChangeTracker.getOlddesignation()) != null) {
            content.append("Designation has been changed from  " + userProfileChangeTracker.getOlddesignation()
                    + " to : " + userProfileChangeTracker.getDesignation());
            content.append(newLineString);
            count++;
        }

        if (StringUtility.trimAndEmptyIsNull(userProfileChangeTracker.getOldpassportNumber()) != null) {
            content.append("PassportNo has been changed from  " + userProfileChangeTracker.getOldpassportNumber()
                    + " to : " + userProfileChangeTracker.getpassportNumber());
            content.append(newLineString);
            count++;
        }

        if (StringUtility.trimAndEmptyIsNull(userProfileChangeTracker.getOldaddress().getCountry()) != null) {
            content.append("Country has been changed from  " + userProfileChangeTracker.getOldaddress().getCountry()
                    + " to : " + userProfileChangeTracker.getAddress().getCountry());
            content.append(newLineString);
            count++;
        }

        if (StringUtility.trimAndEmptyIsNull(userProfileChangeTracker.getOldaddress().getHouseno()) != null) {
            content.append(
                    "House No/ Street has been changed from  " + userProfileChangeTracker.getOldaddress().getHouseno()
                            + " to : " + userProfileChangeTracker.getAddress().getHouseno());
            content.append(newLineString);
            count++;
        }

        if (StringUtility.trimAndEmptyIsNull(userProfileChangeTracker.getOldaddress().getState()) != null) {
            content.append("State has been changed from  " + userProfileChangeTracker.getOldaddress().getState()
                    + " to : " + userProfileChangeTracker.getAddress().getState());
            content.append(newLineString);
            count++;
        }

        if (StringUtility.trimAndEmptyIsNull(userProfileChangeTracker.getOldaddress().getPincode()) != null) {
            content.append("Pincode has been changed from  " + userProfileChangeTracker.getOldaddress().getPincode()
                    + " to : " + userProfileChangeTracker.getAddress().getPincode());
            content.append(newLineString);
            count++;
        }

        Map<ContactType, ContactInformation> oldcontactInfo = userProfileChangeTracker.getOldcontactInfo();
        Map<ContactType, ContactInformation> contactInfo = userProfileChangeTracker.getContactInfo();

        if (oldcontactInfo != null && contactInfo != null) {

            if (userProfileChangeTracker.m_oldcontactInfo.get(ContactType.PERSONAL) != null
                    && userProfileChangeTracker.m_oldcontactInfo.get(ContactType.PERSONAL).getContactNo() != null) {

                content.append("Personal Contact has been changed from  "
                        + oldcontactInfo.get(ContactType.PERSONAL).getContactNo() + " to : "
                        + contactInfo.get(ContactType.PERSONAL).getContactNo());
                count++;
                content.append(newLineString);
            }

            if (userProfileChangeTracker.m_oldcontactInfo.get(ContactType.OFFICE) != null
                    && userProfileChangeTracker.m_oldcontactInfo.get(ContactType.OFFICE).getContactNo() != null) {
                content.append(
                        "Office Contact has been changed from  " + oldcontactInfo.get(ContactType.OFFICE).getContactNo()
                                + " to : " + contactInfo.get(ContactType.OFFICE).getContactNo());
                count++;
                content.append(newLineString);
            }

            if (userProfileChangeTracker.m_oldcontactInfo.get(ContactType.OTHER) != null
                    && userProfileChangeTracker.m_oldcontactInfo.get(ContactType.OTHER).getContactNo() != null) {
                content.append("Emergency Contact has been changed from  "
                        + oldcontactInfo.get(ContactType.OTHER).getContactNo() + " to : "
                        + contactInfo.get(ContactType.OTHER).getContactNo());
                count++;
                content.append(newLineString);
            }
        }

        content.append(newLineString);
        content.append("If you need further information on this, please contact ").append(StringUtility
                .trimAndNullIsDefault(profileUpdator.getUserProfile().getName(), profileUpdator.getEmail()))
                .append(".");

        content.append(newLineString);
        content.append("Thanks,");
        content.append(newLineString);
        content.append("Zillious Solutions Pvt Ltd");
        if (count == 0) {
            return null;
        }

        return content.toString();
    }

    public void addOldContact(ContactInformation contact) {
        if (contact == null || contact.getContactType() == null) {
            return;
        }

        if (m_oldcontactInfo == null) {
            m_oldcontactInfo = new HashMap<ContactInformation.ContactType, ContactInformation>();
        }

        m_oldcontactInfo.put(contact.getContactType(), contact);
    }

}
