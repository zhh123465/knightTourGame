# Build script for Knight Tour Game

# 1. Clean and Package
Write-Host "Building project with Maven..."
mvn clean package
if ($LASTEXITCODE -ne 0) {
    Write-Error "Maven build failed!"
    exit 1
}

# 2. Create Dist Directory
$distDir = "dist"
if (Test-Path $distDir) {
    Remove-Item -Recurse -Force $distDir
}
New-Item -ItemType Directory -Force -Path "$distDir/app" | Out-Null
New-Item -ItemType Directory -Force -Path "$distDir/installer" | Out-Null

# 3. Create App Image (Green/Portable Version)
Write-Host "Creating App Image (Portable)..."
jpackage --type app-image `
    --name "KnightTourGame" `
    --input target `
    --main-jar knight-tour-game-1.0.0.jar `
    --main-class com.knighttour.Launcher `
    --dest "$distDir/app" `
    --java-options "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED --add-opens=javafx.base/javafx.util=ALL-UNNAMED" `
    --vendor "zhh123465" `
    --app-version "1.0.0" `
    --icon "src/main/resources/icon.ico" 

if ($LASTEXITCODE -ne 0) {
    Write-Warning "App Image creation failed! Checking if icon exists..."
    # Retry without icon if failed (icon might be missing)
    jpackage --type app-image `
        --name "KnightTourGame" `
        --input target `
        --main-jar knight-tour-game-1.0.0.jar `
        --main-class com.knighttour.Launcher `
        --dest "$distDir/app" `
        --java-options "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED --add-opens=javafx.base/javafx.util=ALL-UNNAMED" `
        --vendor "zhh123465" `
        --app-version "1.0.0"
}

# 4. Create Installer (EXE)
Write-Host "Creating Installer (EXE)..."
jpackage --type exe `
    --name "KnightTourGame" `
    --input target `
    --main-jar knight-tour-game-1.0.0.jar `
    --main-class com.knighttour.Launcher `
    --dest "$distDir/installer" `
    --win-shortcut `
    --win-menu `
    --win-dir-chooser `
    --java-options "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED --add-opens=javafx.base/javafx.util=ALL-UNNAMED" `
    --vendor "zhh123465" `
    --app-version "1.0.0" `
    --icon "src/main/resources/icon.ico"

if ($LASTEXITCODE -ne 0) {
    Write-Warning "Installer creation failed! Retry without icon..."
    jpackage --type exe `
        --name "KnightTourGame" `
        --input target `
        --main-jar knight-tour-game-1.0.0.jar `
        --main-class com.knighttour.Launcher `
        --dest "$distDir/installer" `
        --win-shortcut `
        --win-menu `
        --win-dir-chooser `
        --java-options "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED --add-opens=javafx.base/javafx.util=ALL-UNNAMED" `
        --vendor "zhh123465" `
        --app-version "1.0.0"
}

Write-Host "Build Complete!"
Write-Host "Portable App: $distDir/app/KnightTourGame"
Write-Host "Installer: $distDir/installer"
