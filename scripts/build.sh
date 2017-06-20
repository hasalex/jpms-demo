#!/bin/bash
#JAVA_HOME=/opt/jdk9

[ -d target ] && rm -rf target/*
mkdir -p target/exploded
java_version=$($JAVA_HOME/bin/java -fullversion 2>&1 | cut -d '"' -f 2)

echo Compiled source code with JDK $java_version
$JAVA_HOME/bin/javac                           \
      -d target/exploded                       \
      -cp dep/vertx.jar:dep/netty.jar:         \
      -sourcepath src/main/java/               \
      -XDignore.symbol.file                    \
      $(find src/main/java/ -name "*.java")
cp -r src/main/resources/* target/exploded/

$JAVA_HOME/bin/jar                                 \
    cfe target/img.jar fr.sw.img.web.MainVerticle  \
    -C target/exploded .
#----
#for dir in target/exploded/*; do
#  if [ -d $dir ]; then
#    $JAVA_HOME/bin/jar                                   \
#        --create                                         \
#        --file target/artifact/$(basename $dir).jar \
#        -C $dir .
#  fi
#done
echo Jar file ready in target/artifact
