# concurrency-study

### inflearn - 재고시스템으로 알아보는 동시성이슈 해결방법

- synchronized
  - 구현이 간단함 
  - 다중 서버에서 사용할 수 없음
- pessimistic lock
  - 실제로 데이터에 lock을 걸어 데이터 정합성을 맞춤
  - dead lock 주의
  - row나 table 단위로 락을 검
  - 충돌이 빈번하게 일어난다면 optimistic lock보다 성능이 좋을 수 있음
  - lock을 통해 제어하기 때문에 데이터 정합성이 어느정도 보장됨
  - 별도의 락을 잡기 때문에 성능감소가 있을 수 있음
- optimistic lock
  - 버전을 이용하여 데이터 정합성을 맞춤
  - 별도의 lock을 잡지 않으므로 pessimistic lock보다 성능상 장점이 있음
  - 업데이트 실패시 재시도 처리를 직접 별도로 해주어야 함
  - 충돌이 빈번하게 일어나면 성능이 좋지 않음
- named lock
  - 이름을 가진 metadata lock을 획득하면 다른 세션은 이 lock이 해제되기 전까지 lock을 획득할 수 없음
  - transaction이 종료될 때 락이 자동으로 해제되지 않으므로 별도의 명령어 수행이나 선점시간이 끝나야 해제됨
  - 분산락을 구현할 때 사용
  - pessimistic lock은 timeout을 구현하기 힘들지만 named lock은 손쉽게 가능
  - 데이터 삽입시 정합성을 맞춰야 하는 경우에도 사용 가능
  - transaction 종료 시 lock 해제와 세션관리를 잘해줘야 함
  - 실제로 사용시 구현방법이 복잡할 수 있음(별도의 데이터소스, jdbc 사용 등)
- lettuce 
  - setnx 명령어를 활용하여 분산락 구현
  - 구현이 간단함
  - 스핀락 방식으로 레디스에 부하를 줄 수 있음(thread.sleep을 이용하여 락획득 텀을 주어야함)
  - 락 획득까지 재시도 처리를 직접 별도로 해주어야 함
  - 재시도가 필요하지 않은 경우 사용
- redisson
  - pub-sub 기반으로 lock 구현 제공
  - 별도 재시도 로직이 필요 없음(락 획득 재시도를 기본으로 제공)
  - 레디스의 부하를 줄여줌
  - lettuce에 비해 구현이 복잡하며 별도의 라이브러리를 사용해야함
  - 라이브러리 사용법을 알아야함
  - 재시도가 필요한 경우 사용
- mysql vs redis
  - mysql
    - 이미 mysql을 사용하고 있따면 별도의 비용없이 사용 가능
    - 어느정도의 트래픽까지는 문제 없음
    - redis보다 성능은 좋지 않음
  - redis
    - 활용중인 redis가 없다면 별도의 구축비용과 인프라 관리비용이 발생함
    - mysql보다 성능이 좋음