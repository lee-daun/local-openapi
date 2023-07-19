package com.local.openapi.core.cache;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.cache.jcache.JCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

@Configuration
@EnableCaching
public class EhCacheConfig {

    @Bean(name = "ehCacheManagerFactoryBean")
    public JCacheManagerFactoryBean ehCacheManagerFactoryBean() throws Exception {
        JCacheManagerFactoryBean jCacheManagerFactoryBean = new JCacheManagerFactoryBean();
        jCacheManagerFactoryBean.setCacheManagerUri(new ClassPathResource("ehcache.xml").getURI());
        return jCacheManagerFactoryBean;
    }

    @Primary
    @Bean(name = "ehCacheCacheManager")
    public CacheManager ehCacheCacheManager() throws Exception {
        final JCacheCacheManager jCacheCacheManager = new JCacheCacheManager();
        jCacheCacheManager.setCacheManager(ehCacheManagerFactoryBean().getObject());
        return jCacheCacheManager;
    }

}
