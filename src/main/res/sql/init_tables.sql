create database if not exists `autoshop`;

use `autoshop`;

create table if not exists customer (
    customer_id int primary key auto_increment,
    first_name varchar(64),
    last_name varchar(64),
    phone varchar(16),
    email varchar(64),
    company varchar(128),
    address varchar(64),
    city varchar(64),
    state char(32),
    zip char(9)
);

create table if not exists vehicle (
    vehicle_id int auto_increment primary key,
	customer_id int,
    vin char(17),
    year char(4),
    make varchar(50),
    model varchar(50),
    license_plate char(16),
    color varchar(16),
    engine varchar(16),
    transmission varchar(20),
    foreign key (customer_id) references customer(customer_id)
);

create table if not exists item (
	item_name varchar(128) primary key,
    item_desc varchar(256),
    retail_price fixed(10,2),
    list_price fixed(10,2),
    taxable bool,
    quantity tinyint
);

create table if not exists work_order (
	work_order_id int primary key auto_increment,
    date_created date,
    date_completed date,
    customer_first_name varchar(64),
    customer_last_name varchar(64),
    customer_phone char(16),
    customer_email varchar(64),
    customer_company varchar(128),
    customer_address varchar(64),
    customer_city varchar(64),
    customer_state varchar(32),
    customer_zip varchar(9),
    vehicle_vin char(17),
    vehicle_year char(4),
    vehicle_make varchar(50),
    vehicle_model varchar(50),
    vehicle_license_plate char(16),
    vehicle_color varchar(16),
    vehicle_engine varchar(16),
    vehicle_tranmission varchar(20),
    vehicle_mileage_in char(10),
    vehicle_mileage_out char(10)
);

create table if not exists work_order_item (
	work_order_item_id int primary key auto_increment,
    work_order_id int,
    item_name varchar(128),
    item_desc varchar(256),
    item_retail_price fixed(10,2),
    item_list_price fixed(10,2),
    item_quantity tinyint,
    item_taxable bool,
    foreign key(work_order_id) references work_order(work_order_id)
);

create table if not exists work_order_labor (
	work_order_labor_id int primary key auto_increment,
    work_order_id int,
    labor_code varchar(32),
    labor_desc varchar(512),
    labor_billed_hrs fixed(10,2),
    labor_rate fixed(10,2),
    labor_taxable bool,
    foreign key(work_order_id) references work_order(work_order_id)
);

create table if not exists work_order_payment (
	work_order_payment_id int primary key auto_increment,
    work_order_id int,
    date_of_payment date,
    type char(5),
    amount fixed(10,2),
    foreign key(work_order_id) references work_order(work_order_id)
);