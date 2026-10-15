@echo off
setlocal
title VyparOS Launcher

set "PROJECT_DIR=%~dp0"

echo ==========================================
echo      VyparOS - Business Operating System
echo ==========================================
echo.

echo Checking project files...

if not exist "%PROJECT_DIR%backend\pom.xml" (
    echo ERROR: Backend pom.xml not found.
    pause
    exit /b 1
)

if not exist "%PROJECT_DIR%frontend\index.html" (
    echo ERROR: Frontend index.html not found.
    pause
    exit /b 1
)

echo Starting VyparOS backend...
start "VyparOS Backend" cmd /k "cd /d "%PROJECT_DIR%backend" && mvn spring-boot:run"

echo.
echo Waiting for backend initialization...
timeout /t 15 /nobreak >nul

echo Starting VyparOS frontend...
start "VyparOS Frontend" cmd /k "cd /d "%PROJECT_DIR%" && python -m http.server 3000 --directory "%PROJECT_DIR%frontend""

echo.
echo Opening VyparOS...
timeout /t 3 /nobreak >nul
start "" "http://localhost:3000"

echo.
echo VyparOS launcher finished.
echo Backend:  http://localhost:8080
echo Frontend: http://localhost:3000
echo.
echo Keep the backend and frontend windows open while using VyparOS.
pause