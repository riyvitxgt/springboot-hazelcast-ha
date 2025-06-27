package com.zhukm.sync.config;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    private static final Logger logger = LoggerFactory.getLogger(HazelcastConfig.class);

    private final HazelcastProperties hazelcastProperties;

    public HazelcastConfig(HazelcastProperties hazelcastProperties) {
        this.hazelcastProperties = hazelcastProperties;
    }

    @Bean
    public Config config() {
        Config config = new Config();

        // 设置集群名称
        config.setClusterName(hazelcastProperties.getCluster().getName());

        // 配置网络
        configureNetwork(config);

        // 配置发现机制
        configureDiscovery(config);

        // 配置缓存Map
        configureMaps(config);

        // 配置管理中心
        configureManagementCenter(config);

        logger.info("Hazelcast配置初始化完成，集群名称: {}", config.getClusterName());

        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(Config config) {
        logger.info("正在启动Hazelcast实例...");
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);
        logger.info("Hazelcast实例启动成功，成员UUID: {}", instance.getCluster().getLocalMember().getUuid());
        return instance;
    }

    private void configureNetwork(Config config) {
        NetworkConfig networkConfig = config.getNetworkConfig();
        HazelcastProperties.Network network = hazelcastProperties.getCluster().getNetwork();

        networkConfig.setPort(network.getPort())
                .setPortAutoIncrement(network.isPortAutoIncrement())
                .setPortCount(network.getPortCount());

        logger.info("网络配置 - 端口: {}, 自动递增: {}, 端口数量: {}",
                network.getPort(), network.isPortAutoIncrement(), network.getPortCount());
    }

    private void configureDiscovery(Config config) {
        JoinConfig joinConfig = config.getNetworkConfig().getJoin();

        // 配置多播发现
        HazelcastProperties.Multicast multicast = hazelcastProperties.getCluster().getMulticast();
        MulticastConfig multicastConfig = joinConfig.getMulticastConfig();
        multicastConfig.setEnabled(multicast.isEnabled());
        if (multicast.isEnabled()) {
            multicastConfig.setMulticastGroup(multicast.getMulticastGroup())
                    .setMulticastPort(multicast.getMulticastPort());
            logger.info("多播发现已启用 - 组: {}, 端口: {}",
                    multicast.getMulticastGroup(), multicast.getMulticastPort());
        }

        // 配置TCP/IP发现
        HazelcastProperties.TcpIp tcpIp = hazelcastProperties.getCluster().getTcpIp();
        TcpIpConfig tcpIpConfig = joinConfig.getTcpIpConfig();
        tcpIpConfig.setEnabled(tcpIp.isEnabled());

        if (tcpIp.isEnabled() && hazelcastProperties.getCluster().getMembers() != null) {
            for (String member : hazelcastProperties.getCluster().getMembers()) {
                tcpIpConfig.addMember(member);
                logger.info("添加集群成员: {}", member);
            }
        }

        // 禁用AWS发现
        joinConfig.getAwsConfig().setEnabled(false);
        // 禁用Azure发现
        joinConfig.getAzureConfig().setEnabled(false);
        // 禁用GCP发现
        joinConfig.getGcpConfig().setEnabled(false);
        // 禁用Kubernetes发现
        joinConfig.getKubernetesConfig().setEnabled(false);
    }

    private void configureMaps(Config config) {
        // 配置默认缓存Map
        MapConfig defaultMapConfig = new MapConfig("default")
                .setTimeToLiveSeconds(3600)
                .setMaxIdleSeconds(1800)
                .setEvictionConfig(new EvictionConfig()
                        .setEvictionPolicy(EvictionPolicy.LRU)
                        .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                        .setSize(10000));

        config.addMapConfig(defaultMapConfig);

        // 配置用户缓存Map
        MapConfig userCacheConfig = new MapConfig("userCache")
                .setTimeToLiveSeconds(7200)
                .setMaxIdleSeconds(3600)
                .setEvictionConfig(new EvictionConfig()
                        .setEvictionPolicy(EvictionPolicy.LFU)
                        .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                        .setSize(5000));

        config.addMapConfig(userCacheConfig);

        // 配置会话缓存Map
        MapConfig sessionCacheConfig = new MapConfig("sessionCache")
                .setTimeToLiveSeconds(1800)
                .setMaxIdleSeconds(900)
                .setEvictionConfig(new EvictionConfig()
                        .setEvictionPolicy(EvictionPolicy.LRU)
                        .setMaxSizePolicy(MaxSizePolicy.PER_NODE)
                        .setSize(1000));

        config.addMapConfig(sessionCacheConfig);

        logger.info("Map缓存配置完成");
    }

    private void configureManagementCenter(Config config) {
        HazelcastProperties.ManagementCenter managementCenter =
                hazelcastProperties.getCluster().getManagementCenter();

        if (managementCenter.isEnabled() && managementCenter.getUrl() != null) {
            ManagementCenterConfig mcConfig = config.getManagementCenterConfig();
            mcConfig.setConsoleEnabled(true);
            logger.info("管理中心已启用: {}", managementCenter.getUrl());
        }
    }
}