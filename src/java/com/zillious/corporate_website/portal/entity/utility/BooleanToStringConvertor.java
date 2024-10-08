package com.zillious.corporate_website.portal.entity.utility;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author ojasvi.bhardwaj
 *
 */

@Converter
public class BooleanToStringConvertor implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean arg) {
        if(arg == null){
            return "N";
        }
        if(arg == true){
            return "Y";   
        } else {
            return "N";
        }
    }

    @Override
    public Boolean convertToEntityAttribute(String arg) {
        if(arg == null){
            return false;
        }
        if(arg.equals("Y")){
            return true;
        } else {
            return false;
        }
    }

}
