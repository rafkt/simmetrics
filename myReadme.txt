For compiling everything from scratch (needed for changes in src core etc.) use:

mvn clean install

For simply compiling examples use: mvn (clean) compile

For running a test class with a main use: mvn exec:java -Dexec.mainClass=org.simmetrics.example.ReadMeExample -f pom.xml

For getting dependencies: (didn't work)
mvn install dependency:copy-dependencies

For dependencies (it worked):

1) added this to pom.xml under main <build>:

		<plugin>
            <artifactId>maven-assembly-plugin</artifactId>
            <configuration>
                <archive>
                    <manifest>
                        <mainClass>
                            com.test.your.main.class.goes.Here
                        </mainClass>
                    </manifest>
                </archive>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
        </plugin>

2) run with:

mvn clean compile assembly:single

3) then:


javac -cp ".:simmetrics-core-4.1.1-jar-with-dependencies.jar" *.java
java -cp ".:simmetrics-core-4.1.1-jar-with-dependencies.jar" *.java
