package utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigReader {
    private static final Properties properties = new Properties();

    static {
        try {
            InputStream input = null;

            // Пробуем загрузить разными способами
            String[] possiblePaths = {
                    "src/test/resources/config.properties",
                    "src/main/resources/config.properties",
                    "config.properties"
            };

            for (String path : possiblePaths) {
                Path filePath = Paths.get(path);
                if (Files.exists(filePath)) {
                    input = new FileInputStream(filePath.toFile());
                    System.out.println("Config loaded from: " + filePath.toAbsolutePath());
                    break;
                }
            }

            // Если не нашли через файловую систему, пробуем через ClassLoader
            if (input == null) {
                input = ConfigReader.class.getClassLoader()
                        .getResourceAsStream("config.properties");
                if (input != null) {
                    System.out.println("Config loaded from classpath");
                }
            }

            if (input == null) {
                System.err.println("Current directory: " + System.getProperty("user.dir"));
                throw new RuntimeException("config.properties not found! " +
                        "Please create file at: src/test/resources/config.properties");
            }

            properties.load(input);
            input.close();
            System.out.println("config.properties loaded successfully!");

        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static String getGoogleLogin() {
        return properties.getProperty("google.login");
    }

    public static String getGooglePassword() {
        return properties.getProperty("google.password");
    }
}