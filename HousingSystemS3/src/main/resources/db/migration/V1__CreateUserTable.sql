CREATE TABLE user(
id INT  AUTO_INCREMENT PRIMARY KEY NOT NULL,
first_name varchar(50),
last_name varchar(50),
date_of_birth date,
email varchar(50),
password varchar(50),
role int
)