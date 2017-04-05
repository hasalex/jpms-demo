module image.inmem {
    requires sw.common;
    requires image.data;

    exports fr.sw.img.inmemory
            to image.backend;
}
