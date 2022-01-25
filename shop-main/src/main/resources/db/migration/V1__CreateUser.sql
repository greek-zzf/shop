create table USER
(
    ID         bigint primary key auto_increment,
    NAME       varchar(100),
    TEL        varchar(100) unique,
    AVATAR_URL varchar(1024),
    ADDRESS varchar(100),
    CREATE_AT  timestamp,
    UPDATE_AT  timestamp

);
INSERT INTO USER (NAME, TEL, AVATAR_URL, ADDRESS)
VALUES ('k ongmu', '13134070272', 'http://url', '火星');
INSERT INTO USER (NAME, TEL, AVATAR_URL, ADDRESS)
VALUES ('kongmu4', '13426777854', 'http://url', '火星');