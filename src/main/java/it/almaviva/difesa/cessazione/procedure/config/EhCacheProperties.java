package it.almaviva.difesa.cessazione.procedure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "cache")
public class EhCacheProperties {

    private final EhCache ehCache = new EhCache();

    @Getter
    @Setter
    public static class EhCache {

        private long timeToLiveSeconds;
        private long maxEntries;

        @Override
        public String toString() {
            return "EhCache{" +
                    "timeToLiveSeconds=" + timeToLiveSeconds +
                    ", maxEntries=" + maxEntries +
                    '}';
        }
    }
}
