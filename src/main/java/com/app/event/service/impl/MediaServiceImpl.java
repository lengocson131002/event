package com.app.event.service.impl;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.app.event.config.AwsConfig;
import com.app.event.service.MediaService;
import com.app.event.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class MediaServiceImpl implements MediaService {
    private final AwsConfig awsConfig;

    @Override
    public String uploadFile(MultipartFile file) {
        File f = null;
        try {
            f = FileUtils.convertMultipartFileToFile(file);
            String fileName = FileUtils.generateUniqueFileName(file);
            uploadFile(fileName, f);
            return getObjectUrl(fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (f != null) {
                f.delete();
            }
        }
        return null;
    }

    private void uploadFile(String fileName, File file) {
        awsConfig.getS3Client().putObject(new PutObjectRequest(awsConfig.bucketName, fileName, file));
    }

    public String getObjectUrl(String fileName) {
        return "https://" + awsConfig.bucketName + ".s3." + awsConfig.region + ".amazonaws.com/" + fileName;
    }
}
