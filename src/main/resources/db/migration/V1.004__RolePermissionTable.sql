create table role_permission (
                            id_role_permission bigint generated always as identity primary key,
                            id_app_user_role bigint not null,
                            id_permission bigint not null,
                            read int default 1,
                            write int default 0,
                            constraint fk_app_user_role
                                foreign key(id_app_user_role)
                                    references app_user_role(id_app_user_role),
                            constraint ft_permission
                                foreign key (id_permission)
                                    references permission(id_permission)
);

