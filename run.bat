@echo off
echo Compiling Java files...
javac -cp "src/main/resources;lib/*" -d target/classes src/main/java/com/examly/springapp/*.java src/main/java/com/examly/springapp/*/*.java

echo Starting application...
java -cp "target/classes;lib/*" -Dspring.profiles.active=default com.examly.springapp.SpringappApplication
pause