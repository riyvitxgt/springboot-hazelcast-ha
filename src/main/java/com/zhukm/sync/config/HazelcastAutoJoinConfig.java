package com.zhukm.sync.config;

import com.hazelcast.config.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastAutoJoinConfig {

    @Bean
    public Config hazelcastConfig() {
        Config config = new Config();
        config.setInstanceName("task-distribution-cluster");

        NetworkConfig networkConfig = config.getNetworkConfig();
        JoinConfig joinConfig = networkConfig.getJoin();

        // 方式1: TCP/IP 静态发现
        configureTcpIpDiscovery(joinConfig);

        // 方式2: 组播发现（局域网环境）
        configureMulticastDiscovery(joinConfig);

        // 方式3: AWS云环境发现
        configureAwsDiscovery(joinConfig);

        // 方式4: Kubernetes环境发现
        configureKubernetesDiscovery(joinConfig);

        // 方式5: Consul/Eureka服务发现
        configureServiceDiscovery(joinConfig);

        return config;
    }

    private void configureTcpIpDiscovery(JoinConfig joinConfig) {
        joinConfig.getTcpIpConfig()
                .setEnabled(true)
                .addMember("192.168.1.100:5701")  // 已知的种子节点
                .addMember("192.168.1.101:5701")
                .addMember("192.168.1.102:5701")
                .setConnectionTimeoutSeconds(30);
    }

    private void configureMulticastDiscovery(JoinConfig joinConfig) {
        joinConfig.getMulticastConfig()
                .setEnabled(false)  // 生产环境通常禁用
                .setMulticastGroup("224.2.2.3")
                .setMulticastPort(54327)
                .setMulticastTimeoutSeconds(30);
    }

    private void configureAwsDiscovery(JoinConfig joinConfig) {
        joinConfig.getAwsConfig()
                .setEnabled(false)
                .setProperty("access-key", "your-access-key")
                .setProperty("secret-key", "your-secret-key")
                .setProperty("region", "us-west-1")
                .setProperty("tag-key", "hazelcast-cluster")
                .setProperty("tag-value", "task-distribution");
    }

    private void configureKubernetesDiscovery(JoinConfig joinConfig) {
        joinConfig.getKubernetesConfig()
                .setEnabled(false)
                .setProperty("namespace", "default")
                .setProperty("service-name", "hazelcast-service")
                .setProperty("service-label-name", "app")
                .setProperty("service-label-value", "hazelcast");
    }

    private void configureServiceDiscovery(JoinConfig joinConfig) {
        // 使用Consul作为服务发现
        DiscoveryConfig discoveryConfig = joinConfig.getDiscoveryConfig();
        discoveryConfig.getDiscoveryStrategyConfigs().add(
                new DiscoveryStrategyConfig("consul-discovery-strategy")
        );
    }
}