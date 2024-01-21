package lodo4ka;

import lodo4ka.utility.DefaultOutputFile;
import lodo4ka.utility.StringParser;
import org.apache.commons.cli.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Util {

    private Options options;
    private CommandLineParser parser;
    HelpFormatter formatter;
    CommandLine cmd;

    List<BigInteger> intList = new ArrayList<>();
    List<BigDecimal> floatList = new ArrayList<>();
    List<String> stringList = new ArrayList<>();

    public Util() {
        configCLI();
    }

    private void configCLI() {

        options = new Options();

        Option outputPath = new Option("o", "output", true, "Output path");
        outputPath.setRequired(false);
        outputPath.setOptionalArg(true);
        outputPath.setType(String.class);
        options.addOption(outputPath);

        Option filePrefix = new Option("p", "prefix", true, "Output files prefix");
        filePrefix.setRequired(false);
        filePrefix.setOptionalArg(true);
        filePrefix.setType(String.class);
        options.addOption(filePrefix);

        Option append = new Option("a", "append", false, "Append to output files");
        append.setRequired(false);
        options.addOption(append);

        Option shortStat = new Option("s", "short", false, "Short type statistics in console");
        shortStat.setRequired(false);
        options.addOption(shortStat);

        Option fullStat = new Option("f", "full", false, "Full type statistics in console");
        fullStat.setRequired(false);
        options.addOption(fullStat);

        Option help = new Option("h", "help", false, "Application usage help");
        help.setRequired(false);
        options.addOption(help);

        parser = new DefaultParser();

        formatter = new HelpFormatter();
        formatter.setOptionComparator(null);

    }

    private void cmdParse(String[] args) {

        try {

            cmd = parser.parse(options, args);
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("util.jar [OPTIONS] <INPUT_FILES>", options);
            System.exit(1);

        }

    }

    private void checkHelp() {
        if (cmd.hasOption("help")) {
            formatter.printHelp("util.jar [OPTIONS] <INPUT_FILES>", options);
            System.exit(1);
        }
    }

    private void readInput() {

        if (cmd.getArgs().length == 0) {
            System.out.println("Input files argument is missing");
            formatter.printHelp("util.jar [OPTIONS] <INPUT_FILES>", options);
            System.exit(1);
        }

        List<String> invalidFileNames = new ArrayList<>();

        for (String inputFileName : cmd.getArgs()) {

            try(BufferedReader br = new BufferedReader(new InputStreamReader(Files.newInputStream(Paths.get(inputFileName)), StandardCharsets.UTF_8))) {

                String line;

                while ((line = br.readLine()) != null) {

                    if (line.isEmpty()) {
                        continue;
                    }

                    BigInteger i = StringParser.strToInteger(line);
                    if (i != null) {
                        intList.add(i);
                        continue;
                    }

                    BigDecimal d = StringParser.strToFloat(line);
                    if (d != null) {
                        floatList.add(d);
                        continue;
                    }

                    stringList.add(line);
                }
            } catch (IOException ignored) {

                invalidFileNames.add(inputFileName);

            }

        }

        if (!invalidFileNames.isEmpty()) {

            System.out.print("Invalid input file(s): ");
            for (String i : invalidFileNames) {
                System.out.print(i + " ");
            }
            System.out.println();
            if (intList.isEmpty() && floatList.isEmpty() && stringList.isEmpty())
                System.exit(1);
        }

    }

    private void writeOutput() {

        String path;
        if (cmd.getOptionValue("output") != null) {
            if (cmd.getOptionValue("output").endsWith("/"))
                path = cmd.getOptionValue("output");
            else path = cmd.getOptionValue("output") + "/" ;
        }
        else path = DefaultOutputFile.PATH.toString();

        String prefix;
        if (cmd.getOptionValue("prefix") != null)
            prefix = cmd.getOptionValue("prefix");
        else prefix = DefaultOutputFile.PREFIX.toString();

        if (!intList.isEmpty()) {

            boolean s = writeToFile(intList, path, prefix, DefaultOutputFile.INTEGERS_FILENAME.toString());

            if (!s) {
                path = DefaultOutputFile.PATH.toString();
                writeToFile(intList, path, prefix, DefaultOutputFile.INTEGERS_FILENAME.toString());
            }

        }

        if (!floatList.isEmpty()) {

            writeToFile(floatList, path, prefix, DefaultOutputFile.FLOATS_FILENAME.toString());
        }

        if (!stringList.isEmpty()) {

            writeToFile(stringList, path, prefix, DefaultOutputFile.STRINGS_FILENAME.toString());
        }

    }

    private <T> boolean  writeToFile(List<T> list, String path, String prefix, String outputFileName) {

        File file = new File(path + prefix + outputFileName);
        file.getParentFile().mkdirs();

        try (PrintWriter intWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, cmd.hasOption("append")), StandardCharsets.UTF_8))) {
            for (T item : list) {
                intWriter.println(item);
            }

        } catch (IOException e) {

            System.out.println(e.getMessage());
            System.out.println("Invalid path: " + path + ". Setting output path to default (current directory)");
            return false;

        }

        return true;
    }

    private void typeStat() {

        if (cmd.hasOption("full")) {

            StringBuilder sb = new StringBuilder().append("Full type statistics:");

            if (!intList.isEmpty()) {

                int intNumber = intList.size();
                BigInteger intMin = Collections.min(intList);
                BigInteger intMax = Collections.max(intList);
                BigInteger intSum = BigInteger.ZERO;
                for (BigInteger i : intList) {
                    intSum = intSum.add(i);
                }
                BigDecimal intAvg = new BigDecimal(intSum).divide(BigDecimal.valueOf(intList.size()), 3, RoundingMode.CEILING);

                sb.append("\n\n\tInteger type statistics:" +
                                "\n\t\tNumber of integers: ").append(intNumber)
                        .append("\n\t\tMin: ").append(intMin)
                        .append("\n\t\tMax: ").append(intMax)
                        .append("\n\t\tSum: ").append(intSum)
                        .append("\n\t\tAverage: ").append(intAvg);
            }

            if (!floatList.isEmpty()) {
                int floatNumber = floatList.size();
                BigDecimal floatMin = Collections.min(floatList);
                BigDecimal floatMax = Collections.max(floatList);
                BigDecimal floatSum = BigDecimal.ZERO;
                for (BigDecimal f : floatList) {
                    floatSum = floatSum.add(f);
                }
                floatSum = floatSum.setScale(3, RoundingMode.CEILING);
                BigDecimal floatAvg = floatSum.divide(BigDecimal.valueOf(floatList.size()), 3, RoundingMode.CEILING);

                sb.append("\n\n\tFloat type statistics:" +
                                "\n\t\tNumber of floats: ").append(floatNumber)
                        .append( "\n\t\tMin: ").append(floatMin)
                        .append("\n\t\tMax: ").append(floatMax)
                        .append("\n\t\tSum: ").append(floatSum)
                        .append("\n\t\tAverage: ").append(floatAvg);
            }

            if (!stringList.isEmpty()) {
                int stringNumber = stringList.size();
                String longestString = stringList.stream().max(Comparator.comparingInt(String::length)).get();
                String shortestString = stringList.stream().min(Comparator.comparingInt(String::length)).get();

                sb.append("\n\n\tString type statistics:" +
                                "\n\t\tNumber of strings: ").append(stringNumber)
                        .append("\n\t\tLongest: ").append(longestString)
                        .append(" (").append(longestString.length()).append(" chars)")
                        .append("\n\t\tShortest: ").append(shortestString)
                        .append(" (").append(shortestString.length()).append(" chars)");
            }

            System.out.println(sb);
            return;
        }

        if (cmd.hasOption("short")) {
            StringBuilder sb = new StringBuilder().append("Short type statistics:");

            if (!intList.isEmpty()) {

                int intNumber = intList.size();

                sb.append("\n\n\tInteger type statistics:" +
                                "\n\t\tNumber of integers: ").append(intNumber);
            }

            if (!floatList.isEmpty()) {

                int floatNumber = floatList.size();

                sb.append("\n\n\tFloat type statistics:" +
                                "\n\t\tNumber of floats: ").append(floatNumber);
            }

            if (!stringList.isEmpty()) {

                int stringNumber = stringList.size();

                sb.append("\n\n\tString type statistics:" +
                                "\n\t\tNumber of strings: ").append(stringNumber);
            }

            System.out.println(sb);
        }

    }

    public void filter(String[] args) {

        cmdParse(args);

        checkHelp();

        readInput();
        writeOutput();
        typeStat();

    }
}
