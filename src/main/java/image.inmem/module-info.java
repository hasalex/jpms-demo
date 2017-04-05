module image.inmem {
    requires sw.common;
    requires image.data;

    provides fr.sw.fwk.dao.DAO with fr.sw.img.inmemory.ImageInMemory;
}
