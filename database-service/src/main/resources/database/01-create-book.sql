--liquibase formatted sql
--changeset apiotrowska:1
CREATE TABLE book (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR NOT NULL,
    author VARCHAR NOT NULL,
    publication_year INT NOT NULL,
    pages INT NOT NULL
);