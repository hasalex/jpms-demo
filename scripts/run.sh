#!/bin/bash
#JAVA_HOME=/opt/jdk9

java_version=$($JAVA_HOME/bin/java -fullversion 2>&1 | cut -d '"' -f 2)

echo Running application with JDK $java_version
$JAVA_HOME/bin/java \
        -classpath "target/img.jar:dep/vertx.jar:dep/netty.jar:dep/h2.jar" \
        fr.sw.img.web.MainVerticle
