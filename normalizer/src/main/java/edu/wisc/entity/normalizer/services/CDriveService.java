package edu.wisc.entity.normalizer.services;

import edu.wisc.entity.normalizer.model.CdriveDownload;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;


@Service
public class CDriveService {
    public String getDownloadLink(String file, String token) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String cdriveUrl = ConfigurationService.CDRIVE_DOWNLOAD_URL + file;
            System.out.println("Cdrive URL = " + cdriveUrl);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            HttpEntity<String> request = new HttpEntity<>(headers);
            ResponseEntity<CdriveDownload> response = restTemplate.exchange(cdriveUrl, HttpMethod.GET, request, CdriveDownload.class);
            CdriveDownload downloadLink = response.getBody();
            return downloadLink.getDownload_url();
        } catch (Exception e){
            return e.toString();
        }
    }

    public String uploadFile(String file, String token,String uploadUrl) {
        try {
            String cdriveUrl = ConfigurationService.CDRIVE_UPLOAD_URL;

            uploadUrl = uploadUrl.replace("\\", "/");
            String[] splittedFileName = uploadUrl.split("/");
            String simpleFileName ;
            if (splittedFileName.length > 0){
                simpleFileName = splittedFileName[splittedFileName.length-1];
                File src = new File(ConfigurationService.RESOURCE_LOCATION + file);
                File target = new File(ConfigurationService.RESOURCE_LOCATION+simpleFileName);

                Files.copy(src.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);

            } else{
                  simpleFileName = file;
            }
            
            FileSystemResource fsCopy = new FileSystemResource(ConfigurationService.RESOURCE_LOCATION + simpleFileName);
    
            LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer " + token);
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            String outputPath = "";
            for( int i = 0; i < splittedFileName.length-1; i++)
            {
                outputPath += splittedFileName[i];
                if (i < splittedFileName.length - 2){
                    outputPath += "/";
                }
            }

            map.add("file", fsCopy);

            map.add("path",outputPath);
            
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(cdriveUrl, request, String.class);

            System.out.println(response.toString());
            return response.toString();
        } catch (Exception e){
            return e.toString();
        }
    }

    public ResponseEntity<String> getLoginToken(String code) {
        ResponseEntity<String> response = null;
        try {
            String authUrl = ConfigurationService.AUTH_URL;
            LinkedMultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            //headers.add("Authorization", "Basic " + token);
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            map.add("redirect_uri", ConfigurationService.REDIRECT_URI);
            map.add("code", code);
            map.add("grant_type", "authorization_code");
            map.add("client_id", ConfigurationService.CLIENT_ID);
            map.add("client_secret", ConfigurationService.CLIENT_SECRET);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
            response = restTemplate.postForEntity(authUrl, request, String.class);
        } catch (Exception e){
            System.out.println(e.toString());
        }
        return response;
    }
}
