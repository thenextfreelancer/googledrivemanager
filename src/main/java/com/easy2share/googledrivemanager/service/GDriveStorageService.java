package com.easy2share.googledrivemanager.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.easy2share.googledrivemanager.execption.FileStorageException;
import com.easy2share.googledrivemanager.service.gdrive.CreateGoogleFile;
import com.easy2share.googledrivemanager.service.gdrive.FindFiles;
import com.easy2share.googledrivemanager.service.gdrive.ShareGoogleFile;
import com.easy2share.googledrivemanager.utils.GDriveStorageProperties;
import com.google.api.client.util.Lists;
import com.google.api.services.drive.model.File;

@Service
public class GDriveStorageService
{
   private final String gdriveStorageDirName;

   @Autowired
   public GDriveStorageService(GDriveStorageProperties gdriveStorageProperties)
   {
      this.gdriveStorageDirName = gdriveStorageProperties.getUploadDir();
   }

   public String uploadFile(MultipartFile file)
   {
      String uploadedFileName = "";
      try
      {
         String folderId = CreateGoogleFile.createGDriveFolder(null, this.gdriveStorageDirName);
         uploadedFileName = storeFile(file, folderId);
      }
      catch (Exception ex)
      {
         throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
      }

      return uploadedFileName;
   }

   public String storeFile(MultipartFile file, String folderId)
   {
      File googleFile = null;
      try
      {
         InputStream is = file.getInputStream();
         googleFile = CreateGoogleFile.createGoogleFile(folderId, file.getContentType(), file.getOriginalFilename(), is);
         ShareGoogleFile.createPublicPermission(googleFile.getId());
         System.out.println("Created Google file!");
         System.out.println("WebContentLink: " + googleFile.getWebContentLink());
         System.out.println("WebViewLink: " + googleFile.getWebViewLink());

         System.out.println("Done!");
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      return googleFile.getWebContentLink();
   }

   // All file attributes; https://developers.google.com/drive/api/v3/reference/files
   public List<File> getAllFiles()
   {
      List<File> files = Lists.newArrayList();
      try
      {
         String folderId = CreateGoogleFile.createGDriveFolder(null, this.gdriveStorageDirName);
         files = FindFiles.getAllFilesInFolder(folderId);
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
      return files;
   }
}
