package com.local.openapi.core.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface NaverClient {

    /**
     * 검색 API를 사용해 네이버 지역 서비스에 등록된 업체 및 기관을 검색한 결과를 XML 형식 또는 JSON 형식으로 반환합니다
     *
     * @Param keyword - 질의어에서 지역 정보를 제외한 키워드 (예: '중앙로 맛집' 에서 '맛집')
     *
     * @Return SearchResponse
     * */
    NaverClient.SearchResponse localSearchByKeyword(String keyword);


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Item{
        private String title; // 업체, 기관의 이름
        private String link; // 업체, 기관의 상세 정보 URL
        private String category;//업체, 기관의 분류 정보
        private String description;//업체, 기관에 대한 설명
        private String telephone;//값을 반환하지 않는 요소. 하위 호환성을 유지하기 위해 있는 요소입니다.
        private String address;//업체, 기관명의 지번 주소
        private String roadAddress;//업체, 기관명의 도로명 주소
        private String mapx;//업체, 기관이 위치한 장소의 x 좌표(KATECH 좌표계 기준). 네이버 지도 API에서 사용할 수 있습니다.
        private String mapy;//업체, 기관이 위치한 장소의 y 좌표(KATECH 좌표계 기준). 네이버 지도 API에서 사용할 수 있습니다.
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    class SearchResponse{
        private String lastBuildDate; //검색 결과를 생성한 시간
        private Integer total;//총 검색 결과 개수
        private Integer start;//검색 시작 위치
        private Integer display;//한 번에 표시할 검색 결과 개수
        private List<Item> items;//	개별 검색 결과. JSON 형식의 결괏값에서는 items 속성의 JSON 배열로 개별 검색 결과를 반환합니다.
    }

}
