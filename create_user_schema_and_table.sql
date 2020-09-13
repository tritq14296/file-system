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
        )
    CREATE TABLE user_role (
        user_role_id number(10) not null,
        user_id number(10),
        user_role_name varchar2(50),
        CONSTRAINT user_role_pk PRIMARY KEY (user_role_id),
        CONSTRAINT user_role_fk FOREIGN KEY (user_id) REFERENCES users(user_id)
    )
    CREATE TABLE user_log (
        user_log_id number(10) not null,
        user_id number(10),
        user_log_in_time timestamp,
        user_log_out_time timestamp,
        CONSTRAINT user_log_pk PRIMARY KEY (user_log_id),
        CONSTRAINT user_log_fk FOREIGN KEY (user_id) REFERENCES users(user_id)
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

--users_role_seq
CREATE SEQUENCE USER_ROLE_SEQ
START WITH 1
MAXVALUE 999999999999999999999999999
MINVALUE 1
NOCYCLE
CACHE 20
NOORDER;

--users_log_seq
CREATE SEQUENCE USER_LOG_SEQ
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
--tritq2/password
insert into users(user_id, user_name, password) VALUES(3, 'tritq2', '$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6');
--insert user role
insert into user_role(user_role_id, user_id, user_role_name) VALUES(1, 3, 'ADMIN');
insert into user_role(user_role_id, user_id, user_role_name) VALUES(2, 2, 'MEMBER');
insert into user_role(user_role_id, user_id, user_role_name) VALUES(3, 1, 'MEMBER');
commit;