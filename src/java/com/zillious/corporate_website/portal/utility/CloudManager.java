package com.zillious.corporate_website.portal.utility;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.zillious.corporate_website.utils.ConfigStore;
import com.zillious.corporate_website.utils.StringUtility;

/**
 * @author nishant.gupta
 *
 */
public enum CloudManager {
    AMAZONS3 {

        @Override
        public String getCompleteDisplayURL(String objectKey) {
            String ZILLIOUS_S3_BUCKET = "ZILLIOUS_S3_BUCKET";
            String ZILLIOUS_S3_BASEURL = "ZILLIOUS_S3_BASEURL";
            String baseUrl = ConfigStore.getStringValue(ZILLIOUS_S3_BASEURL, null);
            String bucketName = ConfigStore.getStringValue(ZILLIOUS_S3_BUCKET, null);
            if (objectKey == null || baseUrl == null || bucketName == null) {
                return null;
            }
            return baseUrl + "/" + bucketName + "/" + objectKey;
        }

        @Override
        public boolean deleteFile(String objectKey) {
            String ZILLIOUS_S3_ACCESSKEY = "ZILLIOUS_S3_ACCESS_KEY";
            String ZILLIOUS_S3_SECRETKEY = "ZILLIOUS_S3_SECRETKEY";
            String ZILLIOUS_S3_BUCKET = "ZILLIOUS_S3_BUCKET";

            String accessKey = ConfigStore.getStringValue(ZILLIOUS_S3_ACCESSKEY, null);
            String secretKey = ConfigStore.getStringValue(ZILLIOUS_S3_SECRETKEY, null);
            String bucketName = ConfigStore.getStringValue(ZILLIOUS_S3_BUCKET, null);

            try {
                AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
                AmazonS3 client = new AmazonS3Client(credentials);
                client.deleteObject(new DeleteObjectRequest(bucketName, objectKey));
                return true;
            } catch (Exception e) {
                s_logger.error("Error while uploading the file to the bucket", e);
            }

            return false;
        }

        @Override
        public String uploadFileAndReturnKey(int userId, String objectKey, MultipartFile file) {
            String ZILLIOUS_S3_ACCESSKEY = "ZILLIOUS_S3_ACCESS_KEY";
            String ZILLIOUS_S3_SECRETKEY = "ZILLIOUS_S3_SECRETKEY";
            String ZILLIOUS_S3_BUCKET = "ZILLIOUS_S3_BUCKET";

            String accessKey = ConfigStore.getStringValue(ZILLIOUS_S3_ACCESSKEY, null);
            String secretKey = ConfigStore.getStringValue(ZILLIOUS_S3_SECRETKEY, null);
            String bucketName = ConfigStore.getStringValue(ZILLIOUS_S3_BUCKET, null);

            // new save
            if (objectKey == null) {
                objectKey = createKey(userId, 30);
            }

            try {
                AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
                AmazonS3 client = new AmazonS3Client(credentials);
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentType(file.getContentType());
                metadata.setContentLength(file.getSize());
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectKey, file.getInputStream(),
                        metadata);

                putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
                client.putObject(putObjectRequest);
                return objectKey;
            } catch (Exception e) {
                s_logger.error("Error while uploading the file to the bucket", e);
                // throw ace;
            }

            return null;
        }
    },
    ;

    private static Logger s_logger = Logger.getLogger(CloudManager.class);

    public String uploadFileAndReturnKey(int userId, String objectKey, MultipartFile file) {
        throw new RuntimeException("Code should not reach here");
    }

    /**
     * Total keylength is given. the user id will be sandwiched and an upper
     * limit of 4 characters will be reserved for the user id
     * 
     * @param userId
     * @param length
     * @return
     */
    protected String createKey(int userId, int length) {
        String stringifiedUserId = StringUtility.pad(String.valueOf(userId), "0", 4, true);
        if (length <= (2 * stringifiedUserId.length())) {
            length *= 2;
        }

        int remainingLength = length - stringifiedUserId.length();
        int count = remainingLength / 2;
        String firstHalf = RandomStringUtils.randomAlphanumeric(count);
        StringBuilder key = new StringBuilder(firstHalf);
        key.append(stringifiedUserId);
        String secondHalf = RandomStringUtils.randomAlphanumeric(remainingLength - count);
        key.append(secondHalf);
        return key.toString();
    }

    public boolean deleteFile(String objectKey) {
        throw new RuntimeException("Code should not reach here");
    }

    public static void main(String[] args) {
        System.out.println(CloudManager.AMAZONS3.createKey(1, 30));
    }

    public String getCompleteDisplayURL(String objectKey) {
        throw new RuntimeException("Code should not reach here");
    }
}
