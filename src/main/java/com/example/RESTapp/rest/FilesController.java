package com.example.RESTapp.rest;

import com.example.RESTapp.exception.*;
import com.example.RESTapp.message.ResponseMessage;
import com.example.RESTapp.rest.files_controller_utilities.FilesControllerUtilities;
import com.example.RESTapp.service.FilesStorageService;
import com.example.RESTapp.rest.files_validation.FilesValidation;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The Rest Controller class provides three methods: a POST method to upload a single text file, a GET method to get record from text file and DELETE method to delete record from text file
 */
@RestController
public class FilesController {

    private final FilesStorageService storageService;
    private final FilesValidation filesValidation;
    private final FilesControllerUtilities filesControllerUtilities;

    private String message;
    private String httpStatus;

    @Autowired
    public FilesController(FilesStorageService storageService, FilesValidation filesValidation, FilesControllerUtilities filesControllerUtilities) {
        this.storageService = storageService;
        this.filesValidation = filesValidation;
        this.filesControllerUtilities = filesControllerUtilities;
    }

    /**
     * Method uploads a single text file only if file is not empty, has txt extension, obligatory header, not blank primary key, suitable record length and last line empty
     * @return ResponseEntity with ResponseMessage object as body
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
            try {
                if (!Objects.requireNonNull(FilenameUtils.getExtension(file.getOriginalFilename()), "File cannot be null").equals("txt") || file.isEmpty()) {
                    throw new IncorrectFileExtensionOrSizeException();
                }
                else if (!filesValidation.validateHeader(file)) {
                    throw new HeaderWasNotFoundException();
                }
                else if (filesValidation.validateRecordPrimaryKey(file)) {
                    throw new NotPrimaryKeyException();
                }
                else if (filesValidation.validateRecordLength(file)) {
                    throw new BadRecordLengthException();
                }

                storageService.save(file);
                message = "Uploaded the file successfully: " + file.getOriginalFilename() + "!";
                httpStatus = HttpStatus.OK.toString();

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, httpStatus));

            } catch (HeaderWasNotFoundException | NotPrimaryKeyException | BadRecordLengthException | IncorrectFileExtensionOrSizeException ex) {
                message = ex.getMessage();
                httpStatus = HttpStatus.EXPECTATION_FAILED.toString();

                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message, httpStatus));

            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "! File with this name perhaps exists";
                httpStatus = HttpStatus.EXPECTATION_FAILED.toString();

                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message, httpStatus));
        }
    }

    /**
     * Method reads a text file and returns single record by primary key
     * @return ResponseEntity with parametrized type which depends on method result - a Record object or a ResponseMessage object as ResponseEntity body when exception is thrown
     */
    @GetMapping(value = "/files/{fileName:.+}/{primaryKey}")
    @ResponseBody
    public ResponseEntity<?> getSingleRecord(@PathVariable String fileName, @PathVariable String primaryKey) {
        try {
            Resource file = storageService.load(fileName);

            List <String[]> records = new BufferedReader(
                    new FileReader(file.getFile()))
                    .lines()
                    .skip(1L)
                    .filter(l -> l.startsWith(primaryKey))
                    .map(l -> l.substring(2))
                    .map(l -> l.split(","))
                    .collect(Collectors.toList());

            if (!records.isEmpty()) {
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(filesControllerUtilities.extractRecords(records, primaryKey));
            } else
                throw new PrimaryKeyWasNotFoundException();

        } catch (PrimaryKeyWasNotFoundException ex) {
            message = ex.getMessage();
            httpStatus = HttpStatus.EXPECTATION_FAILED.toString();

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message, httpStatus));

        } catch (Exception e) {
            message = "File perhaps was not found or is unreadable!";
            httpStatus = HttpStatus.EXPECTATION_FAILED.toString();

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message, httpStatus));
        }
    }

    /**
     * Method deletes a single record from text file only if they exist
     * @return ResponseEntity with http status and ResponseMessage object as body
     */
    @DeleteMapping("/files/{filename:.+}/{primary_key}")
    public ResponseEntity<ResponseMessage> deleteRecords(@PathVariable String filename, @PathVariable String primary_key) {
        try {
            Resource file = storageService.load(filename);

            if (filesControllerUtilities.deleteLines(file.getFile(), primary_key)) {
                message = "Record was deleted successful!";
                httpStatus = HttpStatus.OK.toString();

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message, httpStatus));
            }
            else
                throw new PrimaryKeyWasNotFoundException();

            } catch (PrimaryKeyWasNotFoundException ex) {
            message = ex.getMessage();
            httpStatus = HttpStatus.EXPECTATION_FAILED.toString();

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message, httpStatus));

            } catch (Exception e) {
            message = "File perhaps was not found or is unreadable!";
            httpStatus = HttpStatus.EXPECTATION_FAILED.toString();

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message, httpStatus));
        }
    }
}
