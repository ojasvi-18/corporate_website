package com.zillious.corporate_website.portal.ui.dto;

import org.springframework.web.multipart.MultipartFile;

/**
 * This DTO class is used to help get info from the ui that will have the user
 * id for which the profile picture is to be uploaded and the actual file
 * content
 * 
 * @author nishant.gupta
 *
 */
public class FormDataWithFile {
    private int           user_id;
    private MultipartFile file;

    /**
     * @return the user_id
     */
    public int getUser_id() {
        return user_id;
    }

    /**
     * @param user_id the user_id to set
     */
    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    /**
     * @return the file
     */
    public MultipartFile getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(MultipartFile file) {
        this.file = file;
    }

}
