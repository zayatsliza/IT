package database;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;

public class Element implements Serializable {
    private String value;
    private String column;

    Element(String value, String column) {
        this.value = value;
        this.column = column;
    }

    @JsonIgnore
    public Integer getAsInteger() {
        return parseInt(value);
    }

    @JsonIgnore
    public Float getAsFloat() {
        return Float.parseFloat(value);
    }

    @JsonIgnore
    public char getAsCharacter() throws Exception {
        if (value.length() != 1) throw new Exception("Invalid character value");
        return value.charAt(0);
    }

    @JsonIgnore
    public String getAsString() {
        return value;
    }

    @JsonIgnore
    public String getAsEmail() throws Exception {
        Pattern regexPattern = Pattern.compile("^[(a-zA-Z-0-9-\\_\\+\\.)]+@[(a-z-A-z)]+\\.[(a-zA-z)]{2,3}$");
        Matcher regMatcher = regexPattern.matcher(value);
        if (regMatcher.matches()) {
            return value;
        } else {
            throw new Exception("Invalid email value");
        }
    }

    @JsonIgnore
    public MyEnum getAsEnum() throws Exception {
        Pattern regexPattern = Pattern.compile("^\\s*\\{(\\s*\\w* *= *\\d*\\s*;?)*?\\s*\\}$");
        Matcher regMatcher = regexPattern.matcher(value);
        if (regMatcher.matches()) {
            String str = value;
            str = str.replaceAll("[{}]", "");
            ArrayList<String> arr = Arrays.stream(str.split(";")).map(String::trim).collect(Collectors.toCollection(ArrayList::new));
            ArrayList<String> keys = new ArrayList<>();
            ArrayList<Integer> values = new ArrayList<>();
            for (String a : arr) {
                String[] pair = a.split("=");
                keys.add(pair[0]);
                values.add(parseInt(pair[1]));
            }
            MyEnum myEnum = new MyEnum(keys, values);
            return myEnum;
        } else {
            throw new Exception("Invalid email value");
        }
    }

    public String getColumn() {
        return column;
    }

    public String getValue() {
        return value;
    }

    void setValue(String value) {
        this.value = value;
    }

    void validate(Table table) throws Exception {
        Column column = table.getColumn(this.column);

        if (value == null) {
            if (column.isNullAllowed()) return;
            throw new Exception("Null value is not allowed");
        }

        try {
            switch (column.getType()) {
                case INT:
                    getAsInteger();
                    break;
                case FLOAT:
                    getAsFloat();
                    break;
                case CHAR:
                    getAsCharacter();
                    break;
                case STR:
                    getAsString();
                    break;
                case EMAIL:
                    getAsEmail();
                    break;
                case ENUM:
                    getAsEnum();
                    break;
            }
        } catch (Exception e) {
            throw new Exception(String.format("Invalid element value '%s': %s", value, e.getMessage()));
        }
    }

    public boolean equals(String other) {
        if (other == null) return value == null;
        if (value == null) return other.equals("null");
        return value.equals(other);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return Objects.equals(value, element.value) &&
                Objects.equals(column, element.column);
    }

}