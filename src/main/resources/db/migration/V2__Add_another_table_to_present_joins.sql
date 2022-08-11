create table address
(
    id           bigserial primary key,
    account_id   bigint       not null references account (id),
    full_address varchar(255) not null
);

create index if not exists idx_address_account_id on address (account_id);

insert into address(account_id, full_address)
values (1, 'Some Street in some city 12');
insert into address(account_id, full_address)
values (2, 'Another nice street somewhere');
