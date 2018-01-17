#!/usr/bin/env bash
module_prefix=fr.sewatech
mkdir -p src/main/java/$module_prefix.image.db/fr/sw/img
mv src/main/java/$module_prefix.image.backend/fr/sw/img/db src/main/java/$module_prefix.image.db/fr/sw/img/
echo "module $module_prefix.image.db {
    requires java.sql;

    requires $module_prefix.common;
    requires $module_prefix.image.data;

    exports fr.sw.img.db;
}" > src/main/java/$module_prefix.image.db/module-info.java

mkdir -p src/main/java/$module_prefix.image.inmem/fr/sw/img
mv src/main/java/$module_prefix.image.backend/fr/sw/img/inmemory src/main/java/$module_prefix.image.inmem/fr/sw/img/
echo "module $module_prefix.image.inmem {
    requires $module_prefix.common;
    requires $module_prefix.image.data;

    exports fr.sw.img.inmemory;
}" > src/main/java/$module_prefix.image.inmem/module-info.java

mkdir -p src/test/java/$module_prefix.image.inmem/fr/sw/img
mv src/test/java/fr/sw/img/inmemory src/test/java/$module_prefix.image.inmem/fr/sw/img/inmemory
rm -r src/test/java/fr
