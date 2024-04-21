FROM eclipse-temurin:21-jdk-alpine

RUN addgroup -S app -g 1001 \
    && adduser -S app -u 1001 -G app \
    && mkdir -p /opt/app \
    && chmod -R 0700 /opt/app \
    && chown -R app:app /opt/app

VOLUME /tmp

USER app
WORKDIR /opt/app

ENV JAVA_OPTS="-XX:MinRAMPercentage=80 -XX:MaxRAMPercentage=80 -XX:InitialRAMPercentage=80 -XshowSettings:vm"

COPY target/todo-service*.jar .

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar *.jar ${0} ${@}"]
