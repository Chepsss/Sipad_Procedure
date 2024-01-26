package it.almaviva.difesa.cessazione.procedure.config;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;


@Configuration
@EnableCaching
@Slf4j
@Profile("!test")
public class EhCacheConfig {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;


    public EhCacheConfig(EhCacheProperties ehCacheProperties) {

        EhCacheProperties.EhCache ehCache = ehCacheProperties.getEhCache();

        log.debug("Ehcache config is ==> {}", ehCache.toString());

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
                        ResourcePoolsBuilder.heap(ehCache.getMaxEntries()))
                        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehCache.getTimeToLiveSeconds())))
                        .build());
    }

    @Bean
    public HibernatePropertiesCustomizer hibernateSecondLevelCacheCustomizer(javax.cache.CacheManager cacheManager) {
        return properties -> properties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            // needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }
}
