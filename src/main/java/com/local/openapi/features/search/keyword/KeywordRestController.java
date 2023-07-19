package com.local.openapi.features.search.keyword;


import com.local.openapi.core.domain.KeywordRankInterface;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/keyword")
public class KeywordRestController {

    private final KeywordUseCase keywordUseCase;
    @GetMapping(path = "/ranks")
    ResponseEntity<List<KeywordRankResponse>> getRanks(){

        List<KeywordRankInterface> ranks= keywordUseCase.ranks();
        return ResponseEntity.ok(ranks.stream().map(e ->KeywordRankResponseMapper.INSTANCE.of(e)).collect(Collectors.toList()));
    }
    @Mapper
    interface KeywordRankResponseMapper {
        KeywordRestController.KeywordRankResponseMapper INSTANCE = Mappers.getMapper(KeywordRestController.KeywordRankResponseMapper.class);

        @Mappings({
                @Mapping(source = "name", target = "keyword")
        })
        KeywordRestController.KeywordRankResponse of(KeywordRankInterface rank);
    }
    @Getter
    @Builder
    static class KeywordRankResponse {
        private String keyword;
        private Long count;
    }

}
