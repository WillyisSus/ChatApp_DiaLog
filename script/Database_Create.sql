create database dialog_db;

create table user_info (
    id serial primary key,
    username varchar(100) unique not null,
    password varchar(50) not null,
    email varchar(100) not null,
    displayname varchar(100),
    sex boolean,
    address varchar(255),
    create_date timestamp default current_timestamp,
    is_lock boolean default false,
    is_admin boolean default false
);

create table box_chat (
    id serial primary key,
    is_direct boolean not null,
    box_name varchar(100),
    create_date timestamp default current_timestamp
);

create table member_in_box (
    user_id int references user_info(id),
    box_id int references box_chat(id),
    is_admin boolean,
    primary key (user_id, box_id)
);

create table message (
    msg_id serial primary key,
    sender_id int references user_info(id),
    box_id int references box_chat(id),
    msg text,
    create_date timestamp default current_timestamp
);

create table report_msg (
    rep_msg_id serial primary key,
    reported_id int references user_info(id),
    reporter_id int references user_info(id),
    create_date timestamp default current_timestamp
);

create table user_relationship (
	relationship_id serial primary key,
    user1_id int references user_info(id),
    user2_id int references user_info(id),
    -- 1 for block, 2 for friend request, 3 for friend
    relationship smallint check (relationship between 1 and 3)
);

create table session_log(
    user_id int references user_info(id),
    start_session timestamp default current_timestamp,
    end_sesion timestamp,
    primary key(user_id, start_session)
);