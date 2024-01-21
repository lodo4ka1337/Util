package lodo4ka.utility;

public enum OutputFileNames {
    INTEGERS("integers.txt"),
    FLOATS("floats.txt"),
    STRINGS("strings.txt");

    OutputFileNames(String fileName) {
        this.fileName = fileName;
    }

    private final String fileName;

    @Override
    public String toString() {
        return fileName;
    }
}
