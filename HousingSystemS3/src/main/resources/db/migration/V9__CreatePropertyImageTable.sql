CREATE TABLE property_image (
id INT  AUTO_INCREMENT PRIMARY KEY NOT NULL,
property_id int,
image_url varchar(255),
image_name varchar(255),
file_name varchar(255),
FOREIGN KEY (property_id) REFERENCES property(id)
);