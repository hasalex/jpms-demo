open module sw.common {
    requires java.xml.bind;
    requires jdk.httpserver;

    exports fr.sw.fwk.common;
    exports fr.sw.fwk.dao;
    exports fr.sw.fwk.web;
}
