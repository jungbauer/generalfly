#!/bin/bash

echo "Starting app image build..."

echo "Gradle clean..."
./gradlew clean

echo "Gradle build..."
./gradlew build

echo "Docker build..."
docker build -t generalfly:latest .