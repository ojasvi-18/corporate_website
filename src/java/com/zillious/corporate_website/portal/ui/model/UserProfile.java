package com.zillious.corporate_website.portal.ui.model;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zillious.corporate_website.portal.entity.utility.GenderSerializer;
import com.zillious.corporate_website.portal.ui.Gender;
import com.zillious.corporate_website.portal.ui.dto.UserProfileChanger;
import com.zillious.corporate_website.portal.ui.model.ContactInformation.ContactType;
import com.zillious.corporate_website.portal.utility.CloudManager;
import com.zillious.corporate_website.ui.navigation.WebsiteException;
import com.zillious.corporate_website.ui.navigation.WebsiteExceptionType;
import com.zillious.corporate_website.utils.DateUtility;
import com.zillious.corporate_website.utils.StringUtility;

@Entity
@Table(name = "user_profile")
public class UserProfile implements DBObject {

    private static final long                    serialVersionUID = -7815044639750913111L;

    @Transient
    private static Logger                        s_logger         = Logger.getLogger(UserProfile.class);

    @Id
    @Column(name = "profile_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int                                  m_id;

    @Column(name = "name", nullable = false)
    protected String                             m_name;

    @Embedded
    private Address                              m_address;

    /**
     * date of birth in DD-MM-YYYY
     */
    @Column(name = "dob", nullable = true)
    private String                               m_dob;

    @Column(name = "bloodgroup", nullable = true)
    private String                               m_bloodgp;

    @Column(name = "passportNo", nullable = true)
    private String                               m_passportNumber;

    @Column(name = "designation", nullable = true)
    private String                               m_designation;

    @Column(name = "gender", nullable = true)
    @Convert(converter = GenderSerializer.class)
    private Gender                               m_gender;

    @Column(name = "contacts", nullable = true)
    private String                               m_contactSerial;

    @Transient
    private Map<ContactType, ContactInformation> m_contactInfo;

    @Column(name = "picture", nullable = true)
    private String                               m_pictureKey;

    public UserProfile(String tempName) {
        setName(tempName);
    }

    public UserProfile() {
    }

    private void serializeContactList() {
        if (m_contactInfo == null) {
            m_contactInfo = new HashMap<ContactInformation.ContactType, ContactInformation>();
        } else {
            // Collections.sort(m_contactInfo, new
            // Comparator<ContactInformation>() {
            //
            // @Override
            // public int compare(ContactInformation o1, ContactInformation o2)
            // {
            // if (o1 == null && o2 == null) {
            // return 0;
            // }
            // if (o1 == null) {
            // return 5;
            // }
            // if (o2 == null) {
            // return -5;
            // }
            //
            // if (o1.getContactType().equals(o2.getContactType())) {
            // return 0;
            // }
            //
            // switch (o1.getContactType()) {
            // case PERSONAL:
            // return -5;
            // case OFFICE:
            // if (o2.getContactType() == ContactType.OFFICE) {
            // return 5;
            // }
            // return -5;
            // case OTHER:
            // return 5;
            // }
            // return 0;
            // }
            // });
        }

        StringBuffer buf = new StringBuffer();
        int delimLevel = 0;
        String delim = StringUtility.getDelim(delimLevel);

        ContactInformation personalContact = m_contactInfo.get(ContactType.PERSONAL);
        if (personalContact == null) {
            personalContact = new ContactInformation(ContactType.PERSONAL);
        }
        buf.append(personalContact.serialize(delimLevel + 1)).append(delim);

        ContactInformation officeContact = m_contactInfo.get(ContactType.OFFICE);
        if (officeContact == null) {
            officeContact = new ContactInformation(ContactType.OFFICE);
        }
        buf.append(officeContact.serialize(delimLevel + 1)).append(delim);

        ContactInformation otherContact = m_contactInfo.get(ContactType.OTHER);
        if (otherContact == null) {
            otherContact = new ContactInformation(ContactType.OTHER);
        }
        buf.append(otherContact.serialize(delimLevel + 1));

        m_contactSerial = buf.toString();

    }

    /**
     * ContactSerial Will be
     */
    private void deserializeContactInfo() {

        if (m_contactSerial == null || m_contactSerial.isEmpty()) {
            return;
        }

        String delimRegex = StringUtility.getDelimRegex(0);
        String delimRegexInner = StringUtility.getDelimRegex(1);
        List<String> contactToken = StringUtility.splitToStringList(m_contactSerial, delimRegex);
        m_contactInfo = new HashMap<ContactType, ContactInformation>();

        int i = 0;
        ContactInformation personalContact = ContactInformation.deserialize(contactToken.get(i++), delimRegexInner);
        m_contactInfo.put(ContactType.PERSONAL, personalContact);

        ContactInformation officeContact = ContactInformation.deserialize(contactToken.get(i++), delimRegexInner);
        m_contactInfo.put(ContactType.OFFICE, officeContact);

        ContactInformation otherContact = ContactInformation.deserialize(contactToken.get(i++), delimRegexInner);
        m_contactInfo.put(ContactType.OTHER, otherContact);
    }

    public int getId() {
        return m_id;
    }

    public String getdob() {
        return m_dob;
    }

    public void setdob(String m_dob) {
        this.m_dob = m_dob;
    }

    public String getbloodgp() {
        return m_bloodgp;
    }

    public void setbloodgp(String m_bloodgp) {
        this.m_bloodgp = m_bloodgp;
    }

    public String getpassportNumber() {
        return m_passportNumber;
    }

    public void setpassportNumber(String m_passportNumber) {
        this.m_passportNumber = m_passportNumber;
    }

    public Gender getGender() {
        return m_gender;
    }

    public String getgender() {
        Gender gender = getGender();
        if (gender == null) {
            return null;
        }

        return gender.getCode();
    }

    public void setGender(Gender m_gender) {
        this.m_gender = m_gender;
    }

    /**
     * @return the name
     */

    public String getName() {
        return m_name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        m_name = name;
    }

    public Address getAddress() {
        return m_address;
    }

    public void setAddress(Address m_address) {
        this.m_address = m_address;
    }

    /**
     * DOB in memory is in DD-MM-YYYY. Converting to MM-DD-YYYY for json use.
     * 
     * in memory object converted to json format to be sent to the ui
     * 
     * @return
     */
    public JsonObject convertIntoJson(boolean isSupervisor) {
        JsonObject profile = new JsonObject();

        JsonObject personalDetails = new JsonObject();

        personalDetails.addProperty("Name", StringUtility.trimAndNullIsEmpty(getName()));

        // only ADIMN ROLES have right to edit designation of a user
        if (isSupervisor) {
            personalDetails.addProperty("Designation", StringUtility.trimAndNullIsEmpty(getDesignation()));
        }
        String dobInDDMMYYYY = StringUtility.trimAndNullIsEmpty(getdob());
        String dateInMMDDYYYYDash = DateUtility
                .getDateInMMDDYYYYDash(DateUtility.parseDateInDDMMYYYYDashNullIfError(dobInDDMMYYYY));
        personalDetails.addProperty("DOB", StringUtility.trimAndNullIsEmpty(dateInMMDDYYYYDash));
        personalDetails.addProperty("BloodGroup", StringUtility.trimAndNullIsEmpty(getbloodgp()));
        Gender gender = getGender();
        JsonObject genderJson = null;
        if (gender != null) {
            genderJson = gender.getJson();
        } else {
            genderJson = new JsonObject();
        }
        personalDetails.add("Gender", genderJson);
        personalDetails.addProperty("PassportNo", StringUtility.trimAndNullIsEmpty(getpassportNumber()));
        personalDetails.addProperty("isEditable", true);

        profile.add("PersonalDetails", personalDetails);

        // Contact information
        deserializeContactInfo();

        JsonObject contactDetailsJson = new JsonObject();

        if (m_contactInfo == null) {
            m_contactInfo = new HashMap<ContactInformation.ContactType, ContactInformation>();
        }

        ContactInformation personalContact = m_contactInfo.get(ContactType.PERSONAL);
        if (personalContact == null) {
            personalContact = new ContactInformation(ContactType.PERSONAL);
        }
        JsonObject contact = personalContact.getAsJson();
        contactDetailsJson.add(ContactType.PERSONAL.name(), contact);

        ContactInformation officeContact = m_contactInfo.get(ContactType.OFFICE);
        if (officeContact == null) {
            officeContact = new ContactInformation(ContactType.OFFICE);
        }
        contact = officeContact.getAsJson();
        contactDetailsJson.add(ContactType.OFFICE.name(), contact);

        ContactInformation otherContact = m_contactInfo.get(ContactType.OTHER);
        if (otherContact == null) {
            otherContact = new ContactInformation(ContactType.OTHER);
        }
        contact = otherContact.getAsJson();
        contactDetailsJson.add(ContactType.OTHER.name(), contact);
        profile.add("ContactDetails", contactDetailsJson);

        Address address = getAddress();
        JsonObject addressDetails = new JsonObject();
        if (address != null) {
            addressDetails.addProperty("HNoStreet", StringUtility.trimAndNullIsEmpty(address.getHouseno()));

            addressDetails.addProperty("State", StringUtility.trimAndNullIsEmpty(address.getState()));
            addressDetails.addProperty("Pin", StringUtility.trimAndNullIsEmpty(address.getPincode()));
            addressDetails.addProperty("Country", StringUtility.trimAndNullIsEmpty(address.getCountry()));
            addressDetails.addProperty("isEditable", true);
        }
        profile.add("AddressDetails", addressDetails);

        if (getPictureKey() != null) {
            String completeURL = CloudManager.AMAZONS3.getCompleteDisplayURL(getPictureKey());
            if (completeURL != null) {
                addDisplayPictureURL(profile, completeURL);
            }
        }

        profile.addProperty("profile_id", String.valueOf(getId()));
        s_logger.debug("profile json: " + profile);
        return profile;
    }

    /**
     * DOB should be MM-DD-YYYY, which is converted into DD-MM-YYYY for internal
     * use
     * 
     * @param profileJson
     * @param changesTrackingProfile
     * @param b
     * @return
     * @throws WebsiteException
     */
    public void updateValuesFromJson(JsonObject profileJson, boolean isUpdatorAdmin,
            UserProfileChanger changesTrackingProfile) throws WebsiteException {
        JsonObject personalDetails = null;

        if (profileJson.get("profile_id") == null) {
            throw new WebsiteException(WebsiteExceptionType.INVALID_PROFILE);
        }

        UserProfile userProfile = this;
        userProfile.setId(profileJson.get("profile_id").getAsInt());

        if (profileJson.getAsJsonObject("PersonalDetails") != null) {
            personalDetails = profileJson.getAsJsonObject("PersonalDetails");
        }

        if (personalDetails != null) {
            // here changing regex [A-Z][a-zA-Z] to /^[a-zA-Z]
            if (personalDetails.getAsJsonPrimitive("BloodGroup") != null) {
                String newBloodGroup = StringUtility.trimAndEmptyIsNull(
                        personalDetails.getAsJsonPrimitive("BloodGroup").toString().replaceAll("^\"|\"$", ""));

                String oldBloodGroup = userProfile.getbloodgp();

                boolean isChangeInProfileBloodGroup = compareOldAndNewProfile(newBloodGroup, oldBloodGroup);

                if (isChangeInProfileBloodGroup) {
                    changesTrackingProfile.setbloodgp(newBloodGroup);
                    changesTrackingProfile.setOldbloodgp(oldBloodGroup);
                }

                userProfile.setbloodgp(newBloodGroup);
            }

            if (personalDetails.getAsJsonPrimitive("Name") != null) {
                String newName = StringUtility.trimAndEmptyIsNull(
                        personalDetails.getAsJsonPrimitive("Name").toString().replaceAll("^\"|\"$", ""));

                String oldName = userProfile.getName();

                boolean isChangeInProfileName = compareOldAndNewProfile(newName, oldName);

                if (isChangeInProfileName) {
                    changesTrackingProfile.setName(newName);
                    changesTrackingProfile.setOldname(oldName);
                }

                userProfile.setName(newName);

            }

            if (personalDetails.getAsJsonPrimitive("DOB") != null) {
                // MM-DD-YYYY from the UI
                String newDob = StringUtility
                        .trimAndEmptyIsNull(personalDetails.get("DOB").getAsString().replaceAll("^\"|\"$", ""));
                String oldDob = userProfile.getdob();

                Date dateInMMDDYYYY = DateUtility.parseDateInMMDDYYYYDash(newDob);
                Date dateInMMDDYYYY2 = DateUtility.parseDateInDDMMYYYYDashNullIfError(oldDob);

                String dateInDDMMYYYY = DateUtility.getDateInDDMMYYYYDash(dateInMMDDYYYY);

                String oldDateInDDMMYYYY = DateUtility.getDateInDDMMYYYYDash(dateInMMDDYYYY2);

                boolean isChangeInProfileDob = compareOldAndNewProfile(dateInDDMMYYYY, oldDateInDDMMYYYY);

                if (isChangeInProfileDob) {
                    changesTrackingProfile.setdob(dateInDDMMYYYY);
                    changesTrackingProfile.setOlddob(oldDob);
                }
                
                userProfile.setdob(dateInDDMMYYYY);

            }

            if (personalDetails.get("Gender") != null) {
                String genderString = personalDetails.get("Gender").getAsJsonObject().get("code").getAsString();
                Gender newGender = Gender.getByCode(genderString);
                if (newGender != null) {
                    Gender oldGender = userProfile.getGender();
                    userProfile.setGender(newGender);
                    boolean isChangeInProfileGender = compareOldAndNewProfile(newGender, oldGender);

                    if (isChangeInProfileGender) {
                        changesTrackingProfile.setGender(newGender);
                        changesTrackingProfile.setOldgender(oldGender);
                    }
                }
            }

            if (personalDetails.getAsJsonPrimitive("Designation") != null && isUpdatorAdmin) {
                String newDesignation = StringUtility.trimAndEmptyIsNull(
                        personalDetails.getAsJsonPrimitive("Designation").toString().replaceAll("^\"|\"$", ""));

                String oldDesignation = userProfile.getDesignation();

                boolean isChangeInProfileDesignation = compareOldAndNewProfile(newDesignation, oldDesignation);

                if (isChangeInProfileDesignation) {
                    changesTrackingProfile.setDesignation(newDesignation);
                    changesTrackingProfile.setOlddesignation(oldDesignation);
                } 
                userProfile.setDesignation(newDesignation);
            }
            if (personalDetails.getAsJsonPrimitive("PassportNo") != null) {
                String newPassportNo = StringUtility.trimAndEmptyIsNull(
                        personalDetails.getAsJsonPrimitive("PassportNo").toString().replaceAll("^\"|\"$", ""));

                String oldPassportNo = userProfile.getpassportNumber();

                boolean isChangeInProfilePassportNo = compareOldAndNewProfile(newPassportNo, oldPassportNo);

                if (isChangeInProfilePassportNo) {
                    changesTrackingProfile.setpassportNumber(newPassportNo);
                    changesTrackingProfile.setOldpassportNumber(oldPassportNo);
                    
                }
                userProfile.setpassportNumber(newPassportNo);
            }

        }

        // handling Contact information

        JsonElement contactElement = profileJson.get("ContactDetails");
        userProfile.deserializeContactInfo();

        JsonObject contactObj;
        if (contactElement == null) {
            contactObj = new JsonObject();
        } else {
            contactObj = contactElement.getAsJsonObject();
        }

        JsonElement personalContactElement = contactObj.get(ContactType.PERSONAL.name());
        ContactInformation contact = null;
        if (personalContactElement == null) {
            contact = new ContactInformation(ContactType.PERSONAL);
        } else {
            contact = new ContactInformation();
            JsonObject personalContactObject = personalContactElement.getAsJsonObject();
            contact.setContactType(ContactType.deserialize(personalContactObject.get("type").getAsString()));
            JsonElement numberElement = personalContactObject.get("number");
            String contactNumber = null;
            if (numberElement == null) {
                contactNumber = "";
            } else {
                contactNumber = numberElement.getAsString();
            }
            
            contact.setContactNo(contactNumber);

            String oldContactNo = userProfile.getContactInfo().get(ContactType.PERSONAL).getContactNo();
            boolean isChangeInContactNo = compareOldAndNewProfile(contactNumber, oldContactNo);

            if (isChangeInContactNo) {
                ContactInformation contactOld = new ContactInformation();
                contactOld.setContactType(ContactType.PERSONAL);
                contactOld.setContactNo(oldContactNo);
                changesTrackingProfile.addContact(contact);
                changesTrackingProfile.addOldContact(contactOld);
            }
            

        }
        userProfile.addContact(contact);

        // Office number
        JsonElement officeContactElement = contactObj.get(ContactType.OFFICE.name());
        contact = null;
        if (officeContactElement == null) {
            contact = new ContactInformation(ContactType.OFFICE);
        } else {
            contact = new ContactInformation();
            JsonObject officeContactObject = officeContactElement.getAsJsonObject();
            contact.setContactType(ContactType.deserialize(officeContactObject.get("type").getAsString()));
            JsonElement numberElement = officeContactObject.get("number");
            String contactNumber = null;
            if (numberElement == null) {
                contactNumber = "";
            } else {
                contactNumber = numberElement.getAsString();
            }
            
            contact.setContactNo(contactNumber);
            
            String oldOfficeNo = userProfile.m_contactInfo.get(ContactType.OFFICE).getContactNo();
            boolean isChangeInOfficeNo = compareOldAndNewProfile(contactNumber, oldOfficeNo);

            if (isChangeInOfficeNo) {
                ContactInformation oldOfficeContact = new ContactInformation();
                oldOfficeContact.setContactType(ContactType.OFFICE);
                oldOfficeContact.setContactNo(oldOfficeNo);
                changesTrackingProfile.addContact(contact);
                changesTrackingProfile.addOldContact(oldOfficeContact);
            }
        }
        userProfile.addContact(contact);

        // Other number
        JsonElement otherContactElement = contactObj.get(ContactType.OTHER.name());
        contact = null;
        if (otherContactElement == null) {
            contact = new ContactInformation(ContactType.OTHER);
        } else {
            contact = new ContactInformation();
            JsonObject otherContactObject = otherContactElement.getAsJsonObject();
            contact.setContactType(ContactType.deserialize(otherContactObject.get("type").getAsString()));
            JsonElement numberElement = otherContactObject.get("number");
            String contactNumber = null;
            if (numberElement == null) {
                contactNumber = "";
            } else {
                contactNumber = numberElement.getAsString();
            }
            
            contact.setContactNo(contactNumber);
            
            String oldOtherNo = userProfile.m_contactInfo.get(ContactType.OTHER).getContactNo();
            boolean isChangeInOtherNo = compareOldAndNewProfile(contactNumber, oldOtherNo);

            if (isChangeInOtherNo) {
                ContactInformation contactOtherNo = new ContactInformation();
                contactOtherNo.setContactType(ContactType.OTHER);
                contactOtherNo.setContactNo(oldOtherNo);
                changesTrackingProfile.addContact(contact);
                changesTrackingProfile.addOldContact(contactOtherNo);
            }

        }
        userProfile.addContact(contact);

        // Important!! converts list to DB info
        // String OldHouseNo = userProfile.getAddress().getHouseno();

        userProfile.serializeContactList();

        Address address = new Address();
        Address OldAddress = new Address();
        changesTrackingProfile.setAddress(OldAddress);

        JsonObject addressDetails = profileJson.getAsJsonObject("AddressDetails");

        if (addressDetails != null) {
            // if (addressDetails.get("Country") != null) {
            //
            // address.setCountry(addressDetails.getAsJsonPrimitive("Country").toString().replaceAll("^\"|\"$",
            // ""));
            // }

            // .................
            if (addressDetails.get("Country") != null) {
                String NewCountry = StringUtility.trimAndEmptyIsNull(
                        addressDetails.getAsJsonPrimitive("Country").toString().replaceAll("^\"|\"$", ""));

                String OldCountry = userProfile.getAddress().getCountry();

                address.setCountry(addressDetails.getAsJsonPrimitive("Country").toString().replaceAll("^\"|\"$", ""));

                boolean isChangeInProfileCountry = compareOldAndNewProfile(NewCountry, OldCountry);

                if (isChangeInProfileCountry) {
                    OldAddress.setCountry(OldCountry);
                }
            }
            // ......................

            // if (addressDetails.get("HNoStreet") != null) {
            // address.setHouseno(addressDetails.getAsJsonPrimitive("HNoStreet").toString().replaceAll("^\"|\"$",
            // ""));
            // }
            if (addressDetails.get("HNoStreet") != null) {
                String newHNoStreet = StringUtility.trimAndEmptyIsNull(
                        addressDetails.getAsJsonPrimitive("HNoStreet").toString().replaceAll("^\"|\"$", ""));

                String oldHNoStreet = userProfile.getAddress().getHouseno();

                address.setHouseno(addressDetails.getAsJsonPrimitive("HNoStreet").toString().replaceAll("^\"|\"$", ""));
                boolean isChangeInProfileHNoStreet = compareOldAndNewProfile(newHNoStreet, oldHNoStreet);

                if (isChangeInProfileHNoStreet) {
                    OldAddress.setHouseno(oldHNoStreet);

                }
            }

            // if (addressDetails.get("State") != null) {
            // address.setState(addressDetails.getAsJsonPrimitive("State").toString().replaceAll("^\"|\"$",
            // ""));
            // }

            if (addressDetails.get("State") != null) {
                String newState = StringUtility.trimAndEmptyIsNull(
                        addressDetails.getAsJsonPrimitive("State").toString().replaceAll("^\"|\"$", ""));

                String oldState = userProfile.getAddress().getState();

                address.setState(addressDetails.getAsJsonPrimitive("State").toString().replaceAll("^\"|\"$", ""));

                boolean isChangeInProfileState = compareOldAndNewProfile(newState, oldState);

                if (isChangeInProfileState) {
                    OldAddress.setState(oldState);

                }
            }

            // if (addressDetails.get("Pin") != null) {
            // address.setPincode(addressDetails.getAsJsonPrimitive("Pin").toString().replaceAll("^\"|\"$",
            // ""));
            // }

            if (addressDetails.get("Pin") != null) {
                String newPin = StringUtility.trimAndEmptyIsNull(
                        addressDetails.getAsJsonPrimitive("Pin").toString().replaceAll("^\"|\"$", ""));

                String oldPin = userProfile.getAddress().getPincode();

                address.setPincode(addressDetails.getAsJsonPrimitive("Pin").toString().replaceAll("^\"|\"$", ""));

                boolean isChangeInProfilePin = compareOldAndNewProfile(newPin, oldPin);

                if (isChangeInProfilePin) {
                    OldAddress.setPincode(oldPin);
                }
            }
            if (OldAddress != null) {
                changesTrackingProfile.setAddress(address);
                changesTrackingProfile.setOldaddress(OldAddress);
            }
            if (address != null) {
                userProfile.setAddress(address);
            }
        }

    }

    public void addContact(ContactInformation contact) {
        if (contact == null || contact.getContactType() == null) {
            return;
        }

        if (m_contactInfo == null) {
            m_contactInfo = new HashMap<ContactInformation.ContactType, ContactInformation>();
        }

        m_contactInfo.put(contact.getContactType(), contact);
    }

    public void setId(int id) {
        m_id = id;
    }

    /**
     * @return the contactSerial
     */
    public String getContactSerial() {
        return m_contactSerial;
    }

    /**
     * @param contactSerial the contactSerial to set
     */
    public void setContactSerial(String contactSerial) {
        m_contactSerial = contactSerial;
    }

    public String getDesignation() {
        return m_designation;
    }

    public void setDesignation(String designation) {
        m_designation = designation;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof UserProfile)) {
            return false;
        }

