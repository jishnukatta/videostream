# Build Stage
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Run Stage (FFmpeg installed)
FROM eclipse-temurin:17-jre
WORKDIR /app

# Install ffmpeg
RUN apt-get update && apt-get install -y ffmpeg

# Copy the packaged jar file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Set the environment variable for ffmpeg
ENV FFMPEG_PATH="/usr/bin/ffmpeg"

ENTRYPOINT ["java", "-jar", "app.jar"]
