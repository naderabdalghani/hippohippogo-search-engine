create schema mydb collate utf8mb4_0900_ai_ci;

create table dummy_items
(
    _id int auto_increment
        primary key,
    name varchar(100) null,
    query varchar(255) not null
);

create table hibernate_sequence
(
    next_val bigint null
);

create table page_rank
(
    page varchar(255) not null
        primary key,
    `rank` float not null,
    out_links int null
);

create table pages
(
    link varchar(100) not null
        primary key,
    content varchar(5000) null
);

create table images
(
	id int auto_increment,
	description longtext null,
	link longtext null,
	constraint images_pk
		primary key (id)
);

create table pages_connection
(
    id int not null
        primary key,
    referred varchar(255) null,
    referring varchar(255) null
);

create table search_queries
(
    query varchar(255) not null
        primary key,
    hits int default 1 not null
);

create table words
(
    id int not null
        primary key,
    word varchar(255) null,
    doc_id int null,
    index_of_word int null
);

