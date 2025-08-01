package com.github.justadeni.multiProxyPlayerCount.config;

public interface Config {

    String getProxyIdentifier();

    String getSimpleFormat();

    String getDetailedFormat();

    void reload();

}
