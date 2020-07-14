package com.example.RESTapp.rest.files_validation;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Objects;

/**
 * The implementation of interface for files validation
 */
@Component
public class FilesValidationImpl implements FilesValidation {

    /**
     * Method validates the header in plain text file
     * @return true if header contains obligatory string sequence
     */
    @Override
    public boolean validateHeader(MultipartFile file) {
        InputStream reader = null;
        try {
            reader = new BufferedInputStream(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(reader, "InputStream object is null"), Charset.defaultCharset()))
                .lines()
                .limit(1L)
                .anyMatch(s -> s.equals("PRIMARY_KEY,NAME,DESCRIPTION,UPDATED_TIMESTAMP"));
    }

    /**
     * Method validates the records primary keys
     * @return false if primary key is blank
     */
    @Override
    public boolean validateRecordPrimaryKey(MultipartFile file) {
        InputStream reader = null;
        try {
            reader = new BufferedInputStream(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(reader, "InputStream object is null"), Charset.defaultCharset()))
                .lines()
                .skip(1L)
                .anyMatch(l -> l.startsWith(","));
    }

    /**
     * Method validates the records length
     * @return false if record is greater than 4 elements
     */
    @Override
    public boolean validateRecordLength(MultipartFile file) {
        InputStream reader = null;
        try {
            reader = new BufferedInputStream(file.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new BufferedReader(
                new InputStreamReader(Objects.requireNonNull(reader, "InputStream object is null"), Charset.defaultCharset()))
                .lines()
                .skip(1L)
                .map(l -> l.split(","))
                .anyMatch(l -> l.length > 4);
    }
}
