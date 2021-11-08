create table permission (
                               id_permission bigint generated always as identity primary key,
                               name varchar(50) unique not null
);

insert into permission (name) values ('app_user');
insert into permission (name) values ('order');
insert into permission (name) values('customer_address');
