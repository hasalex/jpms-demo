#!/bin/bash
#JAVA_HOME=/opt/jdk9

java_version=$($JAVA_HOME/bin/java -fullversion 2>&1 | cut -d '"' -f 2)

echo Running application with JDK $java_version
$JAVA_HOME/bin/java -cp target/artifact/img.jar fr.sw.img.Main
#$JAVA_HOME/bin/java                 \
#    --module-path target/artifact   \
#    -m image.main/fr.sw.img.Main
