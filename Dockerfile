# Multi-stage build for optimized production image
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build
COPY . .

RUN apk add --no-cache maven && \
    mvn clean package -DskipTests

# Production stage
FROM eclipse-temurin:21-jre-alpine

RUN apk add --no-cache wget && \
    addgroup -g 1000 -S quarkus && \
    adduser -u 1000 -S quarkus -G quarkus

WORKDIR /deployments

COPY --from=builder --chown=quarkus:quarkus /build/bootstrap/target/quarkus-app/lib/ ./lib/
COPY --from=builder --chown=quarkus:quarkus /build/bootstrap/target/quarkus-app/*.jar ./
COPY --from=builder --chown=quarkus:quarkus /build/bootstrap/target/quarkus-app/app/ ./app/
COPY --from=builder --chown=quarkus:quarkus /build/bootstrap/target/quarkus-app/quarkus/ ./quarkus/

RUN echo '#!/bin/sh' > entrypoint.sh && \
    echo 'exec java $JAVA_OPTS -jar app-runner.jar' >> entrypoint.sh && \
    chmod +x entrypoint.sh

USER quarkus

HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/q/health || exit 1

EXPOSE 8080

ENV QUARKUS_HTTP_HOST=0.0.0.0

ENTRYPOINT ["./entrypoint.sh"]
