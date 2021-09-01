@echo off
set JAVA8_PATH=%userprofile%\.jdks\corretto-1.8.0_302\bin
set JAVA8=%JAVA8_PATH%\java

del /f /q .\out
rd /s /q .\out
%JAVA8% -jar build-scripts\Build.jar --all --output %userprofile%\OSBot\Scripts --include educationalbottersapi
