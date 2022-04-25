package me.kafein.common.config;

import lombok.SneakyThrows;
import me.kafein.common.SuperCombat;
import me.kafein.common.config.language.Language;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ConfigLoader {

    private final Map<ConfigType, ConfigurationNode> configurationNodeMap = new HashMap<>();

    @SneakyThrows
    public ConfigLoader loadConfigs(String dataFolder) {
        Class<?> clazz = SuperCombat.class;
        for (ConfigType configType : ConfigType.values()) {
            ClassLoader classLoader = clazz.getClassLoader();
            File file = null;
            InputStream inputStream = null;
            switch (configType) {
                case SETTINGS:
                    file = new File(dataFolder + "/settings.yml");
                    inputStream = classLoader.getResourceAsStream("settings.yml");
                    break;
                case LANGUAGE:
                    String language = configurationNodeMap.get(ConfigType.SETTINGS).getNode("settings", "language").getString();
                    if (language == null || Language.of(language.toUpperCase(Locale.ROOT)) == null) {
                        language = "en";
                    }
                    file = new File(dataFolder + "/language/language_" + language + ".yml");
                    inputStream = classLoader.getResourceAsStream("language/language_" + language + ".yml");
                    break;
            }
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                OutputStream out = new FileOutputStream(file);
                byte[] buf = new byte[1024];
                int len;
                while ((len = inputStream.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                inputStream.close();
            }
            ConfigurationLoader<ConfigurationNode> loader = YAMLConfigurationLoader
                    .builder()
                    .setFlowStyle(DumperOptions.FlowStyle.BLOCK)
                    .setIndent(2)
                    .setFile(file)
                    .build();
            configurationNodeMap.put(configType, loader.load());
        }
        return this;
    }

    @SneakyThrows
    public <T> ConfigLoader loadFields() {
        for (ConfigType configType : ConfigType.values()) {
            for (Field field : configType.getClazz().getDeclaredFields()) {
                ConfigKey<T> configKey = (ConfigKey<T>) field.get(null);
                ConfigurationNode node = configurationNodeMap.getOrDefault(configType, ConfigurationNode.root());
                node = node.getNode(configType.name().toLowerCase(Locale.ROOT));
                for (String path : configKey.getPath()) {
                    node = node.getNode(path);
                }
                T value = (T) node.getValue();
                if (value == null) continue;
                configKey.setValue(value);
            }
        }
        return this;
    }

}
