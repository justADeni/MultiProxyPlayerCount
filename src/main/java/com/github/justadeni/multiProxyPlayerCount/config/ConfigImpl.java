package com.github.justadeni.multiProxyPlayerCount.config;

import com.moandjiezana.toml.Toml;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class ConfigImpl implements Config {

    private final Logger logger;
    private final Path path;
    private Toml toml;

    public ConfigImpl(Path path, Logger logger) {
        this.path = path.resolve("config.toml");
        this.logger = logger;
        if (Files.notExists(this.path)) {
            try (InputStream is = ConfigImpl.class.getResourceAsStream("/config.toml")) {
                Files.createDirectories(this.path.getParent());
                Files.copy(is, this.path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                logger.error("Error while copying config to data directory. Printing stack trace.");
                Arrays.stream(e.getStackTrace()).forEach(s -> logger.warn(s.toString()));
            }
        }

        reload();
    }

    @Override
    public String getBaseCommand() {
        return toml.getTable("command").getString("base");
    }

    @Override
    public String getDetailedCommand() {
        return toml.getTable("command").getString("detailed");
    }

    @Override
    public String getProxyIdentifier() {
        return toml.getTable("format").getString("name");
    }

    @Override
    public String getHost() {
        return toml.getTable("connection").getString("host");
    }

    @Override
    public int getPort() {
        return Math.toIntExact(toml.getTable("connection").getLong("port"));
    }

    @Override
    public String getUser() {
        return toml.getTable("connection").getString("user");
    }

    @Override
    public String getPassword() {
        return toml.getTable("connection").getString("password");
    }

    @Override
    public String getSimpleFormat() {
        return toml.getTable("format").getString("simple");
    }

    @Override
    public String getDetailedFormat() {
        return toml.getTable("format").getString("detailed");
    }

    @Override
    public void reload() {
        try (InputStream is = Files.newInputStream(path)) {
            toml = toml.read(is);
        } catch (IOException e) {
            logger.error("Error while reading config. Printing stack trace.");
            Arrays.stream(e.getStackTrace()).forEach(s -> logger.warn(s.toString()));
        }
    }

}
