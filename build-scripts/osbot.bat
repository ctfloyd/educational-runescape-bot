@echo off
set JAVA8_PATH= %userprofile%\.jdks\corretto-1.8.0_302\bin
set JAVA8=%JAVA8_PATH%\java

%JAVA8% -jar ..\dependencies\OSBotApi.jar