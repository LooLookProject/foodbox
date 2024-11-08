# Use an official OpenJDK runtime as a parent image
FROM openjdk:21-jdk-slim

# Install tesseract-ocr and necessary packages
RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Download the Korean trained data for Tesseract
RUN wget -O /usr/share/tesseract-ocr/5/tessdata/kor.traineddata \
    https://github.com/tesseract-ocr/tessdata_best/raw/refs/heads/main/kor.traineddata

WORKDIR /app

# Copy the Spring Boot application JAR file into the container
COPY /build/libs/foodbox-0.0.1-SNAPSHOT.jar /app/foodbox.jar

# Expose the port that the application will run on
EXPOSE 80

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/foodbox.jar"]
