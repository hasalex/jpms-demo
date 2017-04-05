#!/usr/bin/env bash
mkdir -p src/main/java/image.show/fr/sw/img
mv src/main/java/fr/sw/img/web src/main/java/image.show/fr/sw/img/web
mv src/main/java/fr/sw/img/Main.java src/main/java/image.show/fr/sw/img/
mv src/main/java/module-info.java src/main/java/image.show/

mkdir -p src/main/java/image.data/fr/sw/img
mv src/main/java/fr/sw/img/data src/main/java/image.data/fr/sw/img/
echo "module image.data {}" > src/main/java/image.data/module-info.java

mkdir -p src/main/java/image.backend/fr/sw/img
mv src/main/java/fr/sw/img/* src/main/java/image.backend/fr/sw/img/
echo "module image.backend {}" > src/main/java/image.backend/module-info.java

mkdir -p src/main/java/sw.common/fr/sw/fwk
mv src/main/java/fr/sw/fwk/* src/main/java/sw.common/fr/sw/fwk/
echo "module sw.common {}" > src/main/java/sw.common/module-info.java

rm -r src/main/java/fr