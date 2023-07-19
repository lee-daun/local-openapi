package com.local.openapi.features.search.keyword.repository;

import com.local.openapi.core.domain.KeywordRankInterface;
import com.local.openapi.core.entity.KeywordLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KeywordLogRepository extends JpaRepository<KeywordLogEntity, Long> {


    @Query(value = "SELECT name" +
            ", COUNT(1) as count" +
            " FROM keyword_log " +
            " WHERE service_name='places'" +
            " GROUP BY name " +
            "ORDER BY COUNT(1) DESC", nativeQuery = true)
    List<KeywordRankInterface> findAllKeywordRank();

}
