
# 문제 해결 전략
## Db tables
### T_HOST

|Column |Type | Description|
|-|-|-|
|ID| BIGINT| Generated id|
|HOST_ID |VARCHAR |uri 의 hostname만 파싱하여 저장 (group by 를 위함)|
### T_VISITE_HISTORY

|Column |Type | Description|
|-|-|-|
|HISOTIRY_ID| BIGINT| Generated id|
|HOST_ID |BIGINT |T_HOST 를 참조하기위한 외래키|
|URI |VARCHAR |입력받은 uri|
|VISIT_TIME |TIMESTAMP |입력될때의 시간|


## 해결 전략

1. Revent visit
	* PagingAndSortingRepository를 이용해 pagination한다.
2. Most frequently visited
	* HOST_ID로 group by하여 HISTORY_ID의 count를 구한다.
	* count로 내림차순 정렬하여 상위 5개를 노출한다.
3. Least Recently visited
	* HOST_ID로 group by하여 VISIT_TIME의 max를 구한다.
	* max값으로 오름차순 정렬하여 상위 5개를 노출한다.

# 프로그램 실행방법

 1. 레파지토리 복사
	 * `git clone https://github.com/jongmin-k/work.`
2. 설정
	1. application.properties 생성
		* `./excute.sh -i`
		*  해당 command 의 결과로 ./conf/application.properties가 생성됨.
	2. server.port 변경
		* `./conf/application.properties 의 "server.port = 8080" 을 원하는 포트로 변경`
	 3. 실행
		* `./excute.sh`
		* Daemon
			* 실행 : `./excute.sh -d`
			* 종료 : `./excute.sh -s` 
	4. Web 접속
		*  `http://${ip}:${port}/`

 > **Note**  Default port(8080)를 사용하는경우엔 1, 2 의 과정은 무시하여도 된다. 
