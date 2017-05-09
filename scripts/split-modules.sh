#!/usr/bin/env bash
echo Creating module image.main
mkdir -p src/main/java/image.main/fr/sw/img
mv src/main/java/fr/sw/img/web src/main/java/image.main/fr/sw/img/web
mv src/main/java/fr/sw/img/Main.java src/main/java/image.main/fr/sw/img/
mv src/main/java/module-info.java src/main/java/image.main/

echo Creating module image.data
mkdir -p src/main/java/image.data/fr/sw/img
mv src/main/java/fr/sw/img/data src/main/java/image.data/fr/sw/img/
echo "module image.data {
    requires sw.common;
}" > src/main/java/image.data/module-info.java

echo Creating module image.backend
mkdir -p src/main/java/image.backend/fr/sw/img
mv src/main/java/fr/sw/img/* src/main/java/image.backend/fr/sw/img/
echo "module image.backend {
    requires java.sql;
    requires java.desktop;

    requires sw.common;
    requires image.data;
}" > src/main/java/image.backend/module-info.java

echo Creating module image.common
mkdir -p src/main/java/sw.common/fr/sw/fwk
mv src/main/java/fr/sw/fwk/* src/main/java/sw.common/fr/sw/fwk/
echo "open module sw.common {
    requires jdk.httpserver;
    requires java.xml.bind;
}" > src/main/java/sw.common/module-info.java

rm -r src/main/java/fr
echo Done