package com.example.RESTapp.rest.files_controller_utilities;

import com.example.RESTapp.model.Record;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * The implementation of FilesControllerUtilities interface
 */
@Component
public class FilesControllerUtilitiesImpl implements FilesControllerUtilities {

    /**
     * Method extracts primary key and values as List of String arrays from records
     * @return a Record object
     */
    @Override
    public Record extractRecords(List<String[]> records, String primaryKey) {
        Map<String, List<String[]>> recordMap = new LinkedHashMap<>();
        Record record = new Record();

        recordMap.put(primaryKey, records);

        record.setRecord(recordMap);

        return record;
    }

    /**
     * Method deletes a record by primary key and creates a new plain text file without it
     * @return true if primary key exists
     */
    @Override
    public boolean deleteLines(File file, String primary_key) {
        List<String> lines = null;

        try {
            lines = FileUtils.readLines(file, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Objects.requireNonNull(lines, "File perhaps is null").stream().anyMatch(s -> s.startsWith(primary_key))) {

            List<String> updatedLines = lines
                    .stream()
                    .filter(s -> (!s.startsWith(primary_key)))
                    .collect(Collectors.toList());

            try {
                FileUtils.writeLines(file, updatedLines, false);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return true;
        }

        return false;
    }
}
