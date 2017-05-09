#!/usr/bin/env bash
mkdir -p src/main/java/image.db/fr/sw/img
mv src/main/java/image.backend/fr/sw/img/db src/main/java/image.db/fr/sw/img/
echo "module image.db {
    requires java.sql;

    requires sw.common;
    requires image.data;

    exports fr.sw.img.db;
}" > src/main/java/image.db/module-info.java

mkdir -p src/main/java/image.inmem/fr/sw/img
mv src/main/java/image.backend/fr/sw/img/inmemory src/main/java/image.inmem/fr/sw/img/
echo "module image.inmem {
    requires sw.common;
    requires image.data;

    exports fr.sw.img.inmemory;
}" > src/main/java/image.inmem/module-info.java

mkdir -p src/test/java/image.inmem/fr/sw/img
mv src/test/java/fr/sw/img/inmemory src/test/java/image.inmem/fr/sw/img/inmemory
rm -r src/test/java/fr
