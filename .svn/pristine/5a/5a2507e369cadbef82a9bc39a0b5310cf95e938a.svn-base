package com.zillious.corporate_website.ui.beans;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zillious.corporate_website.ui.security.ZilliousSecurityWrapperRequest;

/**
 * @author Nishant
 * 
 */
public class CaptchaBean {
    private static Logger s_logger  = Logger.getLogger(CaptchaBean.class);
    private static String secretKey = "6Lcl5BETAAAAAMsKUTK2ipnmq3tiEbb0aihqnbLb";

    public static boolean validateCaptcha(ZilliousSecurityWrapperRequest request, String ip) {
        String g_recaptcha_response = request.getParameter("g-recaptcha-response");
        s_logger.debug("Google recaptcha response captured : " + g_recaptcha_response);
        return confirmRecaptcha(g_recaptcha_response, ip);
    }

    private static boolean confirmRecaptcha(String gRecaptchaResponse, String ip) {
        String reqUrl = "https://www.google.com/recaptcha/api/siteverify";
        String resXML = null;
        BufferedReader inp = null;
        HttpsURLConnection urlCon = null;
        boolean isValid = false;
        try {
            URL url;
            if (s_logger.isDebugEnabled()) {
                s_logger.debug("Executing Recaptcha verification Request Url: " + reqUrl);
            }

            url = new URL(reqUrl);
            String postParams = "secret=" + secretKey + "&response=" + gRecaptchaResponse;
            urlCon = (HttpsURLConnection) url.openConnection();
            urlCon.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(urlCon.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = urlCon.getResponseCode();
            s_logger.debug("\nSending 'POST' request to URL : " + url);
            s_logger.debug("Post parameters : " + postParams);
            s_logger.info("Response Code : " + responseCode);

            inp = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = inp.readLine()) != null) {
                response.append(inputLine);
            }

            // print result
            s_logger.debug(response.toString());

            // parse JSON response and return 'success' value
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(response.toString()).getAsJsonObject();
            JsonElement jsonElement = object.get("success");
            if (jsonElement != null) {
                isValid = jsonElement.getAsBoolean();
            }

            //Logging the case when there is an invalid response.
            if (!isValid) {
                s_logger.info(response.toString());
            }
            return isValid;

        } catch (Exception e) {
            s_logger.error("Error in executing Captcha bean Request: " + reqUrl + " ErrorXML: " + resXML, e);
        } finally {
            try {
                if (inp != null) {
                    inp.close();
                }
            } catch (Exception e) {
                s_logger.error("Error in closing the stream ", e);
            }
        }
        return isValid;

    }

}
