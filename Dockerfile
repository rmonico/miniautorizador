FROM openjdk:11-jre

RUN groupadd -r springapp && useradd --no-log-init -r -g springapp springapp

RUN mkdir /app

WORKDIR /app

COPY --chown=springapp:springapp target/miniautorizador-*.jar /app/application.jar

USER springapp

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "application.jar" ]
