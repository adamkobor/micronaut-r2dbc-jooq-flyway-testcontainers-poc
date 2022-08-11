create table account
(
    id         bigserial primary key,
    name       VARCHAR(255) NOT NULL,
    deleted_at timestamp NULL

);

insert into account(name, deleted_at)
values ('John', null);
insert into account(name, deleted_at)
values ('Adam', now());
insert into account(name, deleted_at)
values ('Regina', null);
