package edu.wisc.entity.normalizer;

import edu.wisc.entity.normalizer.services.ConfigurationService;
import edu.wisc.entity.normalizer.services.StorageService;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class NormalizerApplication {

    @Autowired
    ConfigurationService configurationService;

    public static void main(String[] args) {
        SpringApplication.run(NormalizerApplication.class, args);
    }
}
