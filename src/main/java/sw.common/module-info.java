open module sw.common {
    requires jdk.httpserver;
    requires java.xml.bind;

//    requires slf4j.api;

    exports fr.sw.fwk.web;
    exports fr.sw.fwk.dao;
    exports fr.sw.fwk.common;
}
