drop table if exists vehicle_owner;
drop index if exists vehicle_reg_uindex;
drop table if exists vehicle;
drop table if exists reading;
drop table if exists user_profile;
drop table if exists token;

create table vehicle
(
    reg   text not null
        constraint vehicle_pk primary key,
    make  text not null,
    model text not null,
    color text,
    year  int
);

create unique index vehicle_reg_uindex
    on vehicle (reg);

create table reading
(
    refuel_date date          not null,
    reg         text          not null,
    miles       decimal(5, 2) not null,
    mileage     int           not null,
    liters      decimal(5, 2) not null,
    cost        decimal(5, 2) not null,
    constraint reading_pk
        primary key (reg, mileage)
);

create table token
(
    token      text      not null,
    secret     text      not null,
    created_at TIMESTAMP not null default now(),
    constraint pk_token primary key (token)
);


create table user_profile
(
    id                 text      not null,
    name               text      not null,
    display_name       text      not null,
    location           text      null,
    description        text      null,
    profile_image_url  text      null,
    profile_banner_url text      null,
    followers_count    int       not null default 0,
    following_count    int       not null default 0,
    created_at         TIMESTAMP not null default now(),
    access_token   text      not null references token (token),
    constraint pk_user_profile_id primary key (id)
);

create table vehicle_owner
(
    reg text not null references vehicle(reg),
    owner text not null, -- references user_profile(id) doesn't reference anything so I can load data into it without any users, and register a user additionally
    primary key (reg, owner)
);
