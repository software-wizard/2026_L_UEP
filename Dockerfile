FROM tomcat:9.0-jdk11-openjdk-slim

RUN rm -rf /usr/local/tomcat/webapps/*

RUN mkdir -p /app/downloads

COPY game-server/target/game-server-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

COPY economy-gui/target/game-client.zip /app/downloads/game-client.zip

ENV JPDA_ADDRESS=*:5005
ENV JPDA_TRANSPORT=dt_socket

EXPOSE 8080 5005

CMD ["catalina.sh", "jpda", "run"]