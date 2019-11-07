package edu.wisc.entity.normalizer.model;

public class KeyValue implements Comparable<KeyValue>{
    int index;
    String value;

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return "KeyValue{" +
                "index=" + index +
                ", value='" + value + '\'' +
                '}';
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public int compareTo(KeyValue o) {
        String compareQuantity = o.getValue().toUpperCase();

        //ascending order
        return this.value.toUpperCase().compareTo(compareQuantity);
    }
}
