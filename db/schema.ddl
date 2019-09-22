create table vehicle
(
    reg   text not null
        constraint vehicle_pk primary key,
    make  text not null,
    model text not null,
    color text,
    year  int
);

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

create unique index vehicle_reg_uindex
    on vehicle (reg);

