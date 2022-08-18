use `autoshop`;

drop procedure if exists `AddWorkOrder`;
drop procedure if exists `GetWorkOrderById`;
drop procedure if exists `GetCompletedWorkOrderIds`;
drop procedure if exists `GetIncompletedWorkOrderIds`;
drop procedure if exists `GetAllWorkOrderIds`;
drop procedure if exists `FilterWorkOrder`;
drop procedure if exists `UpdateWorkOrder`;
drop procedure if exists `DeleteWorkOrderById`;

drop procedure if exists `AddWorkOrderAutoPart`;
drop procedure if exists `GetWorkOrderAutoPartById`;
drop procedure if exists `GetWorkOrderAutoPartIdsByWorkOrderId`;
drop procedure if exists `UpdateWorkOrderAutoPart`;
drop procedure if exists `DeleteWorkOrderAutoPartById`;

drop procedure if exists `AddWorkOrderLabor`;
drop procedure if exists `GetWorkOrderLaborById`;
drop procedure if exists `GetWorkOrderLaborIdsByWorkOrderId`;
drop procedure if exists `UpdateWorkOrderLabor`;
drop procedure if exists `DeleteWorkOrderLaborById`;

drop procedure if exists `AddWorkOrderPayment`;
drop procedure if exists `GetWorkOrderPaymentById`;
drop procedure if exists `GetWorkOrderPaymentIdsByWorkOrderId`;
drop procedure if exists `UpdateWorkOrderPayment`;
drop procedure if exists `DeleteWorkOrderPaymentById`;

delimiter $$ ;
create procedure `AddWorkOrder`(
	in date_created date,
    in date_completed date,
    in customer_first_name varchar(64),
    in customer_last_name varchar(64),
    in customer_phone char(16),
    in customer_email varchar(64),
    in customer_company varchar(128),
    in customer_address varchar(64),
    in customer_city varchar(64),
    in customer_state varchar(32),
    in customer_zip varchar(9),
    in vehicle_vin char(17),
    in vehicle_year char(4),
    in vehicle_make varchar(50),
    in vehicle_model varchar(50),
    in vehicle_license_plate char(16),
    in vehicle_color varchar(16),
    in vehicle_engine varchar(16),
    in vehicle_tranmission varchar(20),
    in vehicle_mileage_in char(10),
    in vehicle_mileage_out char(10))
begin
	insert into work_order (date_created, date_completed, 
    customer_first_name, customer_last_name, 
    customer_phone, customer_email, customer_company, 
    customer_address, customer_city, customer_state, customer_zip,
    vehicle_vin, vehicle_year, vehicle_make, vehicle_model, vehicle_license_plate, vehicle_color, vehicle_engine, vehicle_transmission, vehicle_mileage_in, vehicle_mileage_out)
    values (date_created, date_completed, 
    customer_first_name, customer_last_name, 
    customer_phone, customer_email, customer_company, 
    customer_address, customer_city, customer_state, customer_zip,
    vehicle_vin, vehicle_year, vehicle_make, vehicle_model, vehicle_license_plate, vehicle_color, vehicle_engine, vehicle_transmission, vehicle_mileage_in, vehicle_mileage_out);
end $$
delimiter ;

delimiter $$ ;
create procedure `GetWorkOrderById`(in id int)
begin
	select * from work_order where work_order_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetCompletedWorkOrderIds`(in id int)
begin
	select work_order_id from work_order where date_completed is not null;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetIncompletedWorkOrderIds`(in id int)
