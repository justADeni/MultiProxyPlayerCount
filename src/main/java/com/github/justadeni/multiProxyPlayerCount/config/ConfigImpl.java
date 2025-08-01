package com.github.justadeni.multiProxyPlayerCount.config;

import io.github.wasabithumb.jtoml.JToml;
import io.github.wasabithumb.jtoml.document.TomlDocument;
import io.github.wasabithumb.jtoml.except.TomlException;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class ConfigImpl implements Config {

    private static final JToml jtoml = JToml.jToml();

    private final Logger logger;
    private final Path path;
    private TomlDocument doc;

    public ConfigImpl(Path path, Logger logger) {
        this.path = path.resolve("config.toml");
        this.logger = logger;
        if (Files.notExists(this.path)) {
            try (InputStream in = ConfigImpl.class.getResourceAsStream("config.toml")) {
                Files.createDirectories(path.getParent());
                Files.copy(in, path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                logger.error("Error while copying config to data directory. Printing stack trace.");
                Arrays.stream(e.getStackTrace()).forEach(s -> logger.warn(s.toString()));
            }
        }

        reload();
    }

    @Override
    public String getProxyIdentifier() {
        return doc.get("format").asTable().get("name").asPrimitive().asString();
    }

    @Override
    public String getHost() {
        return doc.get("connection").asTable().get("host").asPrimitive().asString();
    }

    @Override
    public int getPort() {
        return doc.get("connection").asTable().get("port").asPrimitive().asInteger();
    }

    @Override
    public String getUser() {
        return doc.get("connection").asTable().get("user").asPrimitive().asString();
    }

    @Override
    public String getPassword() {
        return doc.get("connection").asTable().get("password").asPrimitive().asString();
    }

    @Override
    public String getSimpleFormat() {
        return doc.get("format").asTable().get("simple").asPrimitive().asString();
    }

    @Override
    public String getDetailedFormat() {
        return doc.get("format").asTable().get("detailed").asPrimitive().asString();
    }

    @Override
    public void reload() {
        try {
            doc = jtoml.read(this.path);
        } catch (TomlException e) {
            logger.error("Error while reading config. Printing stack trace.");
            Arrays.stream(e.getStackTrace()).forEach(s -> logger.warn(s.toString()));
        }
    }

}
