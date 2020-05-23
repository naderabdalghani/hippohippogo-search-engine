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

create table mydb.page_rank
(
	page varchar(255) not null
		primary key,
	`rank` double not null,
	out_links int not null
);


create table mydb.pages
(
	id int auto_increment
		primary key,
	link longtext null,
	title longtext null,
	content longtext null,
	length int null,
	description varchar(255) null
);



create table mydb.pages_connection
(
	id int auto_increment
		primary key,
	referred varchar(100) null,
	referring varchar(100) null
);



create table search_queries
(
    query varchar(255) not null
        primary key,
    hits int default 1 not null
);

create table mydb.words
(
	id bigint auto_increment
		primary key,
	word varchar(100) not null,
	doc_id int not null,
	index_of_word int not null
);



