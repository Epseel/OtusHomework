package properties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FilePropertiesReader implements IPropertyReader {

    @Override
    public Map<String, String> getSettings() throws IOException {
        Properties properties = new Properties();
        properties.load(Files.newInputStream(Paths.get(System.getProperty("user.dir") + "/src/main/resources/database.properties")));

        Map<String, String> settings = new HashMap<>();

        for (String key : properties.stringPropertyNames()) {
            settings.put(key, properties.getProperty(key));
        }

        return settings;
    }
}
