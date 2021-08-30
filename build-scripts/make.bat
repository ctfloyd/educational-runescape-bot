@echo off
set JAVA8_PATH=C:\Users\caleb\.jdks\corretto-1.8.0_302\bin
set JAVA8=%JAVA8_PATH%\java

%JAVA8% -jar build-scripts\Build.jar --all --output %userprofile%\OSBot\Scripts --include EducationalBottersApi
