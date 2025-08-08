package com.github.justadeni.multiProxyPlayerCount.config;

public interface Config {

    String getBaseCommand();

    String getDetailedCommand();

    String getProxyIdentifier();

    String getHost();

    int getPort();

    String getUser();

    String getPassword();

    String getSimpleFormat();

    String getDetailedFormat();

    void reload();

}
