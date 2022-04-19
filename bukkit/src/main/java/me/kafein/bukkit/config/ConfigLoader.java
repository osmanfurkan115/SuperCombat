package me.kafein.bukkit.config;

import lombok.Getter;
import lombok.SneakyThrows;
import me.kafein.common.config.ConfigKey;
import me.kafein.common.config.ConfigKeys;
import me.kafein.common.config.ConfigType;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Locale;

public class ConfigLoader {

    @Getter
    private static ConfigLoader instance;
    final private Plugin plugin;

    public ConfigLoader(Plugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    @SneakyThrows
    public ConfigLoader loadConfigs() {

        for (ConfigType configType : ConfigType.values()) {

            File file = null;
            switch (configType) {
                case SETTINGS:
                    file = new File(plugin.getDataFolder(), "settings.yml");
                    break;
                case LANGUAGE:
                    String language = ConfigType.SETTINGS.getConfigurationNode().getNode("settings", "language").getString();
                    file = new File(plugin.getDataFolder(), "language/language_" + language + ".yml");
                    break;
            }
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    plugin.saveResource(configType.getFileName(), false);
                }catch (IllegalArgumentException e) {
                    if (configType == ConfigType.LANGUAGE) {
                        file.createNewFile();
                    }else {
                        throw new NullPointerException("Resource not found! : " + configType.getFileName());
                    }
                }
            }

            ConfigurationNode configurationNode = YAMLConfigurationLoader
                    .builder()
                    .setFile(file)
                    .build().load();

            configType.setConfigurationNode(configurationNode);

        }

        return this;

    }

    @SneakyThrows
    public <T> void loadFields() {

        for (Field field : ConfigKeys.class.getDeclaredFields()) {
            ConfigKey<T> configKey = (ConfigKey<T>) field.get(null);
            ConfigType configType = configKey.getConfigType();
            ConfigurationNode configurationNode = configType.getConfigurationNode();
            if (configurationNode == null) throw new NullPointerException("ConfigurationNode is null");
            configurationNode = configurationNode.getNode(configType.name().toLowerCase(Locale.ROOT), configKey.getPath());
            configKey.setKey((T) configurationNode.getValue());
        }

    }

}
