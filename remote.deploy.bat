@echo off
setlocal EnableDelayedExpansion

:: === CONFIGURA QUI ===
set PLUGIN_DIR=C:\Users\kliti\Desktop\h-y-tale-vote\
set BUILD_CMD=gradlew build
set JAR_NAME=h-y-tale-vote.jar
set BUILD_OUTPUT=%PLUGIN_DIR%\build\libs\%JAR_NAME%
set SERVER_USER=ubuntu
set SERVER_IP=57.128.252.159
set REMOTE_DIR=/home/PvPShield_Hytale_server/Server/mods
set RESTART_CMD=set RESTART_CMD=screen -S hytale -p 0 -X stuff "stop\n" && sleep 7 && screen -S hytale -X quit && sleep 5 && cd /home/PvPShield_Hytale_server && screen -dmS hytale ./start.sh
:: === SCRIPT ===
cd /d "%PLUGIN_DIR%"

echo Building plugin...
call %BUILD_CMD%

if %ERRORLEVEL% neq 0 (
    echo Build fallito!
    pause
    exit /b 1
)

if not exist "%BUILD_OUTPUT%" (
    echo JAR non trovato: %BUILD_OUTPUT%
    pause
    exit /b 1
)

echo Deploy JAR...
scp "%BUILD_OUTPUT%" %SERVER_USER%@%SERVER_IP%:%REMOTE_DIR%/%JAR_NAME%

if %ERRORLEVEL% neq 0 (
    echo SCP fallito!
    pause
    exit /b 1
)

echo Start server...
ssh %SERVER_USER%@%SERVER_IP% "cd /home/PvPShield_Hytale_server && ./start.sh"

echo Deploy completato!
pause