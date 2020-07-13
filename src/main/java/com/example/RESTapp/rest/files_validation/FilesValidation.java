package com.example.RESTapp.rest.files_validation;

import org.springframework.web.multipart.MultipartFile;

/**
 * The interface provides three methods which validate the uploading files
 */
public interface FilesValidation {
    boolean validateHeader(MultipartFile file);
    boolean validateRecordPrimaryKey(MultipartFile file);
    boolean validateRecordLength(MultipartFile file);
}
