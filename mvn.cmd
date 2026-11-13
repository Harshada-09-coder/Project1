@echo off
set "MAVEN_CMD=%~dp0apache-maven-3.9.6\bin\mvn.cmd"
if not exist "%MAVEN_CMD%" set "MAVEN_CMD=%~dp0..\apache-maven-3.9.6\bin\mvn.cmd"
call "%MAVEN_CMD%" %*

