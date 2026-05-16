CREATE DATABASE hotel_management_system;
use hotel_management_system;

CREATE TABLE rooms (
	roomId int PRIMARY KEY,
    roomType varchar(10),
    bedNumbers int,
    isAvailable bool,
    isClean bool,
    isSmoking bool
);

CREATE TABLE customers (
	customerId int PRIMARY KEY,
    customerName varchar(50),
    customerEmail varchar(50),
    customerGender varchar(6),
    customerPoints int
);

CREATE TABLE manager (
	managerName varchar(50),
    managerPassword varchar(50),
    managerEmail varchar(50),
    managerGender varchar(6)
);

CREATE TABLE staff (
	staffId int PRIMARY KEY,
    staffName varchar(50),
    staffEmail varchar(50),
    staffGender varchar(6),
    staffSalary double,
    staffPaid varchar(6)
);

CREATE TABLE booking (
	bookingId int AUTO_INCREMENT PRIMARY KEY,
    roomId int,
    customerId int,
    totalRent double,
    checkIn date,
    checkOut date
)