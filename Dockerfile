FROM openjdk:21-jdk-alpine
WORKDIR /app
COPY . /app/
RUN mkdir -p classes && \
    javac -d classes src/main/java/arep1/taller1/*.java && \
    echo "Main-Class: arep1.taller1.ArimaKinen" > manifest.txt && \
    cd classes && \
    jar cvfm ArimaKinen.jar ../manifest.txt arep1/taller1/*.class

FROM openjdk:21-jre-alpine
WORKDIR /
COPY --from=0 /app/classes/ArimaKinen.jar /ArimaKinen.jar
ENTRYPOINT ["java", "-Xmx256m", "-jar", "/ArimaKinen.jar"]
EXPOSE 5000