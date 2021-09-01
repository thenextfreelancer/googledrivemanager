package com.easy2share.googledrivemanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.easy2share.googledrivemanager.utils.FileStorageProperties;
import com.easy2share.googledrivemanager.utils.GDriveStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties({ FileStorageProperties.class, GDriveStorageProperties.class})
public class GoogledrivemanagerApplication
{

   public static void main(String[] args)
   {
      SpringApplication.run(GoogledrivemanagerApplication.class, args);
   }

}
