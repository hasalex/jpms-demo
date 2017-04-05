module image.db {
    requires java.sql;
    requires sw.common;
    requires image.data;

    provides fr.sw.fwk.dao.DAO with fr.sw.img.db.ImageDB;
}
