@echo off
setlocal
:menu
cls
echo ============================================================
echo           NEXUS JAVA INTERNALS LAB - CENTRAL LAUNCHER
echo ============================================================
echo.
echo [PHASE 1: JVM ARCHITECTURE]
echo   1. Issue #1: JIT Compilation Analysis
echo   2. Issue #2: Custom ClassLoader ^& Bytecode
echo.
echo [PHASE 2: MEMORY MANAGEMENT]
echo   3. Issue #3: Memory Leak ^& OutOfMemoryError
echo   4. Issue #4: GC Comparison (G1 vs ZGC)
echo.
echo [PHASE 3: CONCURRENCY]
echo   5. Issue #5: Race Condition ^& Atomic
echo   6. Issue #6: Custom ThreadPool Implementation
echo   7. Issue #7: Virtual Threads Scalability
echo.
echo [PHASE 4: PROFILING ^& DEBUGGING]
echo   8. Issue #8: Deadlock Analysis ^& Thread Dump
echo   9. Issue #9: JFR Performance Profiling
echo.
echo [ADVANCED: PHASE 5 - HIGH PERFORMANCE I/O]
echo   10. Issue #10: NIO Selector Echo Server
echo   11. Issue #11: Zero-copy File Transfer
echo.
echo [ADVANCED: PHASE 6 - LOW-LEVEL ^& CPU]
echo   12. Issue #12: False Sharing ^& CPU Cache
echo   13. Issue #13: JIT Method Inlining
echo   14. Issue #14: Project Panama / JNI
echo.
echo [ADVANCED: PHASE 7 - MEMORY MANAGER]
echo   15. Issue #15: Off-heap Memory Manager
echo.
echo [OTHER]
echo   0. Exit
echo.
echo ============================================================
set /p choice="Chon so de chay (0-15): "

if "%choice%"=="1" call "scripts\run_phase1_issue1.bat"
if "%choice%"=="2" call "scripts\run_phase1_issue2.bat"
if "%choice%"=="3" call "scripts\run_phase2_issue3.bat"
if "%choice%"=="4" call "scripts\run_phase2_issue4.bat"
if "%choice%"=="5" call "scripts\run_phase3_issue5.bat"
if "%choice%"=="6" call "scripts\run_phase3_issue6.bat"
if "%choice%"=="7" call "scripts\run_phase3_issue7.bat"
if "%choice%"=="8" call "scripts\run_phase4_issue8.bat"
if "%choice%"=="9" call "scripts\run_phase4_issue9.bat"

:: Placeholders for future phases
if "%choice%"=="10" echo [INFO] Issue #10 đang được phát triển... ^& pause
if "%choice%"=="11" echo [INFO] Issue #11 đang được phát triển... ^& pause
if "%choice%"=="12" echo [INFO] Issue #12 đang được phát triển... ^& pause
if "%choice%"=="13" echo [INFO] Issue #13 đang được phát triển... ^& pause
if "%choice%"=="14" echo [INFO] Issue #14 đang được phát triển... ^& pause
if "%choice%"=="15" echo [INFO] Issue #15 đang được phát triển... ^& pause

if "%choice%"=="0" goto end

echo.
echo Bam phim bat ky de quay lai menu...
pause > nul
goto menu

:end
endlocal
exit /b
