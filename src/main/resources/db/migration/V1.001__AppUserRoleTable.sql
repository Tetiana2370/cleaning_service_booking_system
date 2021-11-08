create table app_user_role (
                          id_app_user_role bigint generated always as identity primary key,
                          name varchar(50) unique not null,
                          admin int default 0,
                          active int default 1
);

insert into app_user_role (name, admin, active) values ('admin', 1, 1);
insert into app_user_role (name, admin, active) values ('guest', 0, 1);
insert into app_user_role (name, admin, active) values ('employee', 0, 1);
insert into app_user_role (name, admin, active) values ('customer', 0, 1);
