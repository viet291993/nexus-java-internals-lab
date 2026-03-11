@echo off
echo STARTING MEMORY LEAK DEMO...

if not exist out mkdir out

echo COMPILING...
javac -d out src\main\java\com\nexus\phase2\MemoryLeakDemo.java

if %ERRORLEVEL% NEQ 0 (
    echo Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo RUNNING WITH 64MB LIMIT...
java -Xmx64m -cp out com.nexus.phase2.MemoryLeakDemo

echo.
echo Demo finished.
pause
