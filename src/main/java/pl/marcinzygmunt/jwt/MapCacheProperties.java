package pl.marcinzygmunt.jwt;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
@Getter
@Setter
public class MapCacheProperties {
        private String name;
        private Duration timeToLive;
        private Duration maxIdle;
    }