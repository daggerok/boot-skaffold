FROM maven:3.6.1-jdk-13-alpine as builder
COPY ./pom.xml /tmp/
RUN mvn -f /tmp/ dependency:resolve dependency:resolve-plugins
COPY ./src/ /tmp/src/
RUN mvn -f /tmp/ -P !dev clean resources:resources spring-boot:build-info dependency:copy-dependencies test jar:jar spring-boot:repackage
#RUN apk add --no-cache tree && tree /tmp/ && ls -lah /tmp/target/

FROM maven:3.6.1-jdk-13-alpine
ENTRYPOINT java -cp /tmp/classes:/tmp/dependency/* com.github.daggerok.skaffold.boot.BootSkaffoldApplicationKt
CMD /bin/ash
HEALTHCHECK --interval=3s --timeout=3s --start-period=3s --retries=33 \
        CMD curl -f http://127.0.0.1:8080/actuator/health/ || exit 1
COPY --from=builder /tmp/target/dependency/ /tmp/dependency/
COPY --from=builder /tmp/target/classes/ /tmp/classes/
