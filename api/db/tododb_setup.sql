DROP DATABASE todos;
CREATE DATABASE todos;
USE todos;

CREATE TABLE accounts
(
    pk_user_id VARCHAR(50) PRIMARY KEY,
    username VARCHAR(50),
    password VARCHAR(50)
);

CREATE TABLE tododata
(
    pk_todo_id VARCHAR(50) PRIMARY KEY,
    todoname VARCHAR(50),
    fk_pk_user_id VARCHAR(50) REFERENCES accounts (pk_user_id)
);

INSERT INTO accounts
VALUES
    ('sample', 'sample', 'sample'),
    ('sample2', 'sample2', 'sample2');

INSERT INTO tododata
VALUES
    ('1', 'sample1', 'sample'),
    ('2', 'sample2', 'sample'),
    ('3', 'sample3', 'sample2'),
    ('4', 'sample4', 'sample2');

SELECT *
FROM tododata;

SELECT *
FROM accounts;