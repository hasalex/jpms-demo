module image.db {
    requires java.sql;

    requires sw.common;
    requires image.data;

    exports fr.sw.img.db to image.backend;
}
