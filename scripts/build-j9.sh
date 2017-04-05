#!/bin/bash
JAVA_HOME=/opt/jdk9

rm -r target/main/exploded/*
$JAVA_HOME/bin/javac -d target/main/exploded \
      --module-source-path src/main/java/            \
      $(find src/main/java/ -name "*.java")
cp -r src/main/resources/* target/main/exploded/image.show/

for dir in target/main/exploded/*; do
  if [ -d $dir ]; then
    $JAVA_HOME/bin/jar --create \
        --file target/main/artifact/$(basename $dir).jar \
        -C $dir .
  fi
done