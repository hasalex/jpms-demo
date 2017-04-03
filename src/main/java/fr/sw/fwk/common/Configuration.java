package fr.sw.fwk.common;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.io.InputStream;

@XmlRootElement
public class Configuration {

    private final static Logger logger = new Logger(Configuration.class);
    private static Configuration configuration;

    public static Configuration load(String configurationFileName) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Configuration.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            try (InputStream inputStream = Thread
                    .currentThread()
                    .getContextClassLoader()
                    .getResourceAsStream(configurationFileName)) {
                if (inputStream == null) {
                    logger.error("Cannot load configuration : " + configurationFileName + " not found");
                } else {
                    return (Configuration) unmarshaller.unmarshal(inputStream);
                }
            }
        } catch (JAXBException | IOException e) {
            logger.error("Arggh, cannot read the configuration file: ", e);
        }
        return getDefaultConfiguration();
    }

    public static Configuration get() {
        if (configuration == null)
            configuration = load("configuration.xml");
        return configuration;
    }

    private static Configuration getDefaultConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setPort(8080);
        configuration.setDb("jdbc:h2:mem:");
        return configuration;
    }

    private int port;
    private String db;

    public int getPort() {
        return port;
    }

    @XmlElement
    private void setPort(int port) {
        this.port = port;
    }

    public String getDb() {
        return db;
    }

    @XmlElement
    public void setDb(String db) {
        this.db = db;
    }

    @Override
    public String toString() {
        return "Configuration [port=" + port + ", db=" + db + "]";
    }
}