package Model;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {
    private static final String CONFIG_FILE = "config.properties";
    private final Properties properties;

    public AppConfig() {
        properties = new Properties();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public String getDbUsername() {
        return properties.getProperty("db.username");
    }

    public String getDbPassword() {
        return properties.getProperty("db.password");
    }
}