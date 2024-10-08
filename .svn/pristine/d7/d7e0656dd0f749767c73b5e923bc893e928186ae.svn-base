package com.zillious.corporate_website.portal.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.MutableMutabilityPlan;
import org.springframework.util.StringUtils;

import com.zillious.corporate_website.utils.StringUtility;

public class CommaDelimitedStringsJavaTypeDescriptor extends AbstractTypeDescriptor<List> {
    private static final long  serialVersionUID = -2163705389865670489L;
    public static final String DELIMITER        = ",";

    public CommaDelimitedStringsJavaTypeDescriptor() {
        super(List.class, new MutableMutabilityPlan<List>() {
            private static final long serialVersionUID = 4603742245100642394L;

            @Override
            protected List deepCopyNotNull(List value) {
                return new ArrayList(value);
            }
        });
    }

    @Override
    public String toString(List contactList) {
        if (contactList == null) {
            return null;
        }

        int delimLevel = 0;
        String delim = StringUtility.getDelim(delimLevel);
        String serializedString = StringUtils.collectionToDelimitedString(contactList, delim);
        return serializedString;
    }

    @Override
    public List fromString(String m_contactSerial) {
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

    @Override
    public <X> X unwrap(List value, Class<X> type, WrapperOptions options) {
        return (X) toString(value);
    }

    @Override
    public <X> List wrap(X value, WrapperOptions options) {
        return fromString((String) value);
    }
}