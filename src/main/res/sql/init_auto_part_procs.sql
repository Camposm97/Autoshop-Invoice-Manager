use `autoshop`;

drop procedure if exists `GetAutoPartByName`;
drop procedure if exists `GetAutoPartByDesc`;
drop procedure if exists `AddAutoPart`;
drop procedure if exists `UpdateAutoPart`;
drop procedure if exists `FilterAutoPart`

delimiter $$ ;
create procedure `GetAutoPartByName`(in item_name varchar(256))
begin
	select item_name from item where item_name = item_name;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetAutoPartByDesc`(in item_desc varchar(256)) 
begin
	select item_desc from item where item_desc = item_desc; 
end $$
delimiter ;

delimiter $$ ;
create procedure `AddAutoPart`(
	in item_name varchar(128),
    in item_desc varchar(256),
    in retail_price fixed(10,2),
    in list_price fixed(10,2),
    in taxable bool,
    in quantity tinyint)
begin
	insert into item (item_name, item_desc, retail_price, list_price, taxable, quantity)
    values (item_name, item_desc, retail_price, list_price, taxable, quantity);
end $$
delimiter ;

delimiter $$ ;
create procedure `UpdateAutoPart`(
	in item_name varchar(128),
    in item_desc varchar(256),
    in retail_price fixed(10,2),
    in list_price fixed(10,2),
    in taxable bool,
    in quantity tinyint)
begin
	update item set item_desc = item_desc, retail_price = retail_price, list_price = list_price, taxable = taxable, quantity = quantity where item_name = item_name;
end $$
delimiter ;

delimiter $$ ;
create procedure `FilterAutoPart`(in item_desc varchar(256))
begin
	select * from item where item_desc like concat(item_desc, '%');
end $$
delimiter ;