        UserProfile profile2 = (UserProfile) obj;

        return getId() == profile2.getId();
    }

    @Override
    public int hashCode() {
        return 31 * m_id;
    }

    /**
     * @return the pictureKey
     */
    public String getPictureKey() {
        return m_pictureKey;
    }

    /**
     * @param pictureKey the pictureKey to set
     */
    public void setPictureKey(String pictureKey) {
        m_pictureKey = pictureKey;
    }

    public void addDisplayPictureURL(JsonObject responseObject, String completeURL) {
        responseObject.addProperty("profilePic", completeURL);
    }

    public boolean compareOldAndNewProfile(String newData, String oldData) {
        if ((oldData != null && newData != null)) {
            if (!oldData.equals(newData)) {
                return true;
            }
        } else {
            if (oldData == null && newData != null) {
                return true;
            }
        }
        return false;
    }

    public boolean compareOldAndNewProfile(Gender newData, Gender oldData) {
        if ((oldData != null && newData != null)) {
            if (!oldData.equals(newData)) {
                return true;
            }
        } else {
            if (oldData == null && newData != null) {
                return true;
            }
        }
        return false;
    }

    public Map<ContactType, ContactInformation> getContactInfo() {
        if(m_contactInfo != null) {
            return m_contactInfo;
        }
        
        return new HashMap<ContactType, ContactInformation>();
    }

}
