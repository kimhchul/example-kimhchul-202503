
1. 구현범위에대한설명
   1-1. 4개의 api구현
   	- API #1. 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
         	- URI : /query/getLowPriceByCtg
   	- API #2. 단일 브랜드로 모든 상품구매시, 최저가격 판매 브랜드와 카테고리 가격및 총액 조회 API
   	  	- URI : /query/getLowPriceByBrand
   	- API #3. 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가겨을 조회하는 API
         	- URI : /query/getCtgInfo
   	  	- param : 카테고리명
   	- API #4. 브랜드 및 상품을 추가/변경/삭제 API
         	- URI : /command/changeInfo
4. 코드빌드,테스트,실행방법
   - mac OS, intellij, jdk 17+ 환경에서 개발됨
   - h2, swagger 추가
   - ProductApiApplication.java
      - 프로그램 실행
   - ProductIntegrationTest.java
      - 테스트코드 실행
6. 기타추가정보
   - Swagger UI API 페이지 추가
     	http://localhost:8080/swagger-ui/index.html
   - 응답 객체의 DTO 정보
     - API #1. ResponseLowPriceByCtgDto
     - API #2. ResponseLowPriceByBrandDto
     - API #3. ResponseCtgInfoDto
     - API #4. String
     - 공통 ResponseCtgDto
       	각 API 1 ~ 3의 응답객체 세부 List Dto로 공통 사용
    - DB Table 설계
      특별한 설계없이 제공된 데이터를 수용할수있는 Table 생성(h2 auto-create)

