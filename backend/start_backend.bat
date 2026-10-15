@echo off
set "JAVA_HOME=C:\Users\aditya\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.10.v20260205-0638\jre"
set "PATH=%JAVA_HOME%\bin;C:\Users\aditya\.maven\apache-maven-3.9.9\bin;%PATH%"
cd /d "C:\Users\aditya\OneDrive\Desktop\vypaar\backend"
mvn.cmd spring-boot:run
