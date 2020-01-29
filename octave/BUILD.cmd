@ECHO OFF
echo "Generating source code ..."
cd ./src/pkg/
java -classpath ../../javacc-7.0.5.jar javacc ./octave.jj
echo "Building classes ..."
cd ../../
javac -d ./bin ./src/pkg/*.java
echo "Press enter to exit ..."
pause