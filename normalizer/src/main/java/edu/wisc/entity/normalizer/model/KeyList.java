package edu.wisc.entity.normalizer.model;

import java.util.List;

public class KeyList implements Comparable<KeyList>{
    String key;
    List<Integer> values;

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "KeyList{" +
                "key=" + key +
                ", values='" + values + '\'' +
                '}';
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }

    public List<Integer> getValues() {
        return values;
    }

    @Override
    public int compareTo(KeyList o) {
        String compareQuantity = o.getKey().toUpperCase();

        //ascending order
        return this.key.toUpperCase().compareTo(compareQuantity);
    }
}
