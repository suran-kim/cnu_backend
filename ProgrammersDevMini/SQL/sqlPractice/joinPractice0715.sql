SHOW databases;

CREATE TABLE vital(
	userID int NOT NULL,
    vitalID int, 
    date timestamp NOT NULL,
    weight int NOT NULL,
    primary key(vitalID)
);

CREATE TABLE alert(
	alertID int,
    vitalID int,
    alertType varchar(32),
    date timestamp,
    userID int,
    primary key(alertID)
);


SHOW tables;

DROP TABLE vital;

SELECT * FROM vital;
SELECT * FROM alert;

INSERT INTO vital(userID, vitalID, date, weight) VALUES(100, 1, '2020-01-01', 75);
INSERT INTO vital(userID, vitalID, date, weight) VALUES(100, 3, '2020-01-02', 78);
INSERT INTO vital(userID, vitalID, date, weight) VALUES(101, 2, '2020-01-01', 90);
INSERT INTO vital(userID, vitalID, date, weight) VALUES(101, 4, '2020-01-02', 95);


INSERT INTO alert VALUES(1, 4, 'WeightIncrease', '2020-01-02', 101);
INSERT INTO alert VALUES(2, NULL, 'MissingVital', '2020-01-04', 100);
INSERT INTO alert VALUES(3, NULL, 'MissingVital', '2020-01-04', 101);  


-- INNER JOIN
SELECT * FROM vital v
JOIN alert a ON v.vitalID = a.vitalID;

SELECT * FROM vital v
LEFT JOIN alert a ON v.vitalID = a.vitalID;

--- FULL JOIN
SELECT * FROM vital v
LEFT JOIN alert a ON v.vitalID = a.vitalID
UNION
SELECT * FROM vital v
RIGHT JOIN alert a ON v.vitalID = a.vitalID;

SELECT * FROM vital v
LEFT JOIN alert a ON v.vitalID = a.vitalID
UNION ALL
SELECT * FROM vital v
RIGHT JOIN alert a ON v.vitalID = a.vitalID;

SELECT * FROM alert a
RIGHT JOIN vital v ON v.vitalID = a.vitalID;


-- CROSS JOIN
SELECT * FROM vital v
CROSS JOIN alert a;

-- SELF JOIN
SELECT * FROM vital v1
JOIN vital v2 ON v1.vitalID = v2.vitalID;