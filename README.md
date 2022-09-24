# URL 단축 API : 권희운

## 사용 기술 스택 : java, spring boot, jpa, QueryDsl, Embedded H2 DB

## 빌드 방법
WORK_DIR :  shortener

jar 파일 빌드 (테스트코드 제외) \
./gradlew build -x test

## 서버 구동 방법
빌드된 jar 파일 실행 \
java -jar ./build/libs/shortener-0.0.1-SNAPSHOT.jar 


## 단위 테스트
Service, Repository 계층별 단위테스트 
