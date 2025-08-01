package com.github.justadeni.multiProxyPlayerCount.config;

public class Config {

    private Config() {
        PROXY_IDENTIFIER = ""; //TODO: add toml file loading and unloading in this singleton
    }

    public final String PROXY_IDENTIFIER;

}
