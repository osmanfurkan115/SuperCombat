package me.kafein.common.config;

import lombok.Setter;
import ninja.leaping.configurate.ConfigurationNode;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Locale;

public enum ConfigType {
    SETTINGS(), LANGUAGE();

    @Setter private ConfigurationNode configurationNode;

    @Nullable
    public ConfigurationNode getConfigurationNode() {
        return configurationNode;
    }

    public String getFileName() {
        return this.name().toLowerCase(Locale.ROOT) + ".yml";
    }

}