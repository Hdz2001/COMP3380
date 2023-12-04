
build: SQLServerProject.class

SQLServerProject.class: SQLServerProject.java
	javac SQLServerProject.java

run: SQLServerProject.class
	java -cp .:mssql-jdbc-11.2.0.jre11.jar SQLServerProject

clean:
	rm SQLServerProject.class
