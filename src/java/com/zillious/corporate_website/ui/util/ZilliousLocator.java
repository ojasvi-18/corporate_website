package com.zillious.corporate_website.ui.util;

import java.io.File;

import com.maxmind.geoip.LookupService;
import com.zillious.corporate_website.app.WebsiteApplication;
import com.zillious.corporate_website.i18n.Country;
import com.zillious.corporate_website.logger.Logger;
import com.zillious.corporate_website.utils.ConfigStore;

/**
 * @author Nishant
 * 
 */
public class ZilliousLocator {

    private static File   s_file;
    private static Logger s_logger = Logger.getInstance(ZilliousLocator.class);

    public static Country getCountry(String ipAddress) {
        if (s_file == null) {
            s_file = new File(ConfigStore.getStringValue("geoip.fileloc", ""));
        }
        return getLocation(ipAddress);
    }

    private static Country getLocation(String ipAddress) {
        Country country = null;
        s_logger.info("IPAddress: " + ipAddress + ", data file location: " + s_file.getAbsolutePath());
        if ("127.0.0.1".equals(ipAddress)) {
            return Country.INDIA;
        }
        try {
            LookupService service = new LookupService(s_file, LookupService.GEOIP_MEMORY_CACHE);
            country = Country.getCountryFromCode(service.getCountry(ipAddress).getCode());
        } catch (Exception e) {
            s_logger.error("Error while matching the ipAddress With the country code", e);
            country = Country.INDIA;
        }

        return country;

    }

    public static void main(String[] args) {
        WebsiteApplication.initialize(null);
        System.out.println(getCountry("31.13.110.101"));
    }

}
