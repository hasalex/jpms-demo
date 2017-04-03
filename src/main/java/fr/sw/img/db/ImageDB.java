package fr.sw.img.db;

import fr.sw.fwk.common.Configuration;
import fr.sw.fwk.dao.DAO;
import fr.sw.fwk.dao.DaoException;
import fr.sw.img.data.ImageDescription;

import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ImageDB implements DAO<ImageDescription> {

    private static final String SQL_FIND_ALL = "SELECT filename, content, thumbnail FROM image";
    private static final String SQL_FIND_BY_NAME = SQL_FIND_ALL + " where filename=?";
    private static final String SQL_UPDATE = "UPDATE image SET content=?,thumbnail=? WHERE filename=?";
    private static final String SQL_CREATE = "INSERT INTO image (filename, content, thumbnail) VALUES (?, ?, ?)";
    private static final String SQL_KILL = "DELETE FROM image WHERE filename=?";
    private static final String SQL_INIT = "CREATE TABLE image (filename VARCHAR, content VARCHAR, thumbnail VARCHAR)";

    private final Connection connection;

    public ImageDB() {
        try {
            connection = DriverManager.getConnection(Configuration.get().getDb());
            init();
        } catch (SQLException e) {
            throw new DaoException("Initializing DAO", e);
        }
    }

    public ImageDescription find(String name) {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_FIND_BY_NAME);
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ImageDescription image = new ImageDescription();
                image.setFileName(name);
                image.setContent(fromBase64(resultSet.getString("content")));
                image.setThumbnail(fromBase64(resultSet.getString("thumbnail")));

                return image;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DaoException("Find by name " + name, e);
        }
    }

    @Override
    public List<ImageDescription> findAll() {
        try {
            List<ImageDescription> result = new ArrayList<>();

            PreparedStatement statement = connection.prepareStatement(SQL_FIND_ALL);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                ImageDescription image = new ImageDescription();
                image.setFileName(resultSet.getString("filename"));
                image.setContent(fromBase64(resultSet.getString("content")));
                image.setThumbnail(fromBase64(resultSet.getString("thumbnail")));

                result.add(image);
            }

            return result;
        } catch (SQLException e) {
            throw new DaoException("Find all ", e);
        }
    }

    public ImageDescription update(ImageDescription image) {
        ImageDescription imageFound = find(image.getFileName());
        if (imageFound == null) {
            throw new DaoException("Entity does not exist for key " + image.getFileName());
        }

        try {
            PreparedStatement statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1, toBase64(image.getContent()));
            statement.setString(2, toBase64(image.getThumbnail()));
            statement.setString(3, image.getFileName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Update " + image.getFileName(), e);
        }

        return image;
    }

    public ImageDescription create(ImageDescription image) {
        ImageDescription imageFound = find(image.getFileName());
        if (imageFound != null) {
            throw new DaoException(DaoException.Reason.ALREADY_EXISTS, "Entity already exists for key " + image.getFileName());
        }

        try {
            PreparedStatement statement = connection.prepareStatement(SQL_CREATE);
            statement.setString(1, image.getFileName());
            statement.setString(2, toBase64(image.getContent()));
            statement.setString(3, toBase64(image.getThumbnail()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Create " + image.getFileName(), e);
        }

        return image;
    }

    public void kill(ImageDescription image) {
        ImageDescription imageFound = find(image.getFileName());
        if (imageFound == null) {
            throw new DaoException("Entity does not exist for key " + image.getFileName());
        }

        try {
            PreparedStatement statement = connection.prepareStatement(SQL_KILL);
            statement.setString(1, image.getFileName());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Kill " + image.getFileName(), e);
        }
    }

    private void init() {
        try {
            PreparedStatement statement = connection.prepareStatement(SQL_INIT);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Init DB ", e);
        }
    }

    private String toBase64(byte[] content) {
//        return new BASE64Encoder().encodeBuffer(content);
        return Base64.getEncoder().encodeToString(content);
    }
    private byte[] fromBase64(String encoded) {
//        try {
//            return new BASE64Decoder().decodeBuffer(encoded);
//        } catch (IOException e) {
//            throw new DaoException(e);
//        }
        return Base64.getDecoder().decode(encoded);
    }
}
