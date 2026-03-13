@echo off
REM Phase 3 - Issue 5: Race Condition Demo

if not exist out mkdir out

echo Compiling RaceConditionDemo...
javac -d out src\main\java\com\nexus\phase3\RaceConditionDemo.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo Running RaceConditionDemo...
java -cp out com.nexus.phase3.RaceConditionDemo

echo.
echo Demo finished successfully!
pause
