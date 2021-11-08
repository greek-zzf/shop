create table USER
(
    ID bigint primary key auto_increment,
    NAME varchar(100),
    TEL varchar(100) unique,
    AVATAR_URL varchar(1024),
    CREATE_AT timestamp ,
    UPDATE_AT timestamp

)