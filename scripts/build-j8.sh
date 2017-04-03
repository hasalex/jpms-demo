#!/bin/bash

rm -r target/main/exploded/*
javac -d target/main/exploded               \
      -sourcepath src/main/java/            \
      -XDignore.symbol.file                 \
      $(find src/main/java/ -name "*.java")
cp -r src/main/resources/* target/main/exploded/

jar cfe target/main/artifact/img.jar fr.sw.img.Main \
    -C target/main/exploded .
