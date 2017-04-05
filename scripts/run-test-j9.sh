#!/bin/bash
JAVA_HOME=/opt/jdk9

$JAVA_HOME/bin/java --module-path target/main/artifact:dependencies \
        --class-path dependencies/slf4j-simple-1.7.25.jar:dependencies/hamcrest-core-1.3.jar \
        --patch-module image.inmem=target/test/exploded/ \
        --add-modules image.inmem \
        --add-exports image.inmem/fr.sw.img.inmemory=junit \
        --add-reads image.inmem=junit \
        -m junit/org.junit.runner.JUnitCore fr.sw.img.inmemory.ImageInMemoryTest
