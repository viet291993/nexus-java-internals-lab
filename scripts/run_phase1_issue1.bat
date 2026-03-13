@echo off
echo [Step 1] Tao thu muc output...
if not exist out mkdir out

echo [Step 2] Bien dich code...
javac -d out src\main\java\com\nexus\phase1\JitAnalysis.java

echo [Step 3] Chay voi co PrintCompilation...
echo ============================================================
echo QUAN TRONG: Quan sat cac dong log co dang:
echo "   123   1       3       com.nexus.phase1.JitAnalysis::isPrime (54 bytes)"
echo - Cot dau tien la thoi gian (ms) tu luc JVM start.
echo - Cot thu hai la ID cua lan compile.
echo - Cot thu ba la Attributes (vi du: n cho native, ! cho exception handler).
echo - Cot thu tu la Tier (Cap do compile: 1-4). Tier 4 thuong la C2 Compiler (toi uu nhat).
echo ============================================================
pause

java -XX:+PrintCompilation -cp out com.nexus.phase1.JitAnalysis

echo.
echo Da xong. Kiem tra log phia tren de thay method isPrime duoc compile sang ma may!
pause
