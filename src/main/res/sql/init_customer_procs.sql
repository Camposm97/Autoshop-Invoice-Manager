use `autoshop`;

drop procedure if exists `GetCustomerById`;
drop procedure if exists `AddCustomer`;
drop procedure if exists `CustomerExists`;
drop procedure if exists `GetAllCustomers`;
drop procedure if exists `GetAllCustomerIds`;
drop procedure if exists `UpdateCustomer`;
drop procedure if exists `DeleteCustomerById`;
drop procedure if exists `FilterCustomer`;
drop procedure if exists `GetUniqueCustomerCompanies`;
drop procedure if exists `GetUniqueCustomerAddresses`;
drop procedure if exists `GetUniqueCustomerCities`;
drop procedure if exists `GetUniqueCustomerZips`;

delimiter $$ ; 
create procedure `GetCustomerById`(in customer_id int) 
begin 
	select * from customer where customer.customer_id = customer_id; 
end $$ ; 
delimiter ;

delimiter $$ ;
create procedure `AddCustomer`(
	in first_name varchar(64), 
	in last_name varchar(64), 
	in phone varchar(16), 
	in email varchar(64), 
	in company varchar(128), 
	in address varchar(64), 
	in city varchar(64), 
	in state char(32), 
	in zip char(9))
begin
	insert into customer (first_name, last_name, phone, email, company, address, city, state, zip) 
    values (first_name, last_name, phone, email, company, address, city, state, zip);
end $$ ;
delimiter ;

delimiter $$ ;
create procedure `CustomerExists`(
	in first_name varchar(64),
    in last_name varchar(64), 
	in phone varchar(16), 
	in email varchar(64), 
	in company varchar(128), 
	in address varchar(64), 
	in city varchar(64), 
	in state char(32), 
	in zip char(9))
begin
	select customer_id from customer 
    where first_name = first_name 
    and last_name = last_name 
    and phone = phone 
    and email = email 
    and company = company 
    and address = address 
    and city = city 
    and state = state 
    and zip = zip;
end $$ ;
delimiter ;

delimiter $$ ;
create procedure `GetAllCustomers`()
begin
	select * from customer;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetAllCustomerIds`()
begin
	select customer_id from customer;
end $$
delimiter ;

delimiter $$ ;
create procedure `UpdateCustomer`(
	in first_name varchar(64),
    in last_name varchar(64), 
	in phone varchar(16), 
	in email varchar(64), 
	in company varchar(128), 
	in address varchar(64), 
	in city varchar(64), 
	in state char(32), 
	in zip char(9),
    in customer_id int)
begin
	update customer set 
    first_name = first_name,
    last_name = last_name,
    phone = phone,
    email = email,
    company = company,
    address = address,
    city = city,
    state = state,
    zip = zip
    where customer_id = customer_id;
end $$
delimiter ;

delimiter $$ ;
create procedure `DeleteCustomerById`(in id int)
begin
	delete from customer where customer_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `FilterCustomer`(
	in first_name varchar(64),
    in last_name varchar(64), 
	in phone varchar(16), 
	in email varchar(64), 
	in company varchar(128), 
	in address varchar(64), 
	in city varchar(64), 
	in state char(32), 
	in zip char(9))
begin
	select customer_id from customer 
    where first_name like concat(first_name,'%') 
    and last_name like concat(last_name,'%') 
    and phone like concat(phone,'%') 
    and email like concat(email,'%') 
    and company like concat(company,'%') 
    and address like concat(address,'%') 
    and city like concat(city,'%') 
    and state like concat(state,'%') 
    and zip like concat(zip,'%');
end $$
delimiter ;

delimiter $$ ;
create procedure `GetUniqueCustomerCompanies`()
begin
	select distinct company from customer order by company;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetUniqueCustomerAddresses`()
begin
	select distinct address from customer order by address;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetUniqueCustomerCities`()
begin
	select distinct city from customer order by city;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetUniqueCustomerZips`()
begin
	select distinct zip from customer order by zip;
end $$
delimiter ;

