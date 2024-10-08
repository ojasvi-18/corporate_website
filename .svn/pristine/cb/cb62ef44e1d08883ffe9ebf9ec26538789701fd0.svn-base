package com.zillious.corporate_website.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.net.util.Base64;
import org.apache.log4j.Logger;

import com.zillious.corporate_website.data.ContactDTO;
import com.zillious.corporate_website.portal.entity.EmailMessage;
import com.zillious.corporate_website.ui.navigation.WebsiteActions;
import com.zillious.corporate_website.ui.navigation.WebsiteNavigation;
import com.zillious.corporate_website.ui.resources.QueryTypes;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperResponse;

/**
 * Class to send email alerts using settings defined in ConfigStore.
 */
public class EmailSender {
    private static final Logger s_logger               = Logger.getLogger(EmailSender.class);
    private static EmailSender  s_staticInstance       = new EmailSender();
    private static final long   MAIL_TIMEOUT           = (1000 * 60 * 5);
    private static final String STR_DEFAULT_SMTP_TO    = "DEFAULT_SMTP_TO";
    private static final String STR_DEFAULT_SMTP_FROM  = "DEFAULT_SMTP_FROM";
    private static final String STR_DEFAULT_SMTP_CC    = "DEFAULT_SMTP_CC";
    private static final String STR_ATTENDANCE_SMTP_TO = "ATTENDANCE_SMTP_TO";
    private static final String EMAIL_SMTP_HOST        = "EMAIL_SMTP_HOST";
    private static final String EMAIL_SMTP_PORT        = "EMAIL_SMTP_PORT";
    private static final String EMAIL_SMTP_USERNAME    = "EMAIL_SMTP_USERNAME";
    private static final String EMAIL_SMTP_PASSWORD    = "EMAIL_SMTP_PASSWORD";
    private static final String STR_ATTENDANCE_SMTP_CC = "ATTENDANCE_SMTP_CC";

    public void prepareContactInquiryMailAndSend(QueryTypes type, String emailId, String name, String phone,
            String contactMessage, String exceptionTrace, ZilliousSecurityWrapperRequest request,
            ZilliousSecurityWrapperResponse response) {

        // boolean isSuccess = ((type != null) && (emailId != null) && (name !=
        // null) && (phone != null) && (contactMessage !=null));
        boolean isSuccess = (exceptionTrace == null);
        StringBuilder textContent = new StringBuilder();
        String subject = null;

        String to = ConfigStore.getStringValue(STR_DEFAULT_SMTP_TO, "");

        String cc = ConfigStore.getStringValue(STR_DEFAULT_SMTP_CC, "");

        if (isSuccess) {
            subject = "New Inquiry Request";
            textContent.append("Hi").append("\n");
            textContent.append("New contact request received:\n");
            textContent.append("QueryType:\t").append(type.getDef()).append("\n");
            textContent.append("Name:\t").append(name).append("\n");
            textContent.append("Email:\t").append(emailId).append("\n");
            textContent.append("Phone Number:\t").append(phone).append("\n");
            textContent.append("Message:\t").append(contactMessage).append("\n");
            textContent.append("Please revert back to it at the earliest");
            String token = type.getQueryId() + "$" + emailId;
            String encodeBase64String = Base64.encodeBase64String(token.getBytes());
            textContent.append("\n\nIn order to mark this contact request \"Read\" please go to ")
                    .append(WebsiteActions.CONTACT.getActionURL(request, response,
                            new String[] { "ack", encodeBase64String }, true))
                    .append("\n");
        } else {
            subject = "Failed - New Inquiry Request";
            textContent.append("Hi").append("\n");
            textContent.append("There has been an error while submitting the contact request:\n");
            textContent.append("Trace:").append(exceptionTrace).append("\n");
            if (type != null) {
                textContent.append("QueryType:\t").append(type.getDef()).append("\n");
            }
            if (name != null) {
                textContent.append("Name:\t").append(name).append("\n");
            }
            if (emailId != null) {
                textContent.append("Email:\t").append(emailId).append("\n");
            }
            if (phone != null) {
                textContent.append("Phone Number:\t").append(phone).append("\n");
            }
            if (contactMessage != null) {
                textContent.append("Message:\t").append(contactMessage).append("\n");
            }
            textContent.append("Please look into it at the earliest");
        }
        try {
            sendEmail(to, cc, subject, textContent.toString());
        } catch (MessagingException e) {
            s_logger.error("Error in sending mail to : " + to + " and " + cc, e);
        }
    }

