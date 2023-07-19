package com.local.openapi.features.search.place;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.local.openapi.core.aop.KeywordLog;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
@Validated
public class SearchRestController {
    private final SearchUseCase placeUseCase;


    @KeywordLog(serviceName = "places")
    @GetMapping(path = "/places")
    ResponseEntity<List<PlaceResponse>> getPlaces(@RequestParam(value = "keyword") @NotBlank String keyword){


        List<SearchUseCase.Place> places = placeUseCase.searchByKeyword(keyword);

        return ResponseEntity.ok(places.stream().map(e -> PlaceResponseMapper.INSTANCE.of(e)).collect(Collectors.toList()));
    }


    @Getter
    @Builder
    static class PlaceResponse {
        private String title;
        private String category;
        private String address;
        private String roadAddress;
        private String tel;
        private String link;
        @JsonIgnore
        private Integer channel;
        @JsonIgnore
        private Integer channelSum;


    }

    @Mapper
    interface PlaceResponseMapper {
        PlaceResponseMapper INSTANCE = Mappers.getMapper(PlaceResponseMapper.class);

        PlaceResponse of(SearchUseCase.Place place);
    }

}
