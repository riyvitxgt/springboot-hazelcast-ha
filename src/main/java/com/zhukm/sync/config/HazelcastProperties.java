package com.zhukm.sync.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "hazelcast")
public class HazelcastProperties {

    private Cluster cluster = new Cluster();

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public static class Cluster {
        private String name = "dev-cluster";
        private String password = "dev-password";
        private List<String> members;
        private Network network = new Network();
        private Multicast multicast = new Multicast();
        private TcpIp tcpIp = new TcpIp();
        private ManagementCenter managementCenter = new ManagementCenter();
        private Persistence persistence = new Persistence();
        private Logging logging = new Logging();

        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }

        public List<String> getMembers() { return members; }
        public void setMembers(List<String> members) { this.members = members; }

        public Network getNetwork() { return network; }
        public void setNetwork(Network network) { this.network = network; }

        public Multicast getMulticast() { return multicast; }
        public void setMulticast(Multicast multicast) { this.multicast = multicast; }

        public TcpIp getTcpIp() { return tcpIp; }
        public void setTcpIp(TcpIp tcpIp) { this.tcpIp = tcpIp; }

        public ManagementCenter getManagementCenter() { return managementCenter; }
        public void setManagementCenter(ManagementCenter managementCenter) { this.managementCenter = managementCenter; }

        public Persistence getPersistence() { return persistence; }
        public void setPersistence(Persistence persistence) { this.persistence = persistence; }

        public Logging getLogging() { return logging; }
        public void setLogging(Logging logging) { this.logging = logging; }
    }

    public static class Network {
        private int port = 5701;
        private boolean portAutoIncrement = true;
        private int portCount = 100;
        private String publicAddress;
        private Interfaces interfaces = new Interfaces();

        // Getters and Setters
        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }

        public boolean isPortAutoIncrement() { return portAutoIncrement; }
        public void setPortAutoIncrement(boolean portAutoIncrement) { this.portAutoIncrement = portAutoIncrement; }

        public int getPortCount() { return portCount; }
        public void setPortCount(int portCount) { this.portCount = portCount; }

        public String getPublicAddress() { return publicAddress; }
        public void setPublicAddress(String publicAddress) { this.publicAddress = publicAddress; }

        public Interfaces getInterfaces() { return interfaces; }
        public void setInterfaces(Interfaces interfaces) { this.interfaces = interfaces; }
    }

    public static class Interfaces {
        private boolean enabled = false;
        private List<String> interfaces;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public List<String> getInterfaces() { return interfaces; }
        public void setInterfaces(List<String> interfaces) { this.interfaces = interfaces; }
    }

    public static class Multicast {
        private boolean enabled = false;
        private String multicastGroup = "224.2.2.3";
        private int multicastPort = 54327;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public String getMulticastGroup() { return multicastGroup; }
        public void setMulticastGroup(String multicastGroup) { this.multicastGroup = multicastGroup; }

        public int getMulticastPort() { return multicastPort; }
        public void setMulticastPort(int multicastPort) { this.multicastPort = multicastPort; }
    }

    public static class TcpIp {
        private boolean enabled = true;
        private int connectionTimeoutSeconds = 30;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public int getConnectionTimeoutSeconds() { return connectionTimeoutSeconds; }
        public void setConnectionTimeoutSeconds(int connectionTimeoutSeconds) { this.connectionTimeoutSeconds = connectionTimeoutSeconds; }
    }

    public static class ManagementCenter {
        private boolean enabled = false;
        private String url;

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }
    }

    public static class Persistence {
        private boolean enabled = false;
        private String baseDir = "./hazelcast-persistence";

        public boolean isEnabled() { return enabled; }
        public void setEnabled(boolean enabled) { this.enabled = enabled; }

        public String getBaseDir() { return baseDir; }
        public void setBaseDir(String baseDir) { this.baseDir = baseDir; }
    }

    public static class Logging {
        private String type = "slf4j";

        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
}