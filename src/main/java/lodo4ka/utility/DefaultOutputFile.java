package lodo4ka.utility;

public enum DefaultOutputFile {
    INTEGERS_FILENAME("integers.txt"),
    FLOATS_FILENAME("floats.txt"),
    STRINGS_FILENAME("strings.txt"),
    PATH("."),
    PREFIX("");

    DefaultOutputFile(String value) {
        this.value = value;
    }

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
