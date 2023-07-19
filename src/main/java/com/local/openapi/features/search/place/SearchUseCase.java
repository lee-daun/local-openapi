package com.local.openapi.features.search.place;

import com.local.openapi.core.client.KakaoClient;
import com.local.openapi.core.client.NaverClient;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchUseCase {
    private final KakaoClient kakaoClient;
    private final NaverClient naverClient;
    private final int MAX_SIZE=10;

    @Cacheable(cacheNames = {"place:search:"}, cacheManager = "ehCacheCacheManager", key = "{#keyword}" )
    public List<Place> searchByKeyword(String keyword) {
        KakaoClient.SearchResponse kakaoResponse = kakaoClient.localSearchByKeyword(keyword);
        NaverClient.SearchResponse naverResponse = naverClient.localSearchByKeyword(keyword);

        List<Place> kakao = new ArrayList<>();
        if(kakaoResponse.getDocuments()!=null){
            kakao.addAll(kakaoResponse.getDocuments().stream()
                    .map(e -> PlaceMapper.INSTANCE.of(e))
                    .collect(Collectors.toList()));
        }
        if(naverResponse.getItems()!=null){
            kakao.addAll(naverResponse.getItems().stream()
                    .map(e -> PlaceMapper.INSTANCE.of(e))
                    .collect(Collectors.toList()));
        }


        int size = kakao.size();
        for(int i = 0; i < size; i++){
            Place p1 = kakao.get(i);
            //같은 업체가 있는지 확인.
            Optional<Place> optional = kakao.stream().filter(e -> !e.equals(p1) && e.eq(p1)).findFirst();
            if(optional.isPresent()){
                Place p2= optional.get();
                p1.channelSum+=p2.channel;
            }
        }

        //중복된 검색 결과는 카카오 결과를 제외한 나머지 제거.
        kakao = kakao.stream()
                .filter(e -> !(e.channelSum==3 && e.channel!=1))
                .sorted(Comparator.comparing((Place p) -> p.getChannel())
                        .thenComparing((Place p) -> p.getChannelSum(),Comparator.reverseOrder()))
                .limit(MAX_SIZE)
                .collect(Collectors.toList());
        return kakao;
    }



    @Mapper
    interface PlaceMapper {
        SearchUseCase.PlaceMapper INSTANCE = Mappers.getMapper(SearchUseCase.PlaceMapper.class);

        @Mappings({
                @Mapping(source = "place_name", target = "title", qualifiedByName = "htmlReplace")
                ,@Mapping(source = "place_url", target = "link")
                ,@Mapping(source = "category_name", target = "category")
                ,@Mapping(source = "address_name", target = "address")
                ,@Mapping(source = "road_address_name", target = "roadAddress")
                ,@Mapping(source = "phone", target = "tel")
                ,@Mapping(target = "channel",constant = "1" )
                ,@Mapping(target = "channelSum", constant = "1")
        })
        SearchUseCase.Place of(KakaoClient.Document document);

        @Mappings({
                @Mapping(source = "title", target = "title", qualifiedByName = "htmlReplace")
                ,@Mapping(source = "telephone", target = "tel")
                ,@Mapping(target = "channel", constant = "2")
                ,@Mapping(target = "channelSum",constant = "2")
        })
        SearchUseCase.Place of(NaverClient.Item item);


        @Named("htmlReplace")
        default String htmlReplace(String html) {
            return HtmlUtils.htmlUnescape(html).replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        }
    }


    @Getter
    @Builder
    static class Place {
        private String title;
        private String category;
        private String address;
        private String roadAddress;
        private String tel;
        private String link;
        private Integer channel;// 1: 카카오, 2: 네이버
        private Integer channelSum; //bit연산

        public boolean eq(Place target){
            String t1 = title.replaceAll(" ","").toLowerCase();
            String[] a1=address.split(" ");

            String t2 = target.getTitle().replaceAll(" ","").toLowerCase();
            String[] a2=target.getAddress().split(" ");

            if(t1.equals(t2)){
                String address1=(a1[0].substring(0,2)+a1[1]+a1[2]+a1[3]);
                String address2=(a2[0].substring(0,2)+a2[1]+a2[2]+a2[3]);
                if(address1.equals(address2)){
                    return true;
                }
            }
            return false;
        }
    }
}
