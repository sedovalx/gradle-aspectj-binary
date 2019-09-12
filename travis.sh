#!/usr/bin/env bash

set -e

echo "include 'plugin'" > settings.gradle
./gradlew clean :plugin:publishMavenJavaPublicationToMavenLocal
echo "include 'plugin', 'examples'" > settings.gradle
./gradlew :examples:run
./gradlew bintrayUpload