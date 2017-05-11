package fr.sw.img.inmemory;

import org.junit.Assert;

public class ImageInMemoryTest {

    private ImageInMemory dao = new ImageInMemory();

    @org.junit.Test
    public void findAll() throws Exception {
        Assert.assertTrue(dao.findAll().isEmpty());
    }

}