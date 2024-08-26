create table if not exists person (
    id serial primary key not null,
    login varchar(2000) unique,
    password varchar(2000)
);

insert into person (login, password) VALUES ('ivanov', '123');
insert into person (login, password) VALUES ('petrov', '111');
insert into person (login, password) VALUES ('111', '111');

