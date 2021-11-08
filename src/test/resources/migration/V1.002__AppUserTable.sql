create table app_user (
                        id_app_user bigint generated always as identity primary key,
                        id_app_user_role bigint not null,
                        username varchar(50) unique not null,
                        password varchar(100) not null,
                        first_name varchar(100) not null,
                        last_name varchar(100) not null,
                        email_address varchar(320),
                        phone_number varchar(20),
                        active int default 1,
                        version int not null default 0,
                        constraint fk_app_user_role
                        foreign key(id_app_user_role)
                        references app_user_role(id_app_user_role)
);

insert into app_user (id_app_user_role, username, password, first_name, last_name, email_address, phone_number)
values (1, 'admin', '$2a$10$1X6xcS3b5fpgOQ50z9k.XunBXTs0AS7oRheuG8c6iVAXPDROk9DFO', 'Tetiana', 'Kravchuk', 't@gmail.com', '555000');