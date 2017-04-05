module image.backend {
    requires java.desktop;

    requires transitive sw.common;
    requires transitive image.data;

    exports fr.sw.img.service;

    uses fr.sw.fwk.dao.DAO;
}