    /**
     * Sends email with specified html and text bodies. If html body is null,
     * then text only email is sent. Otherwise multiple mime types are used. To
     * and CC addresses can be multiple email addresses separated by comma or
     * semi-colon.
     * 
     * @param to
     * @param cc
     * @param subject
     * @param htmlContent
     * @param textContent
     * @throws MessagingException
     */
    public void sendEmail(String to, String cc, String subject, String textContent) throws MessagingException {
        try {

            if (s_logger.isDebugEnabled()) {
                s_logger.debug("Sending email with t-" + to + " c-" + cc + " s-" + subject);
            }

            String from = ConfigStore.getStringValue(STR_DEFAULT_SMTP_FROM, "info@zillious.com");

            InternetAddress fromAddress = new InternetAddress(from);
            InternetAddress[] toAddresses;
            try {
                toAddresses = convertToAddresses(to);
            } catch (Exception e) {
                s_logger.error("Error in fetching \"to\" addresses", e);
                toAddresses = null;
            }

            InternetAddress[] ccAddresses;
            try {
                ccAddresses = convertToAddresses(cc);
            } catch (Exception e) {
                s_logger.error("Error in fetching \"cc\" addresses", e);
                ccAddresses = null;
            }

            if (toAddresses == null && ccAddresses == null) {
                throw new RuntimeException("No Valid Recipient");
            }

            Message mail = new MimeMessage(getMailSession());
            mail.setFrom(fromAddress);
            mail.addRecipients(Message.RecipientType.TO, toAddresses);
            if (ccAddresses != null) {
                mail.addRecipients(Message.RecipientType.CC, ccAddresses);
            }

            if (subject != null) {
                mail.setSubject(subject);
            }
            mail.setContent(textContent, "text/plain");

            Transport.send(mail);

        } catch (Exception e) {
            s_logger.error("Error in sending mail", e);
        }
    }

    public static EmailSender getInstance() {
        return s_staticInstance;
    }

    private static InternetAddress[] convertToAddresses(String emailsStr) {
        if (emailsStr == null || emailsStr.length() < 1) {
            return null;
        }
        String[] emails = emailsStr.split("[;,]", -1);
        List<InternetAddress> addresses = new ArrayList<InternetAddress>(emails.length);
        for (int i = 0; i < emails.length; i++) {
            InternetAddress add = null;
            try {
                add = new InternetAddress(emails[i]);
            } catch (Exception e) {
                s_logger.error("Invalid address : " + emails[i], e);
            }
            addresses.add(add);
        }
        if (addresses.size() < 1) {
            return null;
        }
        return addresses.toArray(new InternetAddress[addresses.size()]);
    }

    private static InternetAddress[] convertToAddresses(Collection<String> emailsCol, String from) {
        if (emailsCol == null || emailsCol.isEmpty()) {
            return null;
        }

        emailsCol.remove(from);

        List<InternetAddress> addresses = new ArrayList<InternetAddress>(emailsCol.size());

        Iterator<String> iter = emailsCol.iterator();
        while (iter.hasNext()) {
            InternetAddress add = null;
            String emailId = null;
            try {
                emailId = iter.next();
                add = new InternetAddress(emailId);
            } catch (Exception e) {
                s_logger.error("Invalid address : " + emailId, e);
            }
            addresses.add(add);
        }
        if (addresses.size() < 1) {
            return null;
        }
        return addresses.toArray(new InternetAddress[addresses.size()]);
    }

    private static Session getMailSession() {
        String host = ConfigStore.getStringValue(EMAIL_SMTP_HOST, "localhost");
        String port = ConfigStore.getStringValue(EMAIL_SMTP_PORT, "25");
        String username = ConfigStore.getStringValue(EMAIL_SMTP_USERNAME, null);
        String password = ConfigStore.getStringValue(EMAIL_SMTP_PASSWORD, null);

        Properties prop = new Properties();
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.connectiontimeout", MAIL_TIMEOUT);
        prop.put("mail.smtp.timeout", MAIL_TIMEOUT);
        prop.put("mail.smtp.sendpartial", "true");

        Session mailSession = Session.getInstance(prop,
                (username != null || password != null) ? new Authenticator(username, password) : null);
        mailSession.setDebug(false);
        return mailSession;
    }

    private static class Authenticator extends javax.mail.Authenticator {
        private String m_smtpUsername, m_smtpPassword;

