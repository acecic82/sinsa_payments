# REDIS

정책 파일의 값을 가져올 때 DB를 찌르지 않고 redis 를 먼저 확인하게 구성\
이유 : 요청이 들어오면 정책을 자주 확인하게 될텐데 DB의 부하를 줄여주기 위한 아이디어

### 정책 파일
1. 1회 최대 적립금에 대한 정책
2. 최대 보유 포인트에 대한 정책
3. 만료 날짜에 대한 정책

### 방식

1. 먼저 redis 의 값을 확인한다.
2. 값이 있다면 redis 의 값을 사용한다.
3. 없다면 db 에서 정책 정보를 가져오고 Redis 에 셋팅 해둔다.

참고로 정책을 셋팅할 때 redis 도 같이 셋팅한다.