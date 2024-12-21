SET timezone = 'Asia/Calcutta';

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
    status varchar(10) default 'offline' check (status in ('offline', 'online', 'locked')),
	create_date timestamp default current_timestamp
);

create table user_account_info(
    account_id serial primary key references user_accounts(id) on delete cascade,
	displayname varchar(50),
	dob date,
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
	user_id int references user_accounts(id) on delete set null,
	box_id int references box_chats(id) on delete cascade,
	content text not null,
	create_date timestamp default current_timestamp,
	visible_to_owner boolean default true
);

create table reports(
	id serial primary key,
	reported_id int references user_accounts(id) on delete cascade,
	reporter_id int references user_accounts(id) on delete cascade,
	create_date timestamp default current_timestamp
);

create table friendships(
	request_id int references user_accounts(id) on delete cascade,
	accept_id int references user_accounts(id) on delete cascade,
	is_accepted boolean default false,
	primary key(request_id, accept_id)
);

create table block_lists(
	id serial primary key,
	user_id int references user_accounts(id) on delete cascade,
	block_id int references user_accounts(id) on delete cascade
);

create table box_chat_members(

	box_id int references box_chats(id) on delete cascade,
	user_id int references user_accounts(id) on delete cascade,
	is_admin boolean default false ,
	create_date timestamp default current_timestamp,
	primary key(box_id, user_id)
);

create table user_activity_logs(
	user_id int references user_accounts(id) on delete cascade,
	session_start timestamp default current_timestamp,
	session_end timestamp,
	primary key (user_id, session_start)
);