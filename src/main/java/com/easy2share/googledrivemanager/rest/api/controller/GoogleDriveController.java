package com.easy2share.googledrivemanager.rest.api.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.easy2share.googledrivemanager.rest.api.types.UploadFileResponse;
import com.easy2share.googledrivemanager.service.GDriveStorageService;
import com.google.api.client.util.Lists;
import com.google.api.services.drive.model.File;

@RestController
@RequestMapping("gdrive")
public class GoogleDriveController
{

   private static final Logger logger = LoggerFactory.getLogger(GoogleDriveController.class);

   @Autowired
   private GDriveStorageService gdriveStorageService;

   @PostMapping("/uploadFile")
   public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file)
   {
      String fileDownloadUrl = gdriveStorageService.uploadFile(file);
      logger.info("File has been uploaded.");
      return new UploadFileResponse(file.getOriginalFilename(), fileDownloadUrl, file.getContentType(), file.getSize());
   }

   @PostMapping("/uploadMultipleFiles")
   public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files)
   {
      return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
   }

   @GetMapping("/getAllFiles")
   public List<UploadFileResponse> getAllFiles()
   {
      List<UploadFileResponse> response = Lists.newArrayList();
      List<File> files = gdriveStorageService.getAllFiles();
      for (File f : files)
      {
         response.add(new UploadFileResponse(f.getOriginalFilename(), f.getWebContentLink(), f.getMimeType(), f.getSize()));
      }
      return response;
   }
}
