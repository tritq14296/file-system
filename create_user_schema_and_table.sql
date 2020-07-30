CREATE USER tritq1 IDENTIFIED BY quoctri1;

GRANT ALL PRIVILEGES TO tritq1 IDENTIFIED BY quoctri1;

--create schema
CREATE SCHEMA AUTHORIZATION TRITQ1
    CREATE TABLE users
        ( user_id number(10) not null,
          user_name varchar2(50) not null,
          password varchar2(500) not null,
          CONSTRAINT users_pk PRIMARY KEY (user_id)
         )
    CREATE TABLE user_file (
            user_file_id number(10) not null,
            user_id number(10),
            file_name varchar2(4000),
            CONSTRAINT user_file_pk PRIMARY KEY (user_file_id),
            CONSTRAINT user_file_fk FOREIGN KEY (user_id) REFERENCES users(user_id)
        );

--create seq to auto increment id
CREATE SEQUENCE USER_FILE_SEQ
START WITH 1
MAXVALUE 999999999999999999999999999
MINVALUE 1
NOCYCLE
CACHE 20
NOORDER;

--users
CREATE SEQUENCE USER_SEQ
START WITH 1
MAXVALUE 999999999999999999999999999
MINVALUE 1
NOCYCLE
CACHE 20
NOORDER;

--insert new user with testSys1/Admin@12
insert into users(user_id, user_name, password) VALUES(1, 'testSys1', '$2a$10$Vt.WUTe6vpqdh3Z4WiHhwOqwZIW25hYwG/mx.cTW5zEKcZIiD.jPO');
--tritq1/password
insert into users(user_id, user_name, password) VALUES(2, 'tritq1', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6');
commit;
