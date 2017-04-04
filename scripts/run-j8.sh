#!/bin/bash

java -cp target/main/artifact/img.jar:dependencies/vertx-core-3.3.3.jar:dependencies/vertx-internal-3.3.3.jar:dependencies/vertx-web-3.3.3.jar:dependencies/h2.jar \
     fr.sw.img.Main
