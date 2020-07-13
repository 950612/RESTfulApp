package com.example.RESTapp.rest.files_controller_utilities;

import com.example.RESTapp.model.Record;

import java.io.File;
import java.util.List;

/**
 * The interface provides two methods which help the FilesController class
 */
public interface FilesControllerUtilities {
    Record extractRecords (List<String[]> records, String primaryKey);
    boolean deleteLines(File file, String primary_key);
}
