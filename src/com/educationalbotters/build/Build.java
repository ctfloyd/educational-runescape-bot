package com.educationalbotters.build;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

public class Build {

    public static String SCRIPT_SOURCE = ".\\src\\com\\educationalbotters\\customscripts";
    public static String SCRIPT_OUTPUT_SOURCE = ".\\out\\production\\educational-runescape-bot\\com\\educationalbotters\\customscripts";
    public static String SRC_ROOT = ".\\src\\com\\educationalbotters";
    public static String OUTPUT_SRC_ROOT = ".\\out\\production\\educational-runescape-bot";

    public static List<String> gatherAllScriptNames() {
        return Arrays.asList(new File(SCRIPT_SOURCE).list());
    }

    public static Set<String> getAllFilesInDirectoryEndingWith(String basePath, String endingWith) {
        File currentDirectory = new File(basePath);
        if (currentDirectory == null) {
            return Collections.emptySet();
        }

        Set<String> suffixFiles = new HashSet<>();
        String[] suffixFilesInThisDirectory = currentDirectory.list((dir, name) -> {
            String nextPath = dir.getPath() + File.separator + name;

            if (new File(nextPath).isDirectory()) {
                suffixFiles.addAll(getAllFilesInDirectoryEndingWith(nextPath, endingWith));
                return false;
            }

            return name.endsWith(endingWith);
        });

        if(suffixFilesInThisDirectory != null) {
            Set<String> paths = Arrays.stream(suffixFilesInThisDirectory)
                    .map(file -> basePath + File.separator + file)
                    .collect(Collectors.toSet());

            suffixFiles.addAll(paths);
        }

        return suffixFiles;
    }

    private static String modifySourceStringToCompileString(String inputFiles) {
        return inputFiles
                .replaceAll(".java", ".class")
                .replaceAll("\\./src/", "")
                .replaceAll("\\.\\\\src\\\\", "")
                .replaceAll("\\.\\\\out\\\\production\\\\educational-runescape-bot\\\\", "");
    }


    public static Set<String> compileDirectory(String directory) throws IOException, InterruptedException {
       Set<String> javaInputFiles = getAllFilesInDirectoryEndingWith(directory, ".java");
       File output = new File(OUTPUT_SRC_ROOT);
       if(!output.exists()) {
           output.mkdirs();
       }

        ArrayList<String> compile = new ArrayList<>();
        compile.add("javac");
        compile.add("-cp");
        compile.add("dependencies\\OSBotApi.jar");
        compile.add("-d");
        compile.add(OUTPUT_SRC_ROOT);
        for(String value : javaInputFiles) {
            compile.add(value);
        }
        ProcessBuilder compileProcessBuilder = new ProcessBuilder(compile);
        compileProcessBuilder.redirectErrorStream(true);
        Process compileProcess = compileProcessBuilder.start();
        InputStream is = compileProcess.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        System.out.println("===========" + Paths.get(directory).getFileName().toString() + " COMPILE OUTPUT" + "===============");
        System.out.println(compileProcessBuilder.command());
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        compileProcess.waitFor();
        System.out.println("==========================");

        // Scan directory for all files after compiling, since compiling can create additional files
        return getAllFilesInDirectoryEndingWith(directory.replace(".\\src", OUTPUT_SRC_ROOT), ".class");
    }

    public static void buildScript(String script, String userOutput, Set<String> includes) throws IOException, InterruptedException {
        Set<String> javaInputFiles = getAllFilesInDirectoryEndingWith(SCRIPT_SOURCE + File.separator + script, ".java");

        if(javaInputFiles.isEmpty()) {
            return;
        }

        File outputDir = new File(userOutput);
        if(!outputDir.exists()) {
            outputDir.mkdirs();
        }

        File scriptOutputDir = new File(OUTPUT_SRC_ROOT);
        if(!scriptOutputDir.exists()) {
            scriptOutputDir.mkdirs();
        }

        ArrayList<String> compile = new ArrayList<>();
        compile.add("javac");
        compile.add("-cp");
        StringBuilder classPath = new StringBuilder();
        classPath.append("dependencies\\OSBotApi.jar");
        Set<String> includedPaths = new HashSet<>();
        for(String include : includes) {
            if(include == null) {
                continue;
            }

            Path currentPath = Paths.get(include);
            while(currentPath != null && !currentPath.getFileName().toString().equals("com")) {
                currentPath = currentPath.getParent();
            }

            if(currentPath == null) {
                System.out.println("Could not find 'com' directory for path: " + include);
                continue;
            }

            if(!includedPaths.contains(currentPath.getParent().toAbsolutePath().toString())) {
                String cPath = currentPath.getParent().toAbsolutePath().toString();
                classPath.append(";");
                classPath.append(cPath);
                includedPaths.add(cPath);
            }
        }
        compile.add(classPath.toString());
        compile.add("-d");
        compile.add(OUTPUT_SRC_ROOT);
        for(String value : javaInputFiles) {
           compile.add(value);
        }
        ProcessBuilder compileProcessBuilder = new ProcessBuilder(compile);
        compileProcessBuilder.redirectErrorStream(true);
        Process compileProcess = compileProcessBuilder.start();
        InputStream is = compileProcess.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        System.out.println("===========" + script + " COMPILE OUTPUT" + "===============");
        System.out.println(compileProcessBuilder.command());
        String line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        compileProcess.waitFor();
        System.out.println("==========================");


        Set<String> allFilesWithIncludes = new HashSet<>();
        allFilesWithIncludes.addAll(getAllFilesInDirectoryEndingWith(SCRIPT_OUTPUT_SOURCE + File.separator + script, ".class"));
        allFilesWithIncludes.addAll(includes);
        allFilesWithIncludes = allFilesWithIncludes.stream().map(Build::modifySourceStringToCompileString).collect(Collectors.toSet());

        ArrayList<String> build = new ArrayList<>();
        build.add("jar");
        build.add("cvf");
        build.add(script + ".jar");
        for(String value : allFilesWithIncludes) {
            build.add(value);
        }
        ProcessBuilder buildProcessBuilder = new ProcessBuilder(build);
        buildProcessBuilder.directory(new File(OUTPUT_SRC_ROOT));
        compileProcessBuilder.redirectErrorStream(true);
        Process buildProcess = buildProcessBuilder.start();
        is = buildProcess.getInputStream();
        reader = new BufferedReader(new InputStreamReader(is));

        System.out.println("===========" + script + " PACKAGE OUTPUT" + "===============");
        System.out.println("Command: " + buildProcessBuilder.command());
        line = null;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println("==========================");
        buildProcess.waitFor();

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

        Set<String> includedClassFiles = new HashSet<>();
        for(String include : filesToIncludeInPackage) {
            includedClassFiles.addAll(
                    compileDirectory(SRC_ROOT + File.separator + include)
                            .stream().map(Build::modifySourceStringToCompileString)
                            .map(s -> OUTPUT_SRC_ROOT + File.separator + s)
                            .collect(Collectors.toSet())
            );
        }
        System.out.println("Included class files: " + includedClassFiles);

        boolean hasError = false;
        for (String file : filesToBuild) {
            try {
                buildScript(file, outputPath, includedClassFiles);
            } catch (Exception e) {
                System.out.println("==========ERROR=========");
                System.out.println("Failed to build: " + file);
                System.out.println(e);
                System.out.println("=======================");
                hasError = true;
            }
        }

        // Let application caller know there were errors.
        if(hasError) {
            System.exit(-1);
        }
    }
}