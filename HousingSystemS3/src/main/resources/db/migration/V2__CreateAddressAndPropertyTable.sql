CREATE TABLE address(
id INT  AUTO_INCREMENT PRIMARY KEY NOT NULL,
street varchar(50),
city varchar(50),
post_code varchar(25)
);

CREATE TABLE property(

id INT  AUTO_INCREMENT PRIMARY KEY NOT NULL,
user_id int,
address_id int,
property_type int,
price double,
room INT,
available date,
description text,
rented int,
rented_date date,
FOREIGN KEY (user_id) REFERENCES user(id),
FOREIGN KEY (address_id) REFERENCES address(id)
);