begin
	select work_order_id from work_order where date_completed is null;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetAllWorkOrderIds`(in id int)
begin
	select work_order_id from work_order;
end $$
delimiter ;

delimiter $$ ;
create procedure `FilterWorkOrder`(
	in first_name varchar(64),
    in last_name varchar(64),
    in company varchar(128))
begin
	select work_order_id from work_order 
    where customer_first_name = concat(first_name, '%') 
    and customer_last_name like concat(last_name, '%') 
    and customer_company like concat(company, '%');
end $$
delimiter ;

delimiter $$ ;
create procedure `UpdateWorkOrder`(
	in id int,
    in date_created date,
    in date_completed date,
    in customer_first_name varchar(64),
    in customer_last_name varchar(64),
    in customer_phone char(16),
    in customer_email varchar(64),
    in customer_company varchar(128),
    in customer_address varchar(64),
    in customer_city varchar(64),
    in customer_state varchar(32),
    in customer_zip varchar(9),
    in vehicle_vin char(17),
    in vehicle_year char(4),
    in vehicle_make varchar(50),
    in vehicle_model varchar(50),
    in vehicle_license_plate char(16),
    in vehicle_color varchar(16),
    in vehicle_engine varchar(16),
    in vehicle_tranmission varchar(20),
    in vehicle_mileage_in char(10),
    in vehicle_mileage_out char(10))
begin
	update work_order set 
    date_created = date_created,
    date_completed = date_completed,
    customer_first_name = customer_first_name,
    customer_last_name = customer_last_name,
    customer_phone = customer_phone,
    customer_email = customer_email,
    customer_company = customer_company,
    customer_address = customer_address,
    customer_city = customer_city,
    customer_state = customer_state,
    customer_zip = customer_zip,
    vehicle_vin = vehicle_vin,
    vehicle_year = vehicle_year,
    vehicle_make = vehicle_make,
    vehicle_model = vehicle_model,
    vehicle_license_plate = vehicle_license_plate,
    vehicle_color = vehicle_color,
    vehicle_engine = vehicle_engine,
    vehicle_transmission = vehicle_transmission,
    vehicle_mileage_in = vehicle_mileage_in,
    vehicle_mileage_out = vehicle_mileage_out 
    where work_order_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `DeleteWorkOrderById`(in id int)
begin
    delete from work_order where work_order_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `AddWorkOrderAutoPart`(
    in work_order_id int,
    in item_name varchar(128),
    in item_desc varchar(256),
    in item_retail_price fixed(10,2),
    in item_list_price fixed(10,2),
    in item_quantity tinyint,
    in item_taxable bool)
begin
    insert into work_order_item(work_order_id, item_name, item_desc, item_retail_price, item_list_price, item_quantity, item_taxable) 
    values (work_order_id, item_name, item_desc, item_retail_price, item_list_price, item_quantity, item_taxable);
end $$
delimiter ;

delimiter $$ ;
create procedure `GetWorkOrderAutoPartById`(in id int)
begin
    select * from work_order_item where work_order_item_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetWorkOrderAutoPartIdsByWorkOrderId`(in id int)
begin
    select work_order_item_id from work_order_item where work_order_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `UpdateWorkOrderAutoPart`(
    in work_order_id int,
    in item_name varchar(128),
    in item_desc varchar(256),
    in item_retail_price fixed(10,2),
    in item_list_price fixed(10,2),
    in item_quantity tinyint,
    in item_taxable bool)
begin
    update work_order_item set item_name = item_name, item_desc = item_desc, item_retail_price = item_retail_price, item_list_price = item_list_price, item_quantity = item_quantity, item_taxable = item_taxable where work_order_id = work_order_id;
end $$
delimiter ;

delimiter $$ ;
create procedure `DeleteWorkOrderAutoPartById`(in id int)
begin
    delete from work_order_item where work_order_item_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `AddWorkOrderLabor`(
    in work_order_id int,
    in labor_code varchar(32),
    in labor_desc varchar(512),
    in labor_billed_hrs fixed(10,2),
    in labor_rate fixed(10,2),
    in labor_taxable bool)
begin
    insert into wrok_order_labor (work_order_id, labor_code, labor_desc, labor_billed_hrs, labor_rate, labor_taxable) values (work_order_id, labor_code, labor_desc, labor_billed_hrs, labor_rate, labor_taxable);
end $$
delimiter ;

delimiter $$ ;
create procedure `GetWorkOrderLaborById`(in id int)
begin
    select * from work_order_labor where work_order_labor_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetWorkOrderLaborIdsByWorkOrderId`(in id int)
begin
    select work_order_labor_id from work_order_labor where work_order_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `UpdateWorkOrderLabor`(
    in id int,
    in labor_code varchar(32),
    in labor_desc varchar(512),
    in labor_billed_hrs fixed(10,2),
    in labor_rate fixed(10,2),
    in labor_taxable bool)
begin
    update work_order_labor set labor_code = labor_code, labor_desc = labor_desc, labor_billed_hrs = labor_billed_hrs, labor_rate = labor_rate, labor_taxable = labor_taxable where work_order_labor_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `DeleteWorkOrderLaborById`(in id int)
begin
    delete from work_order_labor where work_order_labor_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `AddWorkOrderPayment`(
    in work_order_id int,
    in date_of_payment date,
    in type char(5),
    in amount fixed(10,2))
begin
    insert into work_order_payment (work_order_id, date_of_payment, type, amount) values (work_order_id, date_of_payment, type, amount);
end $$
delimiter ;

delimiter $$ ;
create procedure `GetWorkOrderPaymentById`(in id int)
begin
    select * from work_order_payment where work_order_payment_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetWorkOrderPaymentIdsByWorkOrderId`(in id int)
begin
    select work_order_payment_id from work_order_payment where work_order_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `UpdateWorkOrderPayment`(
    in id int,
    in date_of_payment date,
    in type char(5),
    in amount fixed(10,2))
begin
    update work_order_payment set date_of_payment = date_of_payment, type = type, amount = amount where work_order_payment_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `DeleteWorkOrderPaymentById`(in id int)
begin
    delete from work_order_payment where work_order_payment_id = id;
end $$
delimiter ;