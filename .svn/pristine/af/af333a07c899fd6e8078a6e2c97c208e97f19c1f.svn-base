/**
 * 
 */
package com.zillious.corporate_website.portal.entity.utility;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.util.StringUtils;

import com.zillious.corporate_website.utils.StringUtility;

/**
 * @author satyam.mittal
 *
 */
@Converter
public class ListContactInfoConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> contactList) {
        if (contactList == null) {
            return null;
        }

        int delimLevel = 0;
        String delim = StringUtility.getDelim(delimLevel);
        String serializedString = StringUtils.collectionToDelimitedString(contactList, delim);
        return serializedString;
    }

    @Override
    public List<String> convertToEntityAttribute(String m_contactSerial) {
        m_contactSerial = StringUtility.trimAndEmptyIsNull(m_contactSerial);

        String delimRegex = StringUtility.getDelimRegex(0);
        List<String> m_contactlist = null;
        if (m_contactSerial != null) {
            String[] token = m_contactSerial.split(delimRegex);
            if (token.length > 0) {
                m_contactlist = new ArrayList<String>();
                for (String contcatSerial : token) {
                    // m_contactlist.add(new ContactInformation(contcatSerial,
                    // 1));
                    m_contactlist.add(contcatSerial);
                }
            }
        }
        return m_contactlist;
    }

}
