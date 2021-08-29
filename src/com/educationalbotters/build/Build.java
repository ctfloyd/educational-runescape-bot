package com.educationalbotters.build;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class Build {

    public static String RELATIVE_PATH = "./src/com/educationalbotters/customscripts";
    public static String OUTPUT_SOURCE = "./out/production/educational-runescape-bot/com/educationalbotters/customscripts";

    public static List<String> gatherAllScriptNames() {
        return Arrays.asList(new File(RELATIVE_PATH).list());
    }

    public static Set<String> getAllJavaFiles(String basePath) {
        File currentDirectory = new File(basePath);
        if (currentDirectory == null) {
            return Collections.emptySet();
        }
        Set<String> javaFiles = new HashSet<>();
        String[] javaFilesInThisFolder = currentDirectory.list((dir, name) -> {
            boolean isJava = name.endsWith(".java");
            if (dir.isDirectory()) {
                javaFiles.addAll(getAllJavaFiles(dir.getAbsolutePath() + "/" + name));
            }
            return isJava;
        });
        if(javaFilesInThisFolder != null) {
            javaFiles.addAll(Arrays.asList(javaFilesInThisFolder));
            javaFiles.stream().map(file -> basePath + "/" + javaFilesInThisFolder);
        }
        return javaFiles;
    }

    public static void build(String script, String userOutput) throws IOException, InterruptedException {
        String compiledJavaOutput = OUTPUT_SOURCE + "/" + script;
        Set<String> javaInputFiles = getAllJavaFiles(RELATIVE_PATH + "/" + script);
        StringBuilder javaInputFilesString = new StringBuilder();
        for(String input : javaInputFiles) {
            javaInputFilesString.append(input);
            javaInputFilesString.append(" ");
        }
        System.out.println("Input files: " + javaInputFilesString);

        Runtime.getRuntime().exec(String.format("javac -d %s %s", compiledJavaOutput, javaInputFilesString)).waitFor();
        Runtime.getRuntime().exec(String.format("jar cvf %s.jar %s/*", script, compiledJavaOutput)).waitFor();
        Path jarFile = Paths.get("./" + script + ".jar");
        Path outputPath = Paths.get(userOutput + "/" + script + ".jar");
        Files.move(jarFile, outputPath, StandardCopyOption.REPLACE_EXISTING);
    }

    public static void main(String[] args) {
        List<String> filesToBuild = new ArrayList<>();
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
                    }
                    break;
                case "--output":
                    try {
                        outputPath = args[++i];
                        System.out.println("Output directory is: " + outputPath);
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("No argument provided after --output");
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

        for (String file : filesToBuild) {
            try {
                build(file, outputPath);
            } catch (Exception e) {
                System.out.println("Failed to build: " + file);
                System.out.println(e);
            }
        }
    }
}