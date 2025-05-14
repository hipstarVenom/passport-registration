# passport-registration
using javafx and sqlite

download the package for javafx in the oracle java website .. keep the package file in the same directory of the project..
also download sqlite jar file from the chrome it can be any version .. keep the jar file in the same directory of the project..

compile:
javac --module-path "javafx-sdk-24.0.1\lib" --add-modules javafx.controls  -cp ".;sqlite-jdbc-3.47.1.0.jar;" *.java

run:
java --module-path "javafx-sdk-24.0.1\lib" --add-modules javafx.controls  -cp ".;sqlite-jdbc-3.47.1.0.jar;" 
Main
