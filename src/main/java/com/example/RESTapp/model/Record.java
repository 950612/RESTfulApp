package com.example.RESTapp.model;

import java.util.List;
import java.util.Map;

/**
 * The model of the record which is a map with primary key as the map-key and List of String arrays as the map-value and assumes that primary key is non-unique
 */
public class Record {

   private Map<String, List<String []>> record;

    public Map<String, List<String[]>> getRecord() {
        return record;
    }

    public void setRecord(Map<String, List<String[]>> record) {
        this.record = record;
    }
}
