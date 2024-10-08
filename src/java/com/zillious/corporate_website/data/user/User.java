package com.zillious.corporate_website.data.user;

import java.io.Serializable;
import java.security.MessageDigest;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.codec.binary.Base64;

import com.zillious.corporate_website.logger.Logger;
import com.zillious.corporate_website.portal.ui.UserRoles;
import com.zillious.corporate_website.portal.ui.model.UserProfile;
import com.zillious.corporate_website.utils.StringUtility;

public class User implements Serializable {
    private static final long serialVersionUID = -7104166664526567706L;

    private static Logger     s_logger         = Logger.getInstance(User.class);

    protected String          m_userId;
    protected boolean         m_isEnabled;
    protected String          m_authToken;
    protected UserRoles       m_UserRole;
    protected String          m_name;
    protected String          m_email;
    protected UserProfile     m_userprofile;
    /** The user id of the user in the device */
    private String            m_deviceUserID;

    /*
     * In Memory Variables
     */
    protected String          m_password;

    public User(ResultSet rs, boolean useColumnNumbers) throws SQLException {
        populateFromDataSet(rs, useColumnNumbers);
    }

    public User(int userId) {
        m_userId = "E_" + userId;
    }

    public User(int id, String role, String name, String email, String password, boolean isEnabled) {
        setUserId(id);
        setUserRole(UserRoles.deserialize(role.substring(0, 1)));
        setName(name);
        setEmail(email);
        setEnabled(isEnabled);
        setPassword(password);
        hashPassword();
    }

    public User(ResultSet result) throws Exception {
        this(result, false);
    }

    private void populateFromDataSet(ResultSet rs, boolean useColumnNumbers) throws SQLException {
        if (useColumnNumbers) {
            int i = 7;
            int employeeUserID = rs.getInt(i++);
            m_userId = employeeUserID > 0 ? "E_" + employeeUserID : null;
            m_isEnabled = "Y".equals(rs.getString(i++));
            m_authToken = rs.getString(i++);
            m_UserRole = UserRoles.deserialize(rs.getString(i++));
            m_name = rs.getString(i++);
            m_email = rs.getString(i++);
            m_deviceUserID = "D_" + rs.getInt(1);
        } else {
            m_userId = "E_" + rs.getInt("user_id");
            m_isEnabled = "Y".equals(rs.getString("is_enabled"));
            m_authToken = rs.getString("auth_token");
            m_UserRole = UserRoles.deserialize(rs.getString("role"));
            m_name = rs.getString("name");
            m_email = rs.getString("email");
        }

    }

    public String getUserId() {
        return m_userId;
    }

    public void setUserId(int userId) {
        m_userId = "E_" + userId;
    }

    public static String generateRandomCSRFToken(int len) {
        StringBuilder buf = new StringBuilder(len + 1);
        for (int i = 0; i < len; i++) {
            buf.append(StringUtility.getRandomAplanumeric());
        }
        return buf.toString();
    }

    /**
     * @return the isEnabled
     */
    public boolean isEnabled() {
        return m_isEnabled;
    }

    /**
     * @param isEnabled the isEnabled to set
     */
    public void setEnabled(boolean isEnabled) {
        m_isEnabled = isEnabled;
    }

    /**
     * @return the authToken
     */
    public String getAuthToken() {
        return m_authToken;
    }

    /**
     * @param authToken the authToken to set
     */
    public void setAuthToken(String authToken) {
        m_authToken = authToken;
    }

    /**
     * @return the userRole
     */
    public UserRoles getUserRole() {
        return m_UserRole;
    }

    /**
     * @param userRole the userRole to set
     */
    public void setUserRole(UserRoles userRole) {
        m_UserRole = userRole;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return m_email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        m_email = email;
    }

    private static String getPasswordHash(String userId, String password) {
        try {
            String dataString = "corp" + password + "zil" + userId;
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(dataString.getBytes("UTF-8"));
            byte raw[] = md.digest();
            return new String(Base64.encodeBase64(raw));

        } catch (Exception e) {
            s_logger.error("Error occurred while hashing password", e);
            return null;
        }
    }

    public static boolean authenthicateUser(String userId, String dbAuthToken, String givenPassword) {
        if (givenPassword == null || dbAuthToken == null) {
            return false;
        }
        String giveAuthToken = User.getPasswordHash(userId, givenPassword);
        return giveAuthToken.equals(dbAuthToken);
    }

    private static String getPasswordHash(int userId, String givenPassword) {
        return getPasswordHash("E_" + userId, givenPassword);
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

    public static void main(String[] args) {
        System.out.println(getPasswordHash(0, "flameon!"));
    }

    public void setPassword(String passwd) {
        if (passwd != null && passwd.length() > 20) {
            passwd = passwd.substring(0, 20);
        }
        m_password = passwd;
    }

    public void hashPassword() {
        m_authToken = getPasswordHash(m_userId, m_password);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof User)) {
            return false;
        }
        User secondDTO = (User) obj;
        if (this == secondDTO) {
            return true;
        }

        if (m_userId == null && secondDTO.getUserId() == null) {
            if (m_deviceUserID == null && secondDTO.getDeviceUserID() == null) {
                return true;
            }

            if (m_deviceUserID != null ^ secondDTO.getDeviceUserID() != null) {
                return false;
            }

            return m_deviceUserID.equals(secondDTO.getDeviceUserID());
        }

        return m_userId.equals(secondDTO.getUserId());
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        hashCode = 31 * hashCode + (m_userId != null ? (m_userId.hashCode()) : 0);
        hashCode = 31 * hashCode + (m_deviceUserID != null ? (m_deviceUserID.hashCode()) : 0);
        return hashCode;
    }

    /**
     * @return the deviceUserID
     */
    public String getDeviceUserID() {
        return m_deviceUserID;
    }

    public UserProfile getUserprofile() {
        return m_userprofile;
    }

    public void setUserprofile(UserProfile userprofile) {
        m_userprofile = userprofile;
    }

}
