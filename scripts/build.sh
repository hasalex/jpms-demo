#!/bin/bash
JAVA_HOME=/opt/jdk9

[ -d target ] && rm -rf target/*
mkdir -p target/exploded
mkdir -p target/artifact
java_version=$($JAVA_HOME/bin/java -fullversion 2>&1 | cut -d '"' -f 2)

echo Compiled source code with JDK $java_version
$JAVA_HOME/bin/javac                           \
      -d target/exploded                       \
      -sourcepath src/main/java/               \
      -XDignore.symbol.file                    \
      $(find src/main/java/ -name "*.java")
cp -r src/main/resources/* target/exploded/

$JAVA_HOME/bin/jar                             \
    cfe target/artifact/img.jar fr.sw.img.Main \
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
