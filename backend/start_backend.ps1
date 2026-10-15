$env:JAVA_HOME = "C:\Users\aditya\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.10.v20260205-0638\jre"
$env:PATH = "$env:JAVA_HOME\bin;C:\Users\aditya\.maven\apache-maven-3.9.9\bin;$env:PATH"

$mvnCmd = "C:\Users\aditya\.maven\apache-maven-3.9.9\bin\mvn.cmd"
$backendDir = "C:\Users\aditya\OneDrive\Desktop\vypaar\backend"

Start-Process -FilePath $mvnCmd -ArgumentList "spring-boot:run" -WorkingDirectory $backendDir -WindowStyle Hidden
Write-Host "Spring Boot backend launch initiated."
