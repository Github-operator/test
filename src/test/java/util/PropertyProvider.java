package util;

import org.testng.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public final class PropertyProvider {

    private static PropertyProvider instance;
    private final ArrayList<Properties> propertiesList = new ArrayList<>();

    private PropertyProvider() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream("project.properties"));
        propertiesList.add(properties);
    }

    private static PropertyProvider getInstance() throws IOException {
        if (instance == null) {
            instance = new PropertyProvider();
        }
        return instance;
    }

    public static String getProperty(final String key) throws IOException {
        for (Properties properties : getInstance().propertiesList) {
            String result = properties.getProperty(key, null);
            if (result != null) {
                return result;
            }
        }
        Assert.fail("Свойство " + key + " не найдено");
        return "";
    }

    public static String getEnvProperty(final String key) {
            return System.getenv(key);
    }
}
