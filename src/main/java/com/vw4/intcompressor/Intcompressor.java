/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.vw4.intcompressor;
import me.lemire.integercompression.*;
import org.apache.commons.cli.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileWriter;
import org.json.JSONArray;

/**
 *
 * @author vyvn
 */
public class Intcompressor {

    public static void main(String[] args) {
        Options options = new Options();
        Option input = new Option("i", "input", true, "input file path");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("o", "output", true, "output file");
        output.setRequired(true);
        options.addOption(output);

        Option action = new Option("a", "action", true, "action");
        action.setRequired(true);
        options.addOption(action);
        
        
        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Intcompressor", options);

            System.exit(1);
        }

        String inputFilePath = cmd.getOptionValue("input");
        String outputFilePath = cmd.getOptionValue("output");
        String actionName = cmd.getOptionValue("action");

        System.out.println(inputFilePath);
        System.out.println(outputFilePath);
        System.out.println(actionName);
        
        try {
            String content = new String(Files.readAllBytes(Paths.get(inputFilePath)));
            JSONArray jsonArray = new JSONArray(content);
            
            int[] intArray = new int[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                intArray[i] = jsonArray.getInt(i);
            }
            
            IntCompressor ic = new IntCompressor();
            int[] outputIntArray;
            if (actionName.equals("compress")) {
                outputIntArray = ic.compress(intArray);
            } else {
                outputIntArray = ic.uncompress(intArray);
            }

            JSONArray outputJsonArray = new JSONArray();
            for (int num : outputIntArray) {
                outputJsonArray.put(num);
            }
            
            FileWriter file = new FileWriter(outputFilePath);
            file.write(outputJsonArray.toString(4));
            
            file.flush();
        
            System.out.println("JSON file created: " + outputFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}