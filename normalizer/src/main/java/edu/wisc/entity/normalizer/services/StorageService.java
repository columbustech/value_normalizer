package edu.wisc.entity.normalizer.services;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {
    private final String storageLocation = ConfigurationService.RESOURCE_LOCATION;
    public static String resourceLocation = "";
    public void store(MultipartFile file) {
        try {
            File newFile = new File(storageLocation+file.getOriginalFilename());
            FileUtils.copyToFile(file.getInputStream(), newFile);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("FAIL!");
        }
    }

    public void store(URL url) {
        try {
            ReadableByteChannel rbc = Channels.newChannel(url.openStream());
            FileOutputStream fos = new FileOutputStream(storageLocation+ FilenameUtils.getName(url.getPath()));
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("FAIL!");
        }
    }

    public File getFile(String name) {
        File newFile = new File(storageLocation+name);
        return newFile;
    }

}
