@echo off
echo ==========================================
echo Knight Tour Game Packaging Script
echo ==========================================

echo Cleaning and packaging with Maven...
call mvn clean package
if %errorlevel% neq 0 (
    echo Maven build failed!
    pause
    exit /b %errorlevel%
)

echo Creating input directory...
if exist target\jpackage-input rmdir /s /q target\jpackage-input
mkdir target\jpackage-input
copy target\knight-tour-game-1.0.0.jar target\jpackage-input\

echo Generating application image with jpackage...
if exist target\release rmdir /s /q target\release
jpackage ^
  --name KnightTourGame ^
  --input target/jpackage-input ^
  --main-jar knight-tour-game-1.0.0.jar ^
  --main-class com.knighttour.Launcher ^
  --type app-image ^
  --dest target/release ^
  --win-console ^
  --java-options "-Dfile.encoding=UTF-8"

if %errorlevel% neq 0 (
    echo jpackage failed!
    pause
    exit /b %errorlevel%
)

echo Packaging complete!
echo Output directory: target/release/KnightTourGame
echo You can zip this directory for distribution.

pause
