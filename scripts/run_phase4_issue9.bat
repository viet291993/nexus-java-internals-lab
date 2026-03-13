@echo off
setlocal

set APP_PATH=f:\nexus-java-internals-lab
set SRC_PATH=%APP_PATH%\src\main\java
set OUT_PATH=%APP_PATH%\out
set MAIN_CLASS=com.nexus.phase4.profiling.BottleneckDemo
set JFR_FILE=%APP_PATH%\docs\performance_recording.jfr

echo [INFO] Compiling BottleneckDemo...
if not exist "%OUT_PATH%" mkdir "%OUT_PATH%"

javac -d "%OUT_PATH%" "%SRC_PATH%\com\nexus\phase4\profiling\BottleneckDemo.java"

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %errorlevel%
)

echo [INFO] Starting application with JFR...
echo ---------------------------------------------------
echo [INSTRUCTIONS]
echo 1. Chuong trinh se tu dong ghi lai JFR.
echo 2. Hay de chuong trinh chay trong khoang 30 giay den 1 phut.
echo 3. Nhan Ctrl+C de dung lai.
echo 4. Sau do mo file %JFR_FILE% bang Java Mission Control (JMC).
echo ---------------------------------------------------

java -XX:StartFlightRecording=duration=60s,filename="%JFR_FILE%" -cp "%OUT_PATH%" %MAIN_CLASS%

echo ---------------------------------------------------
echo [INFO] JFR recording saved to: %JFR_FILE%
echo [TIP] Hay dung JMC de tim Hot Methods (CPU) va Allocation Pressure (Memory).

pause
endlocal
