#!/bin/bash
JAVA_HOME=/opt/jdk9

$JAVA_HOME/bin/java --module-path target/main/artifact:dependencies \
        --class-path dependencies/slf4j-simple-1.7.25.jar \
        -m image.show/fr.sw.img.Main
