@echo off
setlocal

set APP_PATH=f:\nexus-java-internals-lab
set SRC_PATH=%APP_PATH%\src\main\java
set OUT_PATH=%APP_PATH%\out
set MAIN_CLASS=com.nexus.phase3.threadpool.ThreadPoolDemo

echo [INFO] Compiling CustomThreadPool...
if not exist "%OUT_PATH%" mkdir "%OUT_PATH%"

pushd "%SRC_PATH%\com\nexus\phase3\threadpool"
javac -d "%OUT_PATH%" *.java
popd

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

echo [INFO] Running ThreadPoolDemo...
echo ---------------------------------------------------
java -cp "%OUT_PATH%" %MAIN_CLASS%
echo ---------------------------------------------------
echo [INFO] Execution finished.

pause
endlocal
