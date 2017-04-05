package fr.sw.img.service;

import fr.sw.fwk.common.Logger;
import fr.sw.fwk.common.SwException;
import fr.sw.fwk.dao.DAO;
import fr.sw.fwk.dao.DaoException;
import fr.sw.img.data.ImageDescription;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ServiceLoader;

public class ImageService {

    private final DAO<ImageDescription> dao;
    private final Logger logger = new Logger(ImageService.class);

    public ImageService() {
        dao = ServiceLoader.load(DAO.class)
                .findFirst()
                .orElseThrow(() -> new SwException("DAO not found"));
        logger.log("DAO : " + dao.getClass().getName());
    }

    public void createOrUpdate(ImageDescription image) {
        try {
            createThumbnail(image);
            dao.create(image);
        } catch (IOException e) {
            throw new DaoException(e);
        } catch (DaoException e) {
            if (e.getReason() == DaoException.Reason.ALREADY_EXISTS) {
                dao.update(image);
            } else {
                throw e;
            }
        }
    }

    private void createThumbnail(ImageDescription image) throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(image.getContent());

        BufferedImage sourceImage = ImageIO.read(inputStream);
        Image thumbnail = sourceImage.getScaledInstance(256, -1, Image.SCALE_SMOOTH);
        BufferedImage bufferedThumbnail = new BufferedImage(
                thumbnail.getWidth(null),
                thumbnail.getHeight(null),
                BufferedImage.TYPE_4BYTE_ABGR);
        bufferedThumbnail.getGraphics().drawImage(thumbnail, 0, 0, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedThumbnail, "png", outputStream);

        image.setThumbnail(outputStream.toByteArray());
    }

    public ImageDescription get(String name) {
        return dao.find(name);
    }

    public List<ImageDescription> all() {
        List<ImageDescription> images = dao.findAll();
        Collections.shuffle(images);
        return images;
    }
}
