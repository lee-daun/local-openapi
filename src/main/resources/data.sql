CREATE TABLE keyword_log (
    id      BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name    VARCHAR(50),
    service_name    VARCHAR(20),
    created_at  TIMESTAMP
);