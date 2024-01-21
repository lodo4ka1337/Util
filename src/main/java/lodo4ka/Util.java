package lodo4ka;

import lodo4ka.utility.OutputFileNames;
import lodo4ka.utility.StringParser;
import org.apache.commons.cli.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Util {
    public Util() {
        configCLI();
    }

    private Options options;
    private CommandLineParser parser;
    HelpFormatter formatter;
    CommandLine cmd;

    List<BigInteger> intList = new ArrayList<>();
    List<BigDecimal> floatList = new ArrayList<>();
    List<String> stringList = new ArrayList<>();

    String intFileName;
    String floatFileName;
    String stringFileName;

    private void configCLI() {

        options = new Options();

        Option outputPath = new Option("o", "output", true, "Output path");
        outputPath.setRequired(false);
        outputPath.setArgs(1);
        outputPath.setType(String.class);
        options.addOption(outputPath);

        Option filePrefix = new Option("p", "prefix", true, "Output files prefix");
        filePrefix.setRequired(false);
        filePrefix.setArgs(1);
        filePrefix.setType(String.class);
        options.addOption(filePrefix);

        Option append = new Option("a", "append", false, "Append to output files");
        append.setRequired(false);
        options.addOption(append);

        Option shortStat = new Option("s", "short", false, "Brief type statistics in console");
        shortStat.setRequired(false);

        Option fullStat = new Option("f", "full", false, "Full type statistics in console");
        fullStat.setRequired(false);

        OptionGroup statGroup = new OptionGroup();
        statGroup.addOption(shortStat);
        statGroup.addOption(fullStat);
        options.addOptionGroup(statGroup);

        parser = new DefaultParser();

        formatter = new HelpFormatter();
        formatter.setOptionComparator(null);

    }

    private void cmdParse(String[] args) {

        try {

            cmd = parser.parse(options, args);
            if (cmd.getArgs().length == 0)
                throw new ParseException("Input file(s) argument is missing");
        }
        catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("util.jar [OPTIONS] <FILES>", options);
            System.exit(1);
        }

    }

    private void readInput() {

        for (String inputFile : cmd.getArgs()) {

            try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8))) {

                String line = br.readLine();

                while (line != null) {

                    BigInteger i = StringParser.strToInteger(line);
                    if (i != null) {
                        intList.add(i);
                        line = br.readLine();
                        continue;
                    }

                    BigDecimal d = StringParser.strToFloat(line);
                    if (d != null) {
                        floatList.add(d);
                        line = br.readLine();
                        continue;
                    }

                    stringList.add(line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.exit(1);
            }

        }

    }

    private void writeOutput() {

        String path = ".";
        if (cmd.hasOption("output"))
            path = cmd.getOptionValue("output");

        String prefix = "";
        if (cmd.hasOption("prefix"))
            prefix = cmd.getOptionValue("prefix");

        if (!intList.isEmpty()) {
            intFileName = path + "/" + prefix + OutputFileNames.INTEGERS;
            writeToFile(intList, intFileName);
        }

        if (!floatList.isEmpty()) {
            floatFileName = path + "/" + prefix + OutputFileNames.FLOATS;
            writeToFile(floatList, floatFileName);
        }

        if (!stringList.isEmpty()) {
            stringFileName = path + "/" + prefix + OutputFileNames.STRINGS;
            writeToFile(stringList, stringFileName);
        }

    }

    private <T> void  writeToFile(List<T> list, String outputFileName) {

        File file = new File(outputFileName);
        file.getParentFile().mkdirs();

        try (PrintWriter intWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file, cmd.hasOption("append")), StandardCharsets.UTF_8))) {
            for (T item : list) {
                intWriter.println(item);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private void typeStat() {

        if (cmd.hasOption("short")) {
            int itemNumber = intList.size() + floatList.size() + stringList.size();
            System.out.println("Short type statistics: " +
                    "\n\n\tNumber of items written to output files: " + itemNumber);
            return;
        }

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
        }

    }

    public void filter(String[] args) {

        cmdParse(args);
        readInput();
        writeOutput();
        typeStat();

    }
}
