package com.github.vector4wang.proxy;

import java.net.Proxy;
import java.util.List;

/**
 * @author : wangxc
 */
public abstract class ProxyBuilder {

    public class Proxy2{
        private String ip;
        private int port;

        public Proxy2() {
        }

        public Proxy2(String ip, int port) {
            this.ip = ip;
            this.port = port;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public enum Type {
        RANDOM
    }

    public abstract List<Proxy2> extractProxyIp();


}
