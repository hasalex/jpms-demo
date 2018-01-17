#!/usr/bin/env bash
module_prefix=fr.sewatech
module_name=$module_prefix.image.main
echo Creating module $module_name
mkdir -p src/main/java/$module_name/fr/sw/img
mv src/main/java/fr/sw/img/web src/main/java/$module_name/fr/sw/img/web
mv src/main/java/fr/sw/img/Main.java src/main/java/$module_name/fr/sw/img/
mv src/main/java/module-info.java src/main/java/$module_name/

module_name=$module_prefix.image.data
echo Creating module $module_name
mkdir -p src/main/java/$module_name/fr/sw/img
mv src/main/java/fr/sw/img/data src/main/java/$module_name/fr/sw/img/
echo "module $module_name {
    requires $module_prefix.common;

    exports fr.sw.img.data;
}" > src/main/java/$module_name/module-info.java

module_name=$module_prefix.image.backend
echo Creating module $module_name
mkdir -p src/main/java/$module_name/fr/sw/img
mv src/main/java/fr/sw/img/* src/main/java/$module_name/fr/sw/img/
echo "module $module_name {
    requires java.sql;
    requires java.desktop;
    requires $module_prefix.common;
    requires $module_prefix.image.data;

    exports fr.sw.img.service;
}" > src/main/java/$module_name/module-info.java

module_name=$module_prefix.common
echo Creating module $module_name
mkdir -p src/main/java/$module_name/fr/sw/fwk
mv src/main/java/fr/sw/fwk/* src/main/java/$module_name/fr/sw/fwk/
echo "open module $module_name {
    requires jdk.httpserver;
    requires java.xml.bind;

    exports fr.sw.fwk.common;
    exports fr.sw.fwk.dao;
    exports fr.sw.fwk.web;
}" > src/main/java/$module_name/module-info.java

rm -r src/main/java/fr
echo Done