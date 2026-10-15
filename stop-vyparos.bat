@echo off
title Stop VyaparOS
echo =====================================================================
echo                Stopping VyaparOS Services
echo =====================================================================
echo.

powershell -NoProfile -ExecutionPolicy Bypass -Command "& {
    Write-Host 'Checking for active VyaparOS processes...' -ForegroundColor Cyan
    
    # 1. Check Port 3000 (Frontend)
    $p3000 = Get-NetTCPConnection -LocalPort 3000 -State Listen -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess -Unique
    if ($p3000) {
        foreach ($pidToKill in $p3000) {
            Write-Host ('Stopping Frontend server (PID: {0})...' -f $pidToKill) -ForegroundColor Yellow
            Stop-Process -Id $pidToKill -Force -ErrorAction SilentlyContinue
        }
    } else {
        Write-Host 'No Frontend server running on port 3000.' -ForegroundColor Gray
    }

    # 2. Check Port 8080 (Backend)
    $p8080 = Get-NetTCPConnection -LocalPort 8080 -State Listen -ErrorAction SilentlyContinue | Select-Object -ExpandProperty OwningProcess -Unique
    if ($p8080) {
        foreach ($pidToKill in $p8080) {
            $proc = Get-Process -Id $pidToKill -ErrorAction SilentlyContinue
            if ($proc.ProcessName -match 'java') {
                Write-Host ('Stopping Spring Boot Backend (PID: {0})...' -f $pidToKill) -ForegroundColor Yellow
                Stop-Process -Id $pidToKill -Force -ErrorAction SilentlyContinue
            }
        }
    } else {
        Write-Host 'No Backend server running on port 8080.' -ForegroundColor Gray
    }

    Write-Host 'VyaparOS services stopped.' -ForegroundColor Green
}"

echo.
timeout /t 3 >nul
exit /b 0
