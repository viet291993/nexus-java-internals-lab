@echo off
setlocal
echo [Phase 5] Global Cleanup...
:: Kill any previous java processes on ports 7777-7800 and 9999-10010
for /L %%p in (7777,1,7787) do (
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%%p ^| findstr LISTENING') do taskkill /F /PID %%a 2>nul
)
for /L %%p in (9999,1,10010) do (
    for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%%p ^| findstr LISTENING') do taskkill /F /PID %%a 2>nul
)

echo [Phase 5] Preparing Output Directory...
if not exist out mkdir out
if exist out\server.port del out\server.port

echo [Phase 5] Compiling NIO and BIO sources...
javac -d out src\main\java\com\nexus\phase5\io\BioEchoServer.java src\main\java\com\nexus\phase5\io\BioLoadTester.java src\main\java\com\nexus\phase5\io\NioEchoServer.java src\main\java\com\nexus\phase5\io\NioLoadTester.java

if %ERRORLEVEL% neq 0 (
    echo [ERROR] Compilation failed!
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo ============================================================
echo   BIO vs NIO Comparison Lab
echo ============================================================
echo   1. BIO (Blocking I/O): Auto-Port Discovery (Starts at 7777)
echo   2. NIO (Non-blocking): Auto-Port Discovery (Starts at 9999)
echo   [Note] Server windows will close automatically on Exit.
echo ============================================================
echo.
set /p server_type="Select Server Type (1: BIO, 2: NIO): "

if "%server_type%"=="1" goto run_bio
if "%server_type%"=="2" goto run_nio
goto end_script

:run_bio
echo [INFO] Starting BioEchoServer...
start "BioEchoServer-BIO" cmd /c "java -cp out com.nexus.phase5.io.BioEchoServer"

echo.
echo Waiting for server to initialize...
timeout /t 2 > nul

set /p run_bio_client="Run BIO Load Tester (50 threads)? (y/n): "
if /i "%run_bio_client%"=="y" (
    java -cp out com.nexus.phase5.io.BioLoadTester
)
echo.
echo [HINT] To close the server window, press Ctrl+C in that window or close it.
goto end_script

:run_nio
echo [INFO] Starting NioEchoServer...
start "NioEchoServer-NIO" cmd /c "java -cp out com.nexus.phase5.io.NioEchoServer"

echo.
echo Waiting for server to initialize...
timeout /t 2 > nul

set /p run_client="Run NIO Load Tester now? (y/n): "
if /i "%run_client%"=="y" (
    echo [INFO] Running NioLoadTester with 100 connections...
    java -cp out com.nexus.phase5.io.NioLoadTester
)
echo.
echo [HINT] To close the server window, press Ctrl+C in that window or close it.
goto end_script

:end_script
echo.
echo Operation finished.
pause
exit /b
