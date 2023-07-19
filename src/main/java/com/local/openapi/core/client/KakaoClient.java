package com.local.openapi.core.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface KakaoClient {

    /**
     * @Param keyword - 질의어에서 지역 정보를 제외한 키워드 (예: '중앙로 맛집' 에서 '맛집')
     * @Param size - 한 페이지에 보여질 문서의 개수 (기본값: 15)
     * @Param page - 결과 페이지 번호 (기본값: 1)
     *
     * @Return SearchResponse
     * */
    SearchResponse localSearchByKeyword(String keyword);



    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Document{
        private String place_name;//장소명, 업체명
        private String distance;//중심좌표까지의 거리 (단, x,y 파라미터를 준 경우에만 존재) 단위 meter
        private String place_url;//장소 상세페이지 URL
        private String category_name;//카테고리 이름
        private String address_name;//전체 지번 주소
        private String road_address_name;//전체 도로명 주소
        private String id;//장소 ID
        private String phone;//전화번호
        private String category_group_code;//중요 카테고리만 그룹핑한 카테고리 그룹 코드
        private String category_group_name;//중요 카테고리만 그룹핑한 카테고리 그룹
        private String x;//X 좌표값, 경위도인 경우 longitude (경도)
        private String y;//Y 좌표값, 경위도인 경우 latitude(위도)
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    class Meta{
        private SameName same_name;//질의어의 지역 및 키워드 분석 정보
        private int pageable_count;//total_count 중 노출 가능 문서 수 (최대: 45)
        private int total_count;//검색어에 검색된 문서 수
        private boolean is_end;//현재 페이지가 마지막 페이지인지 여부 값이 false면 다음 요청 시 page 값을 증가시켜 다음 페이지 요청 가능
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    class SearchResponse{
        private Meta meta;
        private List<Document> documents;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    class SameName{
        private List<Object> region;//질의어에서 인식된 지역의 리스트 예: '중앙로 맛집' 에서 중앙로에 해당하는 지역 리스트
        private String keyword;//질의어에서 지역 정보를 제외한 키워드 예: '중앙로 맛집' 에서 '맛집'
        private String selected_region;//인식된 지역 리스트 중, 현재 검색에 사용된 지역 정보
    }


}
