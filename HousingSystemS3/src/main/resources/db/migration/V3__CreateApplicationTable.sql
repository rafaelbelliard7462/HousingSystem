CREATE TABLE application(
id INT  AUTO_INCREMENT PRIMARY KEY NOT NULL,
user_id int,
property_id int,
applied_date date,
status int,
description text,
FOREIGN KEY (user_id) REFERENCES user(id),
FOREIGN KEY (property_id) REFERENCES property(id)
);