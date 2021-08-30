package com.educationalbotters.build;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

public class Build {

    public static String SCRIPT_SOURCE = "./src/com/educationalbotters/customscripts";
    public static String SCRIPT_OUTPUT_SOURCE = "./out/production/educational-runescape-bot/com/educationalbotters/customscripts";
    public static String SRC_ROOT = "./src/com/educationalbotters";
    public static String OUTPUT_SRC_ROOT = "./out/production/educational-runescape-bot";

    public static List<String> gatherAllScriptNames() {
        return Arrays.asList(new File(SCRIPT_SOURCE).list());
    }

    public static Set<String> getAllJavaFiles(String basePath) {
        File currentDirectory = new File(basePath);
        if (currentDirectory == null) {
            return Collections.emptySet();
        }
        Set<String> javaFiles = new HashSet<>();
        String[] javaFilesInThisFolder = currentDirectory.list((dir, name) -> {
            String nextPath = dir.getPath() + File.separator + name;
            if (new File(nextPath).isDirectory()) {
                javaFiles.addAll(getAllJavaFiles(nextPath));
                return false;
            }
            return name.endsWith(".java");
        });

        if(javaFilesInThisFolder != null) {
            Set<String> paths = Arrays.stream(javaFilesInThisFolder)
                    .map(file -> basePath + File.separator + file)
                    .collect(Collectors.toSet());

            javaFiles.addAll(paths);
        }
        return javaFiles;
    }

    private static String buildCompileString(Set<String> javaFiles) {
        StringBuilder javaInputFilesString = new StringBuilder();
        for(String input : javaFiles) {
            javaInputFilesString.append(input);
            javaInputFilesString.append(" ");
        }
        return javaInputFilesString.toString();
    }


    public static Set<String> compileDirectory(String directory) throws IOException, InterruptedException {
       Set<String> javaInputFiles = getAllJavaFiles(directory);
       File output = new File(OUTPUT_SRC_ROOT);
       if(!output.exists()) {
           output.mkdirs();
       }
       String compileString = buildCompileString(javaInputFiles);
       Runtime.getRuntime().exec(String.format("javac -d %s %s", OUTPUT_SRC_ROOT, compileString)).waitFor();

       return javaInputFiles;
    }

    public static void buildScript(String script, String userOutput, Set<String> includes) throws IOException, InterruptedException {
        System.out.println("Script is: " + script);
        Set<String> javaInputFiles = getAllJavaFiles(SCRIPT_SOURCE + File.separator + script);

        if(javaInputFiles.isEmpty()) {
            return;
        }

        String javaInputFilesString = buildCompileString(javaInputFiles);

        File outputDir = new File(userOutput);
        if(!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File scriptOutputDir = new File(OUTPUT_SRC_ROOT);
        if(!scriptOutputDir.exists()) {
            scriptOutputDir.mkdirs();
        }


        System.out.println("Output: " + OUTPUT_SRC_ROOT + " input files: " + javaInputFilesString);
        Runtime.getRuntime().exec(String.format("javac -cp dependencies/OSBotApi.jar -d %s %s", OUTPUT_SRC_ROOT, javaInputFilesString)).waitFor();

        String compiledJavaInputString = (javaInputFilesString + buildCompileString(includes))
                .replaceAll(".java", ".class")
                .replaceAll("\\./src/", "")
                // .\src\ -> ""
                .replaceAll("\\.\\\\src\\\\", "");

        System.out.println("Build string is: " + compiledJavaInputString);
        Runtime.getRuntime().exec(
                String.format("jar cvf %s.jar %s", script, compiledJavaInputString),
                null,
                new File(OUTPUT_SRC_ROOT)
        ).waitFor();

        Path jarFile = Paths.get(OUTPUT_SRC_ROOT + File.separator + script + ".jar");
        Path outputPath = Paths.get(userOutput + File.separator + script + ".jar");

        Files.move(jarFile, outputPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        List<String> filesToBuild = new ArrayList<>();
        List<String> filesToIncludeInPackage = new ArrayList<>();
        String outputPath = null;
        boolean isBuildingAll = false;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            switch (arg) {
                case "--all":
                    System.out.println("Build all");
                    isBuildingAll = true;
                    break;
                case "--script":
                    String scriptName;
                    try {
                        scriptName = args[++i];
                        System.out.println("Building Script: " + scriptName);
                        filesToBuild.add(scriptName);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("No argument provided after --script");
                        System.exit(-1);
                    }
                    break;
                case "--output":
                    try {
                        outputPath = args[++i];
                        System.out.println("Output directory is: " + outputPath);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("No argument provided after --output");
                        System.exit(-1);
                    }
                    break;
                case "--include":
                    String includes;
                    try {
                       includes = args[++i];
                       System.out.println("Including directory: " + includes);
                       filesToIncludeInPackage.add(includes);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("No argument provided after --includes");
                        System.exit(-1);
                    }
                    break;
                default:
                    System.out.println("Unrecognized argument: " + arg);
            }
        }
        if (outputPath == null || outputPath.isEmpty()) {
            System.out.println("No output directory specified, exiting!");
            System.exit(-1);
        }

        if (isBuildingAll) {
            filesToBuild = gatherAllScriptNames();
        }

        Set<String> includedJavaFiles = new HashSet<>();
        for(String include : filesToIncludeInPackage) {
            includedJavaFiles.addAll(compileDirectory(SRC_ROOT + File.separator + include));
        }

        boolean hasError = false;
        for (String file : filesToBuild) {
            try {
                buildScript(file, outputPath, includedJavaFiles);
            } catch (Exception e) {
                System.out.println("Failed to build: " + file);
                System.out.println(e);
                hasError = true;
            }
        }

        // Let application caller know there were errors.
        if(hasError) {
            System.exit(-1);
        }
    }
}