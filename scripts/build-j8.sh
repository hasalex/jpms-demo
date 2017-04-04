#!/bin/bash

rm -rf target/main/exploded/*
javac -d target/main/exploded               \
      -cp dependencies/vertx-core-3.3.3.jar:dependencies/vertx-internal-3.3.3.jar:dependencies/vertx-web-3.3.3.jar \
      -sourcepath src/main/java/            \
      -XDignore.symbol.file                 \
      $(find src/main/java/ -name "*.java")
cp -r src/main/resources/* target/main/exploded/

rm -rf target/main/artifact/*
jar cfe target/main/artifact/img.jar fr.sw.img.Main \
    -C target/main/exploded .
