use `autoshop`;

drop procedure if exists `AddVehicle`;
drop procedure if exists `UpdateVehicle`;
drop procedure if exists `DeleteVehicleById`;
drop procedure if exists `DeleteVehiclesByCustomerId`;
drop procedure if exists `GetVehicleById`;
drop procedure if exists `GetAllVehicleIds`;
drop procedure if exists `GetVehiclesByCustomerId`;
drop procedure if exists `FilterVehicle`;
drop procedure if exists `FilterVehicleWithCustomerId`;
drop procedure if exists `GetUniqueVehicleYears`;
drop procedure if exists `GetUniqueVehicleMakes`;
drop procedure if exists `GetUniqueVehicleModels`;
drop procedure if exists `GetUniqueVehicleColors`;
drop procedure if exists `GetUniqueVehicleEngines`;
drop procedure if exists `GetUniqueVehicleTransmissions`;

delimiter $$ ;
create procedure `AddVehicle`(
	in customer_id int, 
    in vin char(17),
	in year char(4),
    in make varchar(50),
    in model varchar(50),
    in license_plate char(16),
    in color varchar(16),
    in engine varchar(16),
    in transmission varchar(20))
begin
	insert into vehicle (customer_id, vin, year, make, model, licnse_plate, color, engine, transmission)
    values (custmomer_id, vin, year, make, model, license_plate, color, engine, transmission);
end $$
delimiter ;

delimiter $$ ;
create procedure `UpdateVehicle`(
	in vin char(17),
	in year char(4),
    in make varchar(50),
    in model varchar(50),
    in license_plate char(16),
    in color varchar(16),
    in engine varchar(16),
    in transmission varchar(20),
    in vehicle_id int)
begin
	update vehicle set 
    vin = vin, 
    year = year, 
    make = make, 
    model = model, 
    license_plate = license_plate, 
    color = color, 
    engine = engine, 
    transmission = tranmission 
    where vehicle_id = vehicle_id;
end $$
delimiter ;

delimiter $$ ;
create procedure `DeleteVehicleById`(in id int)
begin
	delete from vehicle where vehicle_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `DeleteVehiclesByCustomerId`(in id int)
begin
	delete from vehicle where customer_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetVehicleById`(in id int)
begin
	select * from vehicle where vehicle_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetAllVehicleIds`()
begin
	select vehicle_id from vehicle;
end $$
delimiter ;

delimiter $$ ;
create procedure `GetVehiclesByCustomerId`(in id int)
begin
	select vehicle_id from vehicle where customer_id = id;
end $$
delimiter ;

delimiter $$ ;
create procedure `FilterVehicle`(
	in vin char(17),
	in year char(4),
    in make varchar(50),
    in model varchar(50),
    in license_plate char(16),
    in color varchar(16),
    in engine varchar(16),
    in transmission varchar(20))
begin
	select vehicle_id from vehicle 
    where vin like concat(vin,'%') 
    and year like concat(year, '%') 
    and model like concat(model, '%') 
    and license_plate like concat(license_plate, '%') 
    and color like concat(color, '%') 
    and engine like concat(engine, '%') 
    and transmission like concat(transmission, '%');
end $$
delimiter ;

delimiter $$ ;
create procedure `FilterVehicleWithCustomerId`(
	in vin char(17),
	in year char(4),
    in make varchar(50),
    in model varchar(50),
    in license_plate char(16),
    in color varchar(16),
    in engine varchar(16),
    in transmission varchar(20),
    in customer_id int)
begin
	select vehicle_id from vehicle 
    where vin like concat(vin,'%') 
    and year like concat(year, '%') 
    and model like concat(model, '%') 
    and license_plate like concat(license_plate, '%') 
    and color like concat(color, '%') 
    and engine like concat(engine, '%') 
    and transmission like concat(transmission, '%')
    and customer_id = customer_id;
end $$
delimiter ;

delimiter $$
create procedure `GetUniqueVehicleYears`()
begin
	select distinct year from vehicle order by year;
end $$
delimiter ;

delimiter $$
create procedure `GetUniqueVehicleMakes`()
begin
	select distinct make from vehicle order by make;
end $$
delimiter ;

delimiter $$
create procedure `GetUniqueVehicleModels`()
begin
	select distinct model from vehicle order by model;
end $$
delimiter ;

delimiter $$
create procedure `GetUniqueVehicleColors`()
begin
	select distinct color from vehicle order by color;
end $$
delimiter ;

delimiter $$
create procedure `GetUniqueVehicleEngines`()
begin
	select distinct engine from vehicle order by engine;
end $$
delimiter ;

delimiter $$
create procedure `GetUniqueVehicleTransmissions`()
begin
	select distinct transmission from vehicle order by transmission;
end $$
delimiter ;
