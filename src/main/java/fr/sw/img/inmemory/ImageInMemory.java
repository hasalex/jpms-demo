package fr.sw.img.inmemory;

import fr.sw.fwk.dao.DAO;
import fr.sw.fwk.dao.DaoException;
import fr.sw.img.data.ImageDescription;

import java.util.*;

public class ImageInMemory implements DAO<ImageDescription> {

    private Map<String, ImageDescription> images = new HashMap<>();

    public ImageDescription find(String name) {
        return images.get(name);
    }

    public List<ImageDescription> findAll() {
        return new ArrayList<>(images.values());
    }

    public ImageDescription update(ImageDescription image) {
        if (!images.containsKey(image.getFileName())) {
            throw new DaoException("Entity does not exist for key " + image.getFileName());
        }
        images.put(image.getFileName(), image);
        return image;
    }

    public ImageDescription create(ImageDescription image) {
        if (images.containsKey(image.getFileName())) {
            throw new DaoException("Entity already exists for key " + image.getFileName());
        }
        images.put(image.getFileName(), image);
        return image;
    }

    public void kill(ImageDescription image) {
        images.remove(image.getFileName());
    }
}
