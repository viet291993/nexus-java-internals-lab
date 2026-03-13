@echo off
echo STARTING GC COMPARISON...

if not exist out mkdir out
javac -d out src\main\java\com\nexus\phase2\GcPressureDemo.java

if not exist docs\gc_logs mkdir docs\gc_logs

echo RUNNING WITH G1GC...
java -Xms64m -Xmx64m -XX:+UseG1GC -Xlog:gc*:file=docs\gc_logs\g1gc.txt -cp out com.nexus.phase2.GcPressureDemo

echo RUNNING WITH ZGC...
java -Xms64m -Xmx64m -XX:+UseZGC -Xlog:gc*:file=docs\gc_logs\zgc.txt -cp out com.nexus.phase2.GcPressureDemo

echo.
echo GC COMPARISON FINISHED.
echo Logs are saved in docs/gc_logs/
pause
