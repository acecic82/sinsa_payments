# 무신사 페이먼츠 과제

## 기능
적립, 적립취소, 사용, 사용취소

## 프로젝트 설명

이 프로젝트는 무료포인트에 대한 정책과 무료포인트 사용및 취소를 간단하게 해볼 수 있는 프로그램입니다.

#### 정책
- 1회 최대 적립금액 정책
- 최대 보유 적립금액 정책
- 만료일 정책

#### 기능

포인트 적립
- 1회 최대 포인트 적립 금액이 존재하며 수기로도 적립이 가능하다

포인트 취소
- 포인트를 사용하지 않았다면 취소가 가능하다. 만료가 되었다면 취소할 수 없다

포인트 사용
- 보유한 포인트에 한하여 사용이 가능하다.

포인트 사용 취소
- 사용한 포인트 중 일부 혹은 전체 취소가 가능하다

동시성 처리
- 비관적락 사용

## 요구 사항

(문제 유출 방지를 위해 생략합니다)

## API
[API](API.md) 를 참고 해주세요

## AWS
[AWS](AWS.md) 를 참고해 주세요

## ERD
[ERD](ERD.md) 를 참고 해주세요

### Redis 활용처
[REDIS](REDIS.md) 를 참고 해주세요

### Setting
application.yml 파일에 spring.jpa.hibernate.ddl-auto 가 create 로 되어있어 서버 재 시작시 테이블이 날라갑니다\
h2Database 사용으로 컴퓨터에 h2Database 가 설치 되어 있어야 합니다.

### DB read-write 분리

DB를 read db, write db 를 분리하여 read 작업은 read db만 할 수 있게 개선
write db 부하 감소

readDB : 3308 port (docker)
writeDB : 3307 port (docker)

TODO
docker-compose 파일에도 반영 필요

### 아쉬운 부분

- Spring Security 를 이용하여 인가를 하면 더 좋을것 같다.
- 중간에 구조를 갈아엎느라 Service 의 테스트코드를 작성하지 못했다.
- Schedule 로 만료가 있는지 확인하고 업데이트 하는 처리를 했지만, Spring Batch 가 더 적합할 것이다.