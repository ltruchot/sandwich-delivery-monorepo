#!/bin/bash
set -e

echo "Installing Java..."
# Install Java 21 on Ubuntu/Debian (Render's environment)
apt-get update
apt-get install -y openjdk-21-jdk

# Set JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "Java version:"
java -version

echo "Building Spring Boot application..."
cd /opt/render/project/src/apps/spring-boot-api
./gradlew build --no-daemon

echo "Build completed!"