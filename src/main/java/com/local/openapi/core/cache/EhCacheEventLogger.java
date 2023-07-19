package com.local.openapi.core.cache;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class EhCacheEventLogger implements CacheEventListener<Object, Object> {


    @Override
    public void onEvent(
            CacheEvent<? extends Object, ? extends Object> cacheEvent) {
        log.info("EhCacheEventLogger Event Type : "+cacheEvent.getType().name());
        log.info(""+cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }
}
