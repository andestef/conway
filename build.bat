::remove next line to debug this batch file
@echo off
echo Hello from the Conway Plus Plus builder!
cd src
"../build/jdk/bin/javac" Conwayplusplus.java --module-path ../build/javafx/lib --add-modules javafx.controls -d ../build
cd ../build
del "conwayplusplus.exe"
"jdk/bin/jar" cfm Conwayplusplus.jar manifest.txt *.class
"launch4j/launch4jc" conf.xml
del "Conwayplusplus.class"
del "Conwayplusplus$1.class"
del "Conwayplusplus.jar"
conwayplusplus
cd ../