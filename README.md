# 오픈 API를 활용한 장소 검색 서비스

오픈 API를 활용하여 키워드를 통한 장소 검색 서비스를 제공.
<br>키워드에 따른 각 오픈 API 주체별 장소 검색 결과를 서비스에서 통합하여 제공한다.
### 시작하세요
1. 카카오 API 환경 설정.
   1. Kakao Developers 접속하여 어플리케이션 등록.
   <br>(https://developers.kakao.com/console/app)
   2. src/main/resources/application.yml
   ```json
     application:
      web-client:
        kakao:
          connect-timeout: {millisecond}
          read-timeout: {millisecond}
          response-timeout: {millisecond}
          write-timeout: {millisecond}
          host: https://dapi.kakao.com
          apikey: {application_key} //어플리케이션 Key
    ```
2. 네이버 API 어플리케이션 등록. 
   1. 네이버 개발자 센터 접속하여 어플리케이션 등록.
      <br>(https://developers.naver.com/main/)
   2. src/main/resources/application.yml
   ```json
     application:
      web-client:
        naver:
          connect-timeout: {millisecond}
          read-timeout: {millisecond}
          response-timeout: {millisecond}
          write-timeout: {millisecond}
          host: https://openapi.naver.com
          id: {application_id} //어플리케이션 id
          secret: {application_secret} //어플리케이션 secret
   ```
3. 프로젝트 실행
   ```json
     mvn spring-boot:run
   ```

### API
1. 장소 검색
   1. sample <br>```curl --location 'http://localhost:8080/search/places?keyword=카카오'```
   2. 기본 정보 
   - |메서드| URL                                 |
     |----|-------------------------------------|
     | GET | http://localhost:8080/search/places | 
   3. 쿼리 파라미터
    - |이름|타입|설명| 필수 |
      |--|--|--|--|
      |keyword|String|검색을 원하는 키워드|O|
   4. 응답 헤더
   - | 이름 | 설명                                           | 필수   |
     |--------|----------------------------------------------|------|
     | Content-type | 응답 데이터 타입<br> content-type: application/json | O |
   5. 응답 본문
   - | 이름          | 타입     | 설명        |
     |-------------|--------|-----------|
     | title       | String | 장소명,업체명   |
     | category    | String | 카테고리 이름   |
     | address     | String | 전체 지번 주소  |
     | roadAddress | String | 전체 도로명 주소 |
     | tel         | String | 연락처       |
     | link        | String | 장소 상세 URL |

2. 키워드 검색
    1. sample <br>```curl --location 'http://localhost:8080/keyword/ranks'```
    2. 기본 정보
    - |메서드| URL                                 |
           |----|-------------------------------------|
      | GET | http://localhost:8080/keyword/ranks | 
    3. 응답 헤더
    - | 이름 | 설명                                                        | 필수   |
           |--------|-----------------------------------------------------------|------|
      | Content-type | 응답 데이터 타입<br> content-type: application/json | O |
    4. 응답 본문
    - | 이름       | 타입     | 설명     |
      |----------|--------|--------|
      | keyword  | String | 키워드    |
      | count    | Long   | 검색 요청수 |
      


### 환경
- Java 11
- SpringBoot 2.7.13
- Maven3
- H2
- 카카오 API, 네이버 API

### 라이브러리
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- spring-boot-starter-webflux
- spring-boot-starter-aop
  - 장소 검색에 필요한 키워드 저장 처리에 사용.
  <br>(요청 키워드를 저장하여 검색 키워드 목록 제공.)
- spring-boot-starter-cache
- mapstruct
  - Java Bean 매핑 구현을 단순화 하는 코드 생성으로 사용.
- lombok
- ehcache
  - 검색 결과를 보다 빠르게 응답 하기 위해 캐시 사용.
  <br> (캐시 생명 주기 전략은 서비스 상황에 따라 유동적으로 설정 필요.)
- h2
  - 요청 키워드 저장 및 조회에 사용.
