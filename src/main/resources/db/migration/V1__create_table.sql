create table users_jpa (
    user_id serial primary key,
    user_first_name varchar(255) not null,
    user_last_name varchar(255) not null,
    unique(user_first_name, user_last_name)
);