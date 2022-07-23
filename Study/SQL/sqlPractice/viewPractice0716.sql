CREATE OR REPLACE VIEW 뷰이름 AS SELECT 질의 쿼리;

SHOW databases;
USE prod;

SHOW tables;

SELECT *
FROM test;

-- 자주 사용하는 쿼리
SELECT s.id, s.user_id, s.created, s.channel_id, c.channel
FROM session s
JOIN channel c ON c.id = s.channel_id;

CREATE OR REPLACE VIEW test.suranSessionDetails AS
SELECT s.id, s.user_id, s.created, s.channel_id, c.channel
FROM session s
JOIN channel c ON c.id = s.channel_id;

SELECT * 
FROM test.suranSessionDetails
WHERE id = 7;

