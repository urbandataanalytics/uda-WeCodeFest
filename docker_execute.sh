docker run -it --rm --name wecode_workshop -v "$(pwd)/m2":/root/.m2 -v "$(pwd)":/usr/src/mymaven -w /usr/src/mymaven maven:3.6-jdk-8 mvn compile exec:java -Dexec.mainClass=$1 
