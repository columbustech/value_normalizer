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
    public static String CDRIVE_DOWNLOAD_URL="https://api.cdrive.columbusecosystem.com/download/?file_name=";
    public static String CDRIVE_UPLOAD_URL="https://api.cdrive.columbusecosystem.com/upload/";

    /* Start Development Variables
    * The variables defined below are only used for development
    * They might change during production or need to be requested via an API
    */
    public static String CLIENT_ID = "cMKGvPgMZCWiOrjEa3rLdz5CV0gYtFi6tFpr1IbM";
    public static String CLIENT_SECRET = "Nq9ubQ2KfJ5FYPZcX8OAqol0CHj8XGnO2pEBymVxfS7vv2rmwwYQngootYTuDieF6pupD5F00wz0aDu0ALvBYQSxnmUabjeqxiVrOCPRaB100EWSKkZ3m0nVFGI95tiT";
    public static String REDIRECT_URI = "http://node0.cloudproject.cs744-s19-pg0.wisc.cloudlab.us:4200/value_normalizer";
    public static String AUTH_URL = "https://authentication.columbusecosystem.com/o/token/";
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
        System.out.println(WINDOWS_LOCATION);
        System.out.println(LINUX_LOCATION);
        System.out.println(RESOURCE_LOCATION);
    }
}
