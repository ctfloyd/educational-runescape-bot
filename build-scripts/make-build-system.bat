@echo off
set JAVA8_PATH=C:\Users\caleb\.jdks\corretto-1.8.0_302\bin
set JAVAC=%JAVA8_PATH%\javac
set JAR=%JAVA8_PATH%\jar

mkdir tmp
cd src
%JAVAC% -d ../tmp com\educationalbotters\build\*.java
cd ../tmp
%JAR% cvmf ..\src\com\educationalbotters\build\manifest.mf ..\build-scripts\Build.jar com\educationalbotters\build\*.class
cd ..
rd /s /q tmp
