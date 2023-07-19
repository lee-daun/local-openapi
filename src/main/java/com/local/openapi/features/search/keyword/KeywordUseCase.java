package com.local.openapi.features.search.keyword;

import com.local.openapi.core.domain.KeywordRankInterface;
import com.local.openapi.features.search.keyword.repository.KeywordLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordUseCase {
    private final KeywordLogRepository keywordLogRepository;

    public List<KeywordRankInterface> ranks() {
        List<KeywordRankInterface> ranks= keywordLogRepository.findAllKeywordRank();
        return ranks.stream().limit(10).collect(Collectors.toList());
    }

}
