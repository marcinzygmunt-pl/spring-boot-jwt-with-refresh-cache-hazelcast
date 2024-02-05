package pl.marcinzygmunt.jwt;


import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.web.WebFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;


@Configuration
@EnableCaching
@RequiredArgsConstructor
public class HazelcastConfig {

    @Bean
    public WebFilter webFilter(HazelcastInstance hazelcastInstance) {
        Properties properties = new Properties();
        properties.put("instance-name", hazelcastInstance.getName());
        properties.put("sticky-session", "false");
        properties.put("cookie-secure", true);
        properties.put("cookie-http-only", true);
        return new WebFilter(properties);
    }

    @Bean
    public Config hazelcastCacheConfig(HazelcastCacheProperties hazelcastCacheProperties) {
        Config config = new Config();
        config.setClusterName("hazelcast");
        config.setNetworkConfig(
                buildNetworkConfig(hazelcastCacheProperties));
        hazelcastCacheProperties.getMaps().forEach(map -> config.addMapConfig(mapConfig(map)));
        return config;
    }

    private MapConfig mapConfig(MapCacheProperties cacheProperties) {
        MapConfig mapConfig = new MapConfig(cacheProperties.getName());
        mapConfig.setTimeToLiveSeconds(Math.toIntExact(cacheProperties.getTimeToLive().getSeconds()));
        mapConfig.setMaxIdleSeconds(Math.toIntExact(cacheProperties.getMaxIdle().getSeconds()));
        return mapConfig;
    }

    private NetworkConfig buildNetworkConfig(HazelcastCacheProperties hazelcastCacheProperties) {
        NetworkConfig networkConfig = new NetworkConfig();
        networkConfig.setPort(hazelcastCacheProperties.getPort());
        networkConfig.setPortAutoIncrement(false);
        networkConfig.setPublicAddress(hazelcastCacheProperties.getPublicIpAddress() + ":" + hazelcastCacheProperties.getPort());
        if (!hazelcastCacheProperties.getMembers().isEmpty()) {
            TcpIpConfig tcpIpConfig = new TcpIpConfig().setEnabled(true).setMembers(hazelcastCacheProperties.getMembers());
            networkConfig.getJoin().setTcpIpConfig(tcpIpConfig);
            networkConfig.getJoin().getMulticastConfig().setEnabled(false);
        }
        return networkConfig;
    }

}
