package fr.sw.img.service.test;

import fr.sw.img.data.ImageDescription;
import fr.sw.img.inmemory.ImageInMemory;
import fr.sw.img.service.ImageService;

import java.util.Collection;

import static org.junit.Assert.assertTrue;

public class ImageServiceTest {

    private ImageService service = new ImageService(new ImageInMemory());

    @org.junit.Test
    public void findAll() throws Exception {
        assertTrue(service.all().isEmpty());
    }

}