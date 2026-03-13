@echo off
setlocal

set APP_PATH=f:\nexus-java-internals-lab
set SRC_PATH=%APP_PATH%\src\main\java
set OUT_PATH=%APP_PATH%\out
set MAIN_CLASS=com.nexus.phase4.deadlock.DeadlockDemo

echo [INFO] Compiling DeadlockDemo...
if not exist "%OUT_PATH%" mkdir "%OUT_PATH%"

pushd "%SRC_PATH%\com\nexus\phase4\deadlock"
javac -d "%OUT_PATH%" *.java
popd

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

echo [INFO] Running DeadlockDemo...
echo ---------------------------------------------------
echo [VUI LONG LUU Y] Chuong trinh se bi treo tai day do Deadlock.
echo Ban hay mo mot Terminal moi va go lenh sau:
echo 1. 'jps -l' - De tim PID cua chuong trinh.
echo 2. 'jstack <PID>' - De xem Thread Dump va tim loi Deadlock.
echo ---------------------------------------------------

java -cp "%OUT_PATH%" %MAIN_CLASS%

echo ---------------------------------------------------
echo [INFO] Execution finished.

pause
endlocal
