package com.easy2share.googledrivemanager.service.gdrive;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class CreateGoogleFile
{

   public static final File createGoogleFolder(String folderIdParent, String folderName) throws IOException
   {

      File fileMetadata = new File();

      fileMetadata.setName(folderName);
      fileMetadata.setMimeType("application/vnd.google-apps.folder");

      if (folderIdParent != null)
      {
         List<String> parents = Arrays.asList(folderIdParent);

         fileMetadata.setParents(parents);
      }
      Drive driveService = GoogleDriveUtils.getDriveService();

      // Create a Folder.
      // Returns File object with id & name fields will be assigned values
      File file = driveService.files().create(fileMetadata).setFields("id, name").execute();

      return file;
   }

   public static String createGDriveFolder(String googleFolderIdParent, String folderName) throws IOException
   {
      String folderId = GetSubFolders.getFolderIdIFExist(googleFolderIdParent, folderName);
      if (folderId == null)
      {
         File fileMetadata = new File();
         fileMetadata.setName(folderName);
         fileMetadata.setMimeType("application/vnd.google-apps.folder");

         Drive driveService = GoogleDriveUtils.getDriveService();
         File file = driveService.files().create(fileMetadata).setFields("id").execute();
         folderId = file.getId();
         System.out.println("Folder ID: " + file.getId());
      }
      return folderId;
   }

   // PRIVATE!
   private static File _createGoogleFile(
      String googleFolderIdParent,
      String contentType, //
      String customFileName,
      AbstractInputStreamContent uploadStreamContent)
      throws IOException
   {

      File fileMetadata = new File();
      fileMetadata.setName(customFileName);

      List<String> parents = Arrays.asList(googleFolderIdParent);
      fileMetadata.setParents(parents);
      //
      Drive driveService = GoogleDriveUtils.getDriveService();

      File file = driveService.files().create(fileMetadata, uploadStreamContent).setFields("id, webContentLink, webViewLink, parents").execute();

      return file;
   }

   // Create Google File from byte[]
   public static File createGoogleFile(
      String googleFolderIdParent,
      String contentType,
      String customFileName,
      byte[] uploadData)
      throws IOException
   {
      AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);
      return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
   }

   // Create Google File from java.io.File
   public static File createGoogleFile(
      String googleFolderIdParent,
      String contentType, //
      String customFileName,
      java.io.File uploadFile)
      throws IOException
   {
      AbstractInputStreamContent uploadStreamContent = new FileContent(contentType, uploadFile);
      return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
   }

   // Create Google File from InputStream
   public static File createGoogleFile(
      String googleFolderIdParent,
      String contentType, //
      String customFileName,
      InputStream inputStream)
      throws IOException
   {
      AbstractInputStreamContent uploadStreamContent = new InputStreamContent(contentType, inputStream);
      return _createGoogleFile(googleFolderIdParent, contentType, customFileName, uploadStreamContent);
   }

   // Test method
   public static void main(String[] args) throws IOException
   {

      java.io.File uploadFile = new java.io.File("C:\\Users\\abc\\Cooking_Challenge.mp4");

      // Create Google File:
      File googleFile = createGoogleFile(GetSubFolders.getUploadLocationIdByName("Uploads"), "video/mp4", "Cooking_Challenge.mp4", uploadFile);

      System.out.println("Created Google file!");
      System.out.println("WebContentLink: " + googleFile.getWebContentLink());
      System.out.println("WebViewLink: " + googleFile.getWebViewLink());
      System.out.println("Done!");
   }

}