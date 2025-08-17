create table if not exists users (
cpf VARCHAR(255) primary key,
password VARCHAR(255) not null,
data_criacao timestamp DEFAULT CURRENT_TIMESTAMP,
role VARCHAR(50)
);

create table if not exists users_config(
id bigserial primary key,
name varchar(255),
email varchar(255) not null unique,
phone_number varchar(255) not null,
date_birthday date not null,
address varchar(255) not null
);