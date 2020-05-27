create schema mydb collate utf8mb4_0900_ai_ci;

create table hibernate_sequence
(
    next_val bigint null
);

create table images
(
    id int auto_increment
        primary key,
    image_link longtext not null,
    source_link longtext not null,
    title longtext not null,
    length int not null,
    region varchar(2) null,
    date_published date null
);

create table images_words
(
    id int auto_increment
        primary key,
    word varchar(255) not null,
    doc_id int not null,
    index_of_word int not null
);

create table page_rank
(
    page varchar(255) not null
        primary key,
    `rank` double not null,
    out_links int not null
);

create table pages
(
    id int not null
        primary key,
    content longtext not null,
    link longtext not null,
    length int not null,
    title longtext not null,
    description longtext not null,
    region varchar(2) null,
    date_published date null
);

create table pages_connection
(
    id int not null
        primary key,
    referred varchar(255) not null,
    referring varchar(255) not null
);

create table users
(
    ip varchar(255) not null,
    query varchar(255) not null,
    hits int default 1 not null,
    region varchar(255) not null,
    person tinyint(1) null,
    primary key (ip, query, region)
);

create table users_frequent_domains
(
    domain varchar(255) not null,
    user_ip varchar(255) not null,
    hits int default 1 not null,
    primary key (domain, user_ip)
);

create table words
(
    id int not null
        primary key,
    word varchar(255) null,
    doc_id int null,
    index_of_word int null
);

