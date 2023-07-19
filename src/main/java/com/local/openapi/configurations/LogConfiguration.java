package com.local.openapi.configurations;

import com.local.openapi.core.aop.KeywordLog;
import com.local.openapi.core.entity.KeywordLogEntity;
import com.local.openapi.features.search.keyword.repository.KeywordLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Component
@Aspect
@RequiredArgsConstructor
public class LogConfiguration {

    private final KeywordLogRepository keywordLogRepository;
    @Before("@annotation(com.local.openapi.core.aop.KeywordLog)")
    public void keywordLogging(JoinPoint joinPoint) {
        KeywordLogEntity entity = getKeywordLogEntity(joinPoint);
        entity = keywordLogRepository.save(entity);
        log.info("keywordLogging = {}",entity.toString());
    }

    private KeywordLogEntity getKeywordLogEntity(JoinPoint joinPoint){

        Object[] args = joinPoint.getArgs();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        KeywordLog keywordLog = method.getAnnotation(KeywordLog.class);

        int len = method.getParameters().length;
        String parameterName=null;
        for (int i = 0; i < len; i++) {
            parameterName = method.getParameters()[i].getName();
            String keyword = null;
            if(parameterName.equals("keyword")){
                keyword = (String) args[i];
                return KeywordLogEntity.builder()
                        .name(keyword)
                        .serviceName(keywordLog.serviceName())
                        .build();
            }
        }
        return null;
    }

}
