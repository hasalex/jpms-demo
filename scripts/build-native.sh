#!/bin/bash
JAVA_HOME=/opt/jdk9

rm -rf target/dist
$JAVA_HOME/bin/jlink  \
        --module-path $JAVA_HOME/jmods:target/artifact:dependencies \
        --add-modules image.main                   \
        --launcher img=image.main/fr.sw.img.Main                   \
        --output target/dist

cp -r images target/dist/

echo Runtime image built
