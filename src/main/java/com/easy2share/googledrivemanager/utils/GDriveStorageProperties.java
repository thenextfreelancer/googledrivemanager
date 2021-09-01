package com.easy2share.googledrivemanager.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "gdrive")
public class GDriveStorageProperties
{
   private String uploadDir;

   public String getUploadDir()
   {
      return uploadDir;
   }

   public void setUploadDir(String uploadDir)
   {
      this.uploadDir = uploadDir;
   }
}