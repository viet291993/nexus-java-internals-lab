@echo off
echo STARTING ISSUE 2...

if exist out_app rd /s /q out_app
if exist resources\plugins\out rd /s /q resources\plugins\out
mkdir out_app
mkdir resources\plugins\out

echo COMPILING MAIN APP...
javac -d out_app src\main\java\com\nexus\phase1\Plugin.java src\main\java\com\nexus\phase1\CustomClassLoader.java src\main\java\com\nexus\phase1\PluginLauncher.java

echo COMPILING PLUGIN...
javac -cp out_app -d resources\plugins\out resources\plugins\SecretPlugin.java

echo RUNNING LAUNCHER...
java -cp out_app com.nexus.phase1.PluginLauncher

echo ANALYZING BYTECODE...
javap -c resources\plugins\out\com\nexus\phase1\SecretPlugin.class

pause
