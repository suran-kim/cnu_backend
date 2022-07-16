-- Trigger 
-- # 1
DESCRIBE test.suRan_name_gender; -- 스키마를 보여주는 명령어

CREATE TRIGGER 트리거이름
{BEFORE | AFTER} {INSERT | UPDATE | DELETE}
ON 테이블이름 FOR EACH ROW  -- 다수의 레코드가 동시에 변경되는 경우 레코드별 트리거 호출
트리거 로직;

-- # 2
-- trigger 테스트 케이스 : 변경이 일어난 시간과 
-- audit 용도로 테이블 생성 
DROP TABLE IF EXISTS test.suranNameGenderAudit;
CREATE TABLE test.suranNameGenderAudit (
	name varchar(16),
    gender enum('Male', 'Female'),
    modified timestamp
);

-- 테이블의 내용이 변경되기 직전에 실행되는 트리거. 
DROP TRIGGER IF EXISTS test.beforeUpdateSuranNameGender;

CREATE TRIGGER test.beforeUpdateSuranNameGender
	BEFORE UPDATE ON test.suRan_name_gender
    FOR EACH ROW
INSERT INTO test.suranNameGenderAudit
SET name =  OLD.name, -- 바뀌기 전 이름
	gender = OLD.gender,
	modified = NOW();



-- test.suRan_name_gender테이블의 내용 변경    
UPDATE test.suRan_name_gender
SET name = 'Benjamin'
WHERE name = 'Ben';

SELECT * FROM test.suRan_name_gender;
SELECT * FROM test.suranNameGenderAudit;

USE prod;
USE test;
SHOW TRIGGERS;
    
-- 트리거 적용 안 되는 오류 -> how?