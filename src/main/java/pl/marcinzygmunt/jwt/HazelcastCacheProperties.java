package pl.marcinzygmunt.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "pl.marcinzygmunt.hazelcast")
public class HazelcastCacheProperties {
    private String clusterName;
    private String publicIpAddress;
    private int port;
    private List<MapCacheProperties> maps;
    private List<String> members;
}
