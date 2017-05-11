module image.backend {
    requires java.sql;
    requires java.desktop;

    requires sw.common;
    requires image.data;

    requires image.inmem;
    requires image.db;

    exports fr.sw.img.service;
}
