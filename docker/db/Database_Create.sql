create database dialog_db;
\connect dialog_db;
create table admin_accounts(
	id serial primary key,
	username varchar(50) not null unique,
	password varchar(100) not null,
	email varchar(200) not null unique,
	displayname varchar(50),
    salt char(6) not null,
	create_date timestamp default current_timestamp,
	is_online boolean default false
);
create table user_accounts(
	id serial primary key,
    username varchar(50) not null unique,
    password varchar(100) not null ,
    email varchar(200) not null unique,
    salt char(6) not null,
    is_online boolean default false,
    is_lock boolean default false,
	create_date timestamp default current_timestamp
);

create table user_account_info(
    account_id int primary key references user_accounts(id),
	displayname varchar(50),
	sex boolean not null,
	address varchar(200)

);

create table box_chats (
	id serial primary key,
	box_name varchar(50) not null,
	is_direct boolean default true,
	create_date timestamp default current_timestamp
);

create table messages(
	id serial primary key,
	user_id int references user_accounts(id),
	box_id int references box_chats(id),
	content text not null,
	create_date timestamp default current_timestamp
);

create table reports(
	id serial primary key,
	reported_id int references user_accounts(id),
	reporter_id int references user_accounts(id),
	create_date timestamp default current_timestamp
);

create table friendships(
	request_id int references user_accounts(id),
	accept_id int references user_accounts(id),
	is_accepted boolean default false,
	primary key(request_id, accept_id)
);

create table block_lists(
	id serial primary key,
	user_id int references user_accounts(id),
	block_id int references user_accounts(id)
);

create table box_chat_members(

	box_id int references box_chats(id),
	user_id int references user_accounts(id),
	is_admin boolean default false ,
	create_date timestamp default current_timestamp,
	primary key(box_id, user_id)
);

create table user_activity_logs(
	user_id int references user_accounts(id),
	session_start timestamp default current_timestamp,
	session_end timestamp,
	primary key (user_id, session_start)
);