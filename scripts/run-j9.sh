#!/bin/bash
JAVA_HOME=/opt/jdk9

$JAVA_HOME/bin/java --module-path target/main/artifact \
        -m image.show/fr.sw.img.Main
