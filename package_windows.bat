@echo off
setlocal

set APP_NAME=KnightTourGame
set APP_VERSION=1.0.0
set MAIN_CLASS=com.knighttour.Main
set JAR_PATH=target\knight-tour-game-1.0.0.jar
set INPUT_DIR=target
set OUTPUT_DIR=dist

echo Cleaning previous build...
rmdir /s /q %OUTPUT_DIR% 2>nul
mkdir %OUTPUT_DIR%

echo Checking for jpackage...
where jpackage >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: jpackage not found. Please ensure you are running JDK 14 or later.
    exit /b 1
)

echo Building project...
call mvn clean package
if %errorlevel% neq 0 (
    echo Error: Maven build failed.
    exit /b 1
)

echo Creating installer...
jpackage ^
  --name "%APP_NAME%" ^
  --input "%INPUT_DIR%" ^
  --main-jar "knight-tour-game-%APP_VERSION%.jar" ^
  --main-class "%MAIN_CLASS%" ^
  --type app-image ^
  --dest "%OUTPUT_DIR%" ^
  --win-dir-chooser ^
  --win-menu ^
  --win-shortcut ^
  --java-options "-Dfile.encoding=UTF-8"

if %errorlevel% neq 0 (
    echo Error: jpackage failed.
    exit /b 1
)

echo.
echo Build successful!
echo Application image created at: %OUTPUT_DIR%\%APP_NAME%
pause
