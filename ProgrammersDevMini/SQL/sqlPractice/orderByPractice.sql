SHOW DATABASES;
USE prod;
SHOW TABLES;

SELECT * 
FROM prod.session;

SELECT *
FROM session;

SELECT id, user_id, channel_id
FROM prod.session
LIMIT 10;

SELECT channel_id
FROM prod.session;

-- 유일한 채널 ID를 알고싶을 때
SELECT DISTINCT channel_id
FROM prod.session;

SELECT channel_id, COUNT(1)
FROM prod.session
GROUP BY 1;

SELECT channel_id, COUNT(1)
FROM prod.session
GROUP BY channel_id;

-- 테이블의 모든 레코드 표시 
SELECT COUNT(1)
FROM session;

SELECT *
FROM channel;

SELECT COUNT(1)
FROM session
WHERE channel_id = 5; -- WHERE channel_id in(5)

-- CASE WHEN
SELECT DISTINCT channel_id,
	CASE
		WHEN channel_id IN (1, 5, 6)  THEN 'Social-Media'
        WHEN channel_id IN (2, 4) THEN 'Search-Engine'
        ELSE 'Something-Else'
	END channel_type
FROM session;

SELECT * FROM prod.count_test;
SELECT COUNT(*) FROM prod.count_test;

SELECT COUNT(1)
FROM session
WHERE channel_id IN (4, 5);

SELECT COUNT(1)
FROM channel
WHERE channel LIKE '%G%';

SELECT DISTINCT channel
FROM channel
WHERE channel LIKE '%o%';

SELECT DISTINCT channel
FROM channel
WHERE channel NOT LIKE '%o%';


-- String 함수
SELECT
	LENGTH(channel),
    UPPER(channel),
    LOWER(channel),
    LEFT(channel, 4), -- 앞에서부터 4글자만 출력
    RPAD(channel, 15, '-') -- 오른쪽에 최대 15글자까지 '-'를 패딩한다
FROM channel;

SELECT value
FROM count_test
ORDER BY value DESC;

SELECT value
FROM count_test
ORDER BY 1 DESC; -- SELECT의 첫 번째 필드(value)로 내림차순 정렬

SELECT id, channel_id
FROM session
ORDER BY 2 DESC, 1 DESC;

SELECT
	LEFT(created, 7) AS mon,
    COUNT(1) AS sessionCount
FROM prod.session
GROUP BY 1 -- GROUP BY mon, GROUP BY LEFT (created, 7)
ORDER BY 1;

-- 내 코드 ;;
SELECT
	channel_id AS Most_channel 
FROM session
GROUP BY 1
ORDER BY 1;

-- 예제 코드
SELECT
	channel_id,
    COUNT(1) AS session_count,
    COUNT(DISTINCT user_id) AS user_count
FROM session
GROUP BY 1		-- GROUP BY channel_id
ORDER BY 2 DESC; -- ORDER BY session_count DESC



SELECT 
	channel_id,
    COUNT(1) AS session_count,
    COUNT(user_id) AS user_count
FROM session
GROUP BY 1
ORDER BY 2 DESC; -- session_count DESC


SELECT
	user_id,
    COUNT(1) AS session_count	
FROM session
GROUP BY 1 			-- GROUP BY user_id
ORDER BY 2 DESC		-- ORDER BY count DESC
LIMIT 1;

SELECT 
    LEFT(created, 7) AS month,
	COUNT(DISTINCT user_id) AS user_count
FROM session
GROUP BY 1
ORDER BY 1;


SELECT 
	LEFT(s.user_id, 7),
	COUNT(DISTINCT c.id),
    c.channel
FROM session s JOIN channel c ON s.channel_id = c.id 
GROUP BY 3
ORDER BY 1;


SELECT 
	LEFT(s.created, 7) AS mon,
    c.channel,
    COUNT(DISTINCT user_id) AS MAU
FROM session s JOIN channel c ON s.channel_id = c.id 
GROUP BY 1, 2
ORDER BY 1 DESC, 2;

SELECT 
	LEFT(s.created, 7) AS mon,
    c.channel,
    COUNT(user_id) AS MAU
FROM session s JOIN channel c ON s.channel_id = c.id 
GROUP BY 1, 2
ORDER BY 1 DESC, 2;


CREATE TABLE prod.vital(
	user_id int NOT NULL,
    vital_id int,
    date timestamp NOT NULL,
    weight int NOT NULL,
    primary key(vital_id)
);

CREATE TABLE prod.alert (
	alert_id int,
    vital_id int,
    alert_type varchar(32),
    date timestamp,
    user_id int,
    primary key(alert_id)
);

-- 레코드 추가 pk는 유일성 보장 : 키 값이 같은 레코드가 존재하면 안 됨
INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(100, 1, '2020-01-01', 75);
INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(100, 1, '2020-01-02', 78);
INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(100, 1, '2020-01-01', 90);
INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(100, 1, '2020-01-02', 95);
INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(999, 5, '2020-01-02', -1);  -- weight 필드에 음수값이 들어갔다
INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(999, 5, '2020-01-02', 10);  -- primery key로 지정된 필드의 값이 중복된다.

INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(1, 4, '2020-01-02', 101);
INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(2, NULL, '2020-01-04', 100);
INSERT INTO prod.vital(user_id, vital_id, date, wight) VALUES(3, NULL, '2020-01-04', 101);  -- 


DELETE FROM prod.vital WHERE wight <= 0;
DELETE FROM prod.vital;
SELECT * FROM prod.vital;



SELECT * FROM prod.vital WHERE vital_id = 4;

UPDATE prod.vital
SET weight = 92
WHERE vital_id = 4;

SELECT * FROM prod.alert WHERE vital_id IS NOT NULL;

DELETE FROM prod.vital WHERE vital_id = 5;
SELECT * FROM prod.vital;