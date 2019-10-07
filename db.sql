DROP DATABASE IF EXISTS ds4;

CREATE DATABASE ds4;

USE ds4;

# drop table article;

CREATE TABLE article(
	id INT(20),
	regDate DATETIME NOT NULL,
	sourceId INT(10),
	webPath VARCHAR(100) NOT NULL,
	title VARCHAR(100) NOT NULL,
	`body` LONGTEXT,
	writer VARCHAR(100) NOT NULL,
	colDate DATETIME NOT NULL,
	PRIMARY KEY(id,sourceId)
);

INSERT INTO article (
	id,regDate,sourceId,webPath,title,`body`,writer,colDate
) VALUES (
	249155, NOW(), 2, "t", "t", "t", "t", NOW()
),(
	249154, NOW(), 2, "test", "test", "test", "test", NOW()
)
 ON DUPLICATE KEY UPDATE
	regDate = VALUES(regDate),
	webPath = VALUES(webPath),
	webPath = VALUES(webPath),
	webPath = VALUES(webPath),
	`body` = VALUES(`body`),
	colDate = VALUES(colDate)

DESC article;

# truncate article;

SELECT *
FROM article
ORDER BY regDate DESC;

SELECT COUNT(*)
FROM article;


# drop table `source`;

CREATE TABLE `source`(
	id INT(10) PRIMARY KEY,
	regDate DATETIME NOT NULL,
	site VARCHAR(100) NOT NULL,
	category VARCHAR(100) NOT NULL,
	siteUrl VARCHAR(100) NOT NULL,
	listUrl VARCHAR(100) NOT NULL,
	dateformat VARCHAR(100) NOT NULL
);

DESC `source`;

# truncate `source`;

INSERT INTO `source`
VALUES (1,NOW(),"인사이트","패션","https://www.insight.co.kr","/section/fashion","yyyy-MM-dd hh:mm:ss"),
(2,NOW(),"인사이트","음식·맛집","https://www.insight.co.kr","/section/food","yyyy-MM-dd hh:mm:ss"),
(3,NOW(),"위키트리","패션뷰티","https://www.wikitree.co.kr","/main/list.php?nc_id=82","yyyy-MM-dd hh:mm:ss"),
(4,NOW(),"위키트리","푸드","https://www.wikitree.co.kr","/main/list.php?nc_id=81","yyyy-MM-dd hh:mm:ss");


SELECT *
FROM `source`
ORDER BY regDate DESC;