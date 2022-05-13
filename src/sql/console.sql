drop DATABASE if exists cevapeDB;
create DATABASE if not exists cevapeDB;
use cevapeDB;

create table if not exists USER(
    pk_usr int primary key auto_increment,
    username varchar(10)
);

create table if not exists PASSIV(
    pk_passiv int primary key auto_increment,
    fk_pk_usr int,
    FOREIGN KEY (fk_pk_usr) references USER(pk_usr),
    multiplikator double
);

INSERT INTO USER
VALUE (1, 'Bogo');

INSERT INTO PASSIV
VALUE (1, 1, 1.0);

#INSERT INTO PASSIV (fk_pk_usr, multiplikator) VALUE(1, 1.0);