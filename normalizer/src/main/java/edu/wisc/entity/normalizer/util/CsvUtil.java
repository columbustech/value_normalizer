package edu.wisc.entity.normalizer.util;

import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import edu.wisc.entity.normalizer.model.KeyList;
import edu.wisc.entity.normalizer.model.KeyValue;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class CsvUtil {

    public static String getCsvColumn(File file, int column, int sort) {
        try {
            int index = 0;
            List<KeyValue> columnList = new ArrayList<>();
            //Map<Integer, String> columnList = new LinkedHashMap<>();
            Gson json = new Gson();
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                KeyValue columnData = new KeyValue();
                columnData.setIndex(index);
                columnData.setValue(nextLine[column]);
                columnList.add(index, columnData);
                index++;
            }
            if (sort == 1) {
                System.out.println("Sorting");
                Collections.sort(columnList);
            }

            String columnJson = json.toJson(columnList);
            System.out.println(columnJson);
            return columnJson;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("FAIL!");
        }
    }

    public static String writeCSVDiff(File file, int column, List<List<KeyValue>> keyValData) {
        try {
            Map<Integer, String> diffMap = getLocalDiffMapFromList(keyValData);
            CSVReader reader = new CSVReader(new FileReader(file));
            List<String[]> csvBody = reader.readAll();
            diffMap.forEach((index, value) -> {
                System.out.println("Previous data at row:"+index+ " is:"+ csvBody.get(index)[column]);
                csvBody.get(index)[column] = value;
                System.out.println("New data at row:"+index+ " is:"//+ csvBody.get(index)[column]
                        + " where value is:"+value);
            });
            reader.close();
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            writer.writeAll(csvBody);
            writer.flush();
            writer.close();
            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("FAIL!");
        }
    }

    public static String writeGlobalDiff(File file, int column, List<List<Integer>> keyValData) {
        try {
            CSVReader reader = new CSVReader(new FileReader(file));
            List<String[]> csvBody = reader.readAll();
            Map<Integer, String> diffMap = getGlobalDiffMapFromList(keyValData, csvBody, column);
            diffMap.forEach((index, value) -> {
                System.out.println("Previous data at row:"+index+ " is:"+ csvBody.get(index)[column]);
                csvBody.get(index)[column] = value;
                System.out.println("New data at row:"+index + " is:"+ csvBody.get(index)[column]//
                        + " where value is:"+value);
            });
            reader.close();
            CSVWriter writer = new CSVWriter(new FileWriter(file));
            writer.writeAll(csvBody);
            writer.flush();
            writer.close();
            return "SUCCESS";
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("FAIL!");
        }
    }

    public static Map<Integer, String> getGlobalDiffMapFromList(List<List<Integer>> keyValData, List<String[]> csvBody, int column){
        Map<Integer, String> diffMap = new HashMap<>();
        for (int i = 0; i<keyValData.size(); i++) {
            List<Integer> listToMerge = keyValData.get(i);
            // First find the largest Value!
            String maxValueToSave = "";
            for (int j = 0; j < listToMerge.size(); j++) {
                if(maxValueToSave.length() < csvBody.get(listToMerge.get(j))[column].length()) {
                    maxValueToSave = csvBody.get(listToMerge.get(j))[column];
                }
            }
//            System.out.println("max value to save in List:" + i + " is:" + maxValueToSave);
            //Save the largest Value
            for (int j = 0; j < listToMerge.size(); j++) {
//                System.out.print("Max values with other indices: " + listToMerge.get(j));
                diffMap.put(listToMerge.get(j), maxValueToSave);
            }
        }
        return diffMap;


//        keyValData.entrySet().forEach( (entry) -> {
//            List<Integer> valueList = entry.getValue();
//            // First find the largest Value!
//            String maxValueToSave = "";
//            if(maxValueToSave.length() < csvBody.get(entry.getKey())[column].length())
//                maxValueToSave = csvBody.get(entry.getKey())[column];
//
//            for (int j = 0; j < valueList.size(); j++) {
//                if(maxValueToSave.length() < csvBody.get(valueList.get(j))[column].length()) {
//                    maxValueToSave = csvBody.get(valueList.get(j))[column];
//                }
//            }
//            System.out.println("max value to save in List:" + entry.getKey() + " is:" + maxValueToSave);
//            //Save the largest Value
//            diffMap.put(entry.getKey(), maxValueToSave);
//            for (int j = 0; j < valueList.size(); j++) {
//                System.out.print("Max values with other indices: " + valueList.get(j));
//                diffMap.put(valueList.get(j), maxValueToSave);
//            }
//        });

       // return diffMap;
    }
    public static Map<Integer, String> getLocalDiffMapFromList(List<List<KeyValue>> keyValData){
        Map<Integer, String> diffMap = new HashMap<>();
        for (int i = 0; i<keyValData.size(); i++) {
            List<KeyValue> listToMerge = keyValData.get(i);
            // First find the largest Value!
            String maxValueToSave = "";
            for (int j = 0; j < listToMerge.size(); j++) {
                if(maxValueToSave.length() < listToMerge.get(j).getValue().length()) {
                    maxValueToSave = listToMerge.get(j).getValue();
                }
            }
            System.out.println("max value to save in List:" + i + " is:" + maxValueToSave);
            //Save the largest Value
            for (int j = 0; j < listToMerge.size(); j++) {
                diffMap.put(listToMerge.get(j).getIndex(), maxValueToSave);
            }
        }
        return diffMap;
    }

    public static String getGlobalColumn(File file, int column) {
        try {
            int index = 0;
            List<KeyValue> columnList = new ArrayList<>();
            Map<String, List<Integer>> keyToNumberOfValues = new LinkedHashMap<>();
            List<KeyList> tabularList = new ArrayList<>();
            //Map<Integer, String> columnList = new LinkedHashMap<>();
            Gson json = new Gson();
            CSVReader reader = new CSVReader(new FileReader(file));
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                KeyValue columnData = new KeyValue();
                columnData.setIndex(index);
                columnData.setValue(nextLine[column]);
                columnList.add(index, columnData);
                index++;
            }
            Collections.sort(columnList);
            columnList.forEach( (columnData) -> {
                if(keyToNumberOfValues.containsKey(columnData.getValue())) {
                    List<Integer> valueList = keyToNumberOfValues.get(columnData.getValue());
                    valueList.add(columnData.getIndex());
                    keyToNumberOfValues.put(columnData.getValue(), valueList);
                } else {
                    List<Integer> valueList = new ArrayList<>();
                    valueList.add(columnData.getIndex());
                    keyToNumberOfValues.put(columnData.getValue(), valueList);
                }
            });
            keyToNumberOfValues.entrySet().forEach((entry) -> {
                KeyList element = new KeyList();
                element.setKey(entry.getKey());
                element.setValues(entry.getValue());
                tabularList.add(element);
            });
            String columnJson = json.toJson(tabularList);
            System.out.println(columnJson);
            return columnJson;
        } catch (Exception e) {
            System.out.println(e);
            throw new RuntimeException("FAIL!");
        }
    }
}
