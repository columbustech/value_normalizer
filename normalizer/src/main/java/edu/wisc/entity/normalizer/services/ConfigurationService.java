package edu.wisc.entity.normalizer.services;

import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class ConfigurationService {

    private static String WINDOWS_LOCATION="src/main/resources/csv/";
    private static String LINUX_LOCATION="/normalizer/csv/";
    public static String RESOURCE_LOCATION;
    public static String CDRIVE_DOWNLOAD_URL="https://api.cdrive.columbusecosystem.com/download/?path=";
    public static String CDRIVE_UPLOAD_URL="https://api.cdrive.columbusecosystem.com/upload/";

    /* Start Development Variables
    * The variables defined below are only used for development
    * They might change during production or need to be requested via an API
    */
    public static String CLIENT_ID;
    public static String CLIENT_SECRET;

    //ToDo: Decide redirect URI. 
    public static String REDIRECT_URI;
    public static String AUTH_URL = "https://authentication.columbusecosystem.com/o/token/";
    public static String APP_NAME = "value-normalizer";
    /* End Development Variables
    */
    public ConfigurationService() {
        if (SystemUtils.IS_OS_LINUX) {
            RESOURCE_LOCATION = LINUX_LOCATION;
        } else if (SystemUtils.IS_OS_WINDOWS) {
            RESOURCE_LOCATION = WINDOWS_LOCATION;
        } else {
            RESOURCE_LOCATION = LINUX_LOCATION;
        }
	String host_name = System.getenv("CDRIVE_URL");
	String user_name = System.getenv("COLUMBUS_USERNAME");
	REDIRECT_URI = host_name + "app/"+ user_name + "/" + APP_NAME +"/upload";
	//REDIRECT_URI = "http://node0.cloudproject.cs744-s19-pg0.wisc.cloudlab.us:8000/upload";
	System.out.println(REDIRECT_URI);
        //CLIENT_ID = "cMKGvPgMZCWiOrjEa3rLdz5CV0gYtFi6tFpr1IbM";
	//CLIENT_SECRET = "Nq9ubQ2KfJ5FYPZcX8OAqol0CHj8XGnO2pEBymVxfS7vv2rmwwYQngootYTuDieF6pupD5F00wz0aDu0ALvBYQSxnmUabjeqxiVrOCPRaB100EWSKkZ3m0nVFGI95tiT";
	CLIENT_ID = System.getenv("COLUMBUS_CLIENT_ID");
        CLIENT_SECRET = System.getenv("COLUMBUS_CLIENT_SECRET");
        System.out.println(CLIENT_ID);
	System.out.println(CLIENT_SECRET);
	System.out.println(WINDOWS_LOCATION);
        System.out.println(LINUX_LOCATION);
        System.out.println(RESOURCE_LOCATION);
    }
}
