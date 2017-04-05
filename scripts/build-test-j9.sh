#!/bin/bash
JAVA_HOME=/opt/jdk9

rm -r target/test/exploded/*
$JAVA_HOME/bin/javac -d target/test/exploded \
      --module-path dependencies:target/main/artifact \
      --add-modules junit \
      --add-reads image.inmem=junit \
      --patch-module image.inmem=src/test/java/image.inmem \
      $(find src/test/java/ -name "*.java")