        public Authenticator(String user, String password) {
            m_smtpUsername = user;
            m_smtpPassword = password;
        }

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(m_smtpUsername, m_smtpPassword);
        }
    }

    private EmailSender() {
    }

    public void sendNewsletterSubscriptionAlert(String emailId, String ipAddress, boolean subscriptionSuccessful) {
        StringBuilder textContent = new StringBuilder();

        String to = ConfigStore.getStringValue(STR_DEFAULT_SMTP_TO, "");

        // Everyone else.
        String cc = ConfigStore.getStringValue(STR_DEFAULT_SMTP_CC, "");

        String subject = "Newsletter Subscription Alert - " + emailId + " - "
                + (subscriptionSuccessful ? "Successful" : "Unsuccessful");
        textContent.append("Hi").append("\r\n");
        textContent.append(emailId).append(" from ip address: " + ipAddress);

        if (subscriptionSuccessful) {
            textContent.append(" has successfully subscribed ");
        } else {
            textContent.append(" was unable to subscribe ");
        }
        textContent.append("to the Zillious Newsletter");

        try {
            sendEmail(to, cc, subject, textContent.toString());
        } catch (MessagingException e) {
            s_logger.error("Error in sending the mail to : " + to + " and " + cc, e);
        }
    }

    public void sendErrorMail(String subject, String trace, String extraInfo) {
        StringBuilder textContent = new StringBuilder();

        String to = ConfigStore.getStringValue(STR_DEFAULT_SMTP_TO, "");

        // Everyone else.
        String cc = ConfigStore.getStringValue(STR_DEFAULT_SMTP_CC, "");

        textContent.append("Hi, There has been an error. Please check the stack trace\n").append(trace);
        if (extraInfo != null) {
            textContent.append("\n").append(extraInfo);
        }

        try {
            sendEmail(to, cc, subject, textContent.toString());
        } catch (MessagingException e) {
            s_logger.error("Error in sending the mail to : " + to + " and " + cc, e);
        }
    }

    public void prepareDigestContactInquiryMailAndSend(List<ContactDTO> inquiryDTOs, String serverName) {
        StringBuilder textContent = new StringBuilder();
        String subject = null;

        String to = ConfigStore.getStringValue(STR_DEFAULT_SMTP_TO, "");

        String cc = ConfigStore.getStringValue(STR_DEFAULT_SMTP_CC, "");

        subject = "New Inquiry Requests - Digest Email";
        textContent.append("Hi").append("\n");
        textContent.append("Following New contact requests have been received:\n");
        StringBuilder ackURLPrepend = new StringBuilder(128);
        ackURLPrepend.append(serverName).append(WebsiteNavigation.getServletPath());

        ackURLPrepend.append("/").append(WebsiteActions.CONTACT.getDisplayName());

        for (int i = 0; i < inquiryDTOs.size(); i++) {
            textContent.append("Enquiry Number: ").append(i + 1).append(":\n");
            ContactDTO dto = inquiryDTOs.get(i);
            textContent.append("QueryType:\t").append(dto.getType().getDef()).append("\n");
            textContent.append("Name:\t").append(dto.getName()).append("\n");
            textContent.append("Email:\t").append(dto.getEmail()).append("\n");
            textContent.append("Phone Number:\t").append(dto.getPhone()).append("\n");
            textContent.append("Message:\t").append(dto.getMessage()).append("\n");
            textContent.append("Please revert back to it at the earliest");
            String token = dto.getType().getQueryId() + "$" + dto.getEmail();
            String encodeBase64String = Base64.encodeBase64String(token.getBytes());
            textContent.append("\n\nIn order to mark this contact request \"Read\" please go to ")
                    .append(ackURLPrepend.toString() + "/ack/" + encodeBase64String).append("\n");
        }
        try {
            sendEmail(to, cc, subject, textContent.toString());
        } catch (MessagingException e) {
            s_logger.error("Error in sending mail to : " + to + " and " + cc, e);
        }
    }

    public void prepareDigestNewsletterMailAndSend(List<ContactDTO> subscriptionDTOS) {

        StringBuilder textContent = new StringBuilder();

        String to = ConfigStore.getStringValue(STR_DEFAULT_SMTP_TO, "");

        // Everyone else.
        String cc = ConfigStore.getStringValue(STR_DEFAULT_SMTP_CC, "");

        String subject = "Newsletter Subscription Alerts - Digest Mail";
        textContent.append("Hi").append("\r\n");
        textContent.append("Following Newsletter Subscription Requests have been received from: "
                + DateUtility.getPreviousDaysDate(10, 0, 0) + " uptil now\n");
        for (int i = 0; i < subscriptionDTOS.size(); i++) {
            ContactDTO contactDTO = subscriptionDTOS.get(i);
            textContent.append(i + 1).append(": ").append(contactDTO.getEmail()).append(" from ip address: ")
                    .append(contactDTO.getClientIP()).append("\n");
        }

        try {
            sendEmail(to, cc, subject, textContent.toString());
        } catch (MessagingException e) {
            s_logger.error("Error in sending the mail to : " + to + " and " + cc, e);
        }

    }

    public void preparePopupRequestMailAndSend(String content, String exceptionTrace,
            ZilliousSecurityWrapperRequest request, ZilliousSecurityWrapperResponse response) {
        boolean isSuccess = (exceptionTrace == null);
        StringBuilder textContent = new StringBuilder();
        String subject = null;

        String to = ConfigStore.getStringValue(STR_DEFAULT_SMTP_TO, "");

        String cc = ConfigStore.getStringValue(STR_DEFAULT_SMTP_CC, "");

        if (isSuccess) {
            subject = "New Popup Request";
            textContent.append("Hi").append("\n");
            textContent.append("New Popup content request received:\n");
            textContent.append("content:\n").append(content).append("\n");
            textContent.append("Please revert back to it at the earliest");
            textContent.append("\n\nIn order to mark this request \"okay\" please go to ")
                    .append(WebsiteActions.SETUP_INDEX_PAGE_POPUP.getActionURL(request, response,
                            new String[] { "ack", "true" }, true))
                    .append("\n");
            textContent.append("\n\nIn order to mark this request as inactive")
                    .append(WebsiteActions.SETUP_INDEX_PAGE_POPUP.getActionURL(request, response,
                            new String[] { "ack", "false" }, true))
                    .append("\n");
        } else {
            subject = "Failed - New Inquiry Request";
            textContent.append("Hi").append("\n");
            textContent.append("There has been an error while adding the popup request:\n");
            textContent.append("Trace:").append(exceptionTrace).append("\n");
            textContent.append("Please look into it at the earliest");
        }
        try {
            sendEmail(to, cc, subject, textContent.toString());
        } catch (MessagingException e) {
            s_logger.error("Error in sending mail to : " + to + " and " + cc, e);
        }
    }

    public static void sendEmail(EmailMessage emailMessage) throws MessagingException {
        try {

            String from = emailMessage.getFrom();
            if (from == null) {
                from = ConfigStore.getStringValue(STR_DEFAULT_SMTP_FROM, "info@zillious.com");
            }

            InternetAddress fromAddress = new InternetAddress(from);

            InternetAddress[] toAddresses = convertToAddresses(emailMessage.getTo(), from);
            InternetAddress[] ccAddresses = convertToAddresses(emailMessage.getCc(), from);

            Message mail = new MimeMessage(getMailSession());
            mail.setFrom(fromAddress);
            mail.setReplyTo(new InternetAddress[] { fromAddress });
            mail.addRecipients(Message.RecipientType.TO, toAddresses);
            if (ccAddresses != null) {
                mail.addRecipients(Message.RecipientType.CC, ccAddresses);
            }

            // sending a copy to sender
            mail.addRecipient(Message.RecipientType.CC, fromAddress);

            String subject = emailMessage.getSubject();
            if (subject != null) {
                mail.setSubject(subject);
            }

            String textContent = emailMessage.getContent();

            String contentType = emailMessage.getContentType();

            if (contentType == null) {
                contentType = "text/plain";
            }

            mail.setContent(textContent, contentType);

            Transport.send(mail);

        } catch (Exception e) {
            s_logger.error("Error in sending mail", e);
        }

    }

    public void sendDeviceUsersNotPresent(Set<String> notPresent) {
        StringBuilder textContent = new StringBuilder();
        String subject = null;

        String to = ConfigStore.getStringValue(STR_ATTENDANCE_SMTP_TO, "");

        String cc = ConfigStore.getStringValue(STR_ATTENDANCE_SMTP_CC, "");

        subject = "No User Info for Device IDs";

        textContent.append("Hi").append("\n");
        textContent.append(
                "We recieved attendance records for users who do not have an entry in the system. Below are the device id's of such users:");
        for (String id : notPresent) {
            textContent.append(id).append("\r\n");
        }

        textContent.append("Please take appropriate actions for the same.");

        try {
            sendEmail(to, cc, subject, textContent.toString());
        } catch (MessagingException e) {
            s_logger.error("Error in sending mail to : " + to + " and " + cc, e);
        }
    }
}
