package edu.wisc.entity.normalizer.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.gson.Gson;

public class CsvToJsonConverter {

    public static String readObjectsFromCsv(File file) throws IOException {
        CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader(Map.class).with(bootstrap).readValues(file);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(mappingIterator.readAll());
    }

    public static String getHeaders(File file) throws IOException {
        Map<Integer, String> headers = new LinkedHashMap<>();
        int index = 0;
        CsvSchema bootstrap = CsvSchema.emptySchema().withHeader();
        CsvMapper csvMapper = new CsvMapper();
        MappingIterator<Map<?, ?>> mappingIterator = csvMapper.reader(Map.class).with(bootstrap).readValues(file);
        while (mappingIterator.hasNext()) {
            for (Map.Entry<?, ?> entry : mappingIterator.next().entrySet())
            {
                headers.put(index , entry.getKey().toString());
                index = index +1;
            }
            // Break as soon as you read the first row (header).
            break;
        }
        Gson json = new Gson();
        String jsonHeader = json.toJson(headers);
        System.out.println(jsonHeader);
        return jsonHeader;
    }

}
