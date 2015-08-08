package com.onemenu.server.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

/**
 * 
 * @author lin
 * 
 */
public class AwsS3Utils {

	private static final Logger LOGGER = Logger.getLogger(AwsS3Utils.class);

	private static final String S3_HOST_NAME = "https://s3-us-west-2.amazonaws.com"; // onemenu-s3-server

	// private static final String ONE_MENU_BUCKET_NAME = "onemenu-aws-s3";
	private static final String ONE_MENU_BUCKET_NAME = "s3-onemenu";
	// 根目标
	private static final String ROOT_RES = "resources";
	// 用户资源
	private static final String CUSTOMER_RES = "customer-res";
	// 餐馆资源
	private static final String RESTAURANT_RES = "restaurant-res";
	// 订单相关资源
	private static final String ORDER_RES = "order-res";

	private static final String propertiesFileName = "/AwsCredentials.properties";
	private static String PROFILE_NAME = PropertiesUtils.getProperties("profileName", propertiesFileName);
	private static String ACCESS_KEY = PropertiesUtils.getProperties("accessKey", propertiesFileName);
	private static String SECRET_KEY = PropertiesUtils.getProperties("secretKey", propertiesFileName);

	public AwsS3Utils() throws FileNotFoundException, IOException {
	}

	public static String getAwsS3ImageUrl(String awsS3url, String folderName, String imageName) {

		return awsS3url + folderName + "/" + imageName;
	}

	public static String getAwsS3RootResUrl() {

		return S3_HOST_NAME + "/" + ONE_MENU_BUCKET_NAME + "/" + ROOT_RES + "/";
	}

	public static String getAwsS3RestaurantResUrl() {

		return getAwsS3RootResUrl() + RESTAURANT_RES + "/";
	}

	public static String getAwsS3CustomerResUrl() {

		return getAwsS3RootResUrl() + CUSTOMER_RES + "/";
	}

	public static String getAwsS3OrderResUrl() {

		return getAwsS3RootResUrl() + ORDER_RES + "/";
	}

	public static String getAwsS3RootResPath() {

		return ONE_MENU_BUCKET_NAME + File.separator + ROOT_RES + File.separator;
	}

	public static String getAwsS3RestaurantResUploadPath() {

		return ROOT_RES + File.separator + RESTAURANT_RES + File.separator;
	}

	public static String getAwsS3CustomerResUploadPath() {
		return ROOT_RES + File.separator + CUSTOMER_RES + File.separator;
	}

	public static String getAwsS3OrderResUploadPath() {
		return ROOT_RES + File.separator + ORDER_RES + File.separator;
	}

	public static void uploadFileToAwsS3RestaurantRes(String folderName, String fileName, InputStream in, ObjectMetadata metaddata) {

		String filePath = getAwsS3RestaurantResUploadPath() + folderName + File.separator + fileName;
		uploadFileToS3(filePath, in, metaddata);
	}

	public static void uploadFileToAwsS3CustomerRes(String folderName, String fileName, InputStream in, ObjectMetadata metaddata) {
		String filePath = getAwsS3CustomerResUploadPath() + folderName + File.separator + fileName;
		uploadFileToS3(filePath, in, metaddata);
	}

	public static void uploadFileToAwsS3OrderRes(String folderName, String fileName, InputStream in, ObjectMetadata metaddata) {
		String filePath = getAwsS3OrderResUploadPath() + folderName + File.separator + fileName;
		uploadFileToS3(filePath, in, metaddata);
	}

	public static String getMineType(String base64Str) {

		String mimetype = base64Str.substring(base64Str.indexOf(":") + 1, base64Str.indexOf(";"));
		return mimetype;
	}

	public static String generateImageName(String width, String height, String extension) {

		String imageName = System.currentTimeMillis() + "_" + width + "_" + height + "." + extension;
		return imageName;
	}

	public static void uploadFileToS3(String filePath, InputStream in, ObjectMetadata metaddata) {

		AmazonS3 s3client = new AmazonS3Client(new BasicAWSCredentials(ACCESS_KEY, SECRET_KEY));
		if (LOGGER.isDebugEnabled())
			LOGGER.debug(PROFILE_NAME);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug(ACCESS_KEY);
		if (LOGGER.isDebugEnabled())
			LOGGER.debug(SECRET_KEY);

		try {

			s3client.putObject(ONE_MENU_BUCKET_NAME, filePath, in, metaddata);
		} catch (AmazonServiceException ase) {
			LOGGER.error("Caught an AmazonServiceException, which " + "means your request made it " + "to Amazon S3, but was rejected with an error response" + " for some reason.");
			LOGGER.error("Error Message:    " + ase.getMessage());
			LOGGER.error("HTTP Status Code: " + ase.getStatusCode());
			LOGGER.error("AWS Error Code:   " + ase.getErrorCode());
			LOGGER.error("Error Type:       " + ase.getErrorType());
			LOGGER.error("Request ID:       " + ase.getRequestId());
			LOGGER.error("", ase);
		} catch (AmazonClientException ace) {
			LOGGER.error("Caught an AmazonClientException, which " + "means the client encountered " + "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			LOGGER.error("Error Message: " + ace.getMessage());
			LOGGER.error("", ace);
		}
	}

	// Upload file
	public static void uploadFileToS3(String bucketName, String keyName, File file) {

		AmazonS3 s3client = new AmazonS3Client(new ProfileCredentialsProvider(PROFILE_NAME));
		try {
			s3client.putObject(new PutObjectRequest(bucketName, keyName, file));
		} catch (AmazonServiceException ase) {
			LOGGER.error("Caught an AmazonServiceException, which " + "means your request made it " + "to Amazon S3, but was rejected with an error response" + " for some reason.");
			LOGGER.error("Error Message:    " + ase.getMessage());
			LOGGER.error("HTTP Status Code: " + ase.getStatusCode());
			LOGGER.error("AWS Error Code:   " + ase.getErrorCode());
			LOGGER.error("Error Type:       " + ase.getErrorType());
			LOGGER.error("Request ID:       " + ase.getRequestId());
			LOGGER.error("", ase);
		} catch (AmazonClientException ace) {
			LOGGER.error("Caught an AmazonClientException, which " + "means the client encountered " + "an internal error while trying to " + "communicate with S3, "
					+ "such as not being able to access the network.");
			LOGGER.error("Error Message: " + ace.getMessage());
			LOGGER.error("", ace);
		}
	}

}
