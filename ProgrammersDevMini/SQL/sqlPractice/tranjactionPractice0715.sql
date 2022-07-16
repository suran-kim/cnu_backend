SHOW databases;

USE test;

DROP TABLE IF EXISTS test.suRan_name_gender; -- 만일 테이블이 이미 존재하면 삭제 (동일 DB를 쓰고있기 때문)
CREATE TABLE test.suRan_name_gender (
 name varchar(16) NOT NULL,
 gender enum('Male', 'Female') default NULL
);  -- enum 다음 주어진 값 중 하나만 가질 수 있는 타입, Male, Female, NULL 만 가질 수 있다. 

INSERT INTO test.suRan_name_gender VALUES('suRan', 'Female');
INSERT INTO test.suRan_name_gender VALUES('Jane', 'Female');
INSERT INTO test.suRan_name_gender(name) VALUES('UnKnown'); -- 테이블에 존재하는 필드 수보다 적은 값의 리스트를 지정하면 에러 발생. 필드이름 명시하면 해결 
INSERT INTO test.suRan_name_gender VALUES('superMan', 'Male2'); -- enum타입에 존재하지 않는 값이 주어지면 비어있는 문자열로 초기화

-- autocommit = True

SHOW VARIABLES LIKE 'AUTOCOMMIT'; 	-- return ON 
SET autocommit = 1; 				-- autocommit = 0은 false

BEGIN;
DELETE FROM test.suRan_name_gender;
INSERT INTO test.suRan_name_gender VALUES('Kei', 'Male');
ROLLBACK; -- BEGIN 이후 발생한 테이블 수정, 추가, 삭제 명령 취소 // END; COMMIT; 를 실행하는 순간

SELECT * FROM test.suRan_name_gender;

SELECT * FROM test.suRan_name_gender;

