# VyaparOS Windows Desktop Shortcut Generator
param (
    [string]$LiveUrl = ""
)

$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$desktop = [Environment]::GetFolderPath("Desktop")
if (-not (Test-Path $desktop)) {
    if (Test-Path "$env:OneDrive\Desktop") {
        $desktop = "$env:OneDrive\Desktop"
    } elseif (Test-Path "$env:USERPROFILE\Desktop") {
        $desktop = "$env:USERPROFILE\Desktop"
    }
}

$iconPath = Join-Path $scriptDir "frontend\assets\brand\app-icon.ico"
$targetBatch = Join-Path $scriptDir "start-vyparos.bat"

Write-Host "Creating VyaparOS Desktop Shortcuts..." -ForegroundColor Cyan
Write-Host "Desktop Location: $desktop" -ForegroundColor Gray

# 1. Local App Launcher Shortcut
$wsh = New-Object -ComObject WScript.Shell
$shortcutPath = Join-Path $desktop "VyaparOS.lnk"
$shortcut = $wsh.CreateShortcut($shortcutPath)
$shortcut.TargetPath = $targetBatch
$shortcut.WorkingDirectory = $scriptDir
$shortcut.Description = "VyaparOS - One platform. Your entire business."
if (Test-Path $iconPath) {
    $shortcut.IconLocation = "$iconPath,0"
}
$shortcut.Save()

Write-Host " [SUCCESS] Created Local Launcher Shortcut: $shortcutPath" -ForegroundColor Green

# 2. Live Web Shortcut (if URL is passed)
if ($LiveUrl -ne "") {
    $liveShortcutPath = Join-Path $desktop "VyaparOS Live.url"
    $urlContent = "[InternetShortcut]`r`nURL=" + $LiveUrl + "`r`nIconFile=" + $iconPath + "`r`nIconIndex=0`r`n"
    [System.IO.File]::WriteAllText($liveShortcutPath, $urlContent)
    Write-Host " [SUCCESS] Created Live Website Shortcut: $liveShortcutPath -> $LiveUrl" -ForegroundColor Green
}

Write-Host "`nAll done! You can now launch VyaparOS directly from your Desktop." -ForegroundColor Cyan
