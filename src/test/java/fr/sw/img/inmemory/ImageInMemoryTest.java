package fr.sw.img.inmemory;

import fr.sw.img.data.ImageDescription;
import org.junit.Assert;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;

import java.util.Collection;

public class ImageInMemoryTest {

    private ImageInMemory dao = new ImageInMemory();

    @org.junit.Test
    public void findAll() throws Exception {
        Assert.assertTrue(dao.findAll().isEmpty());
    }

}