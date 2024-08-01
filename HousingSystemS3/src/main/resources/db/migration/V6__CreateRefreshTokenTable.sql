CREATE TABLE refresh_token (
id INT  AUTO_INCREMENT PRIMARY KEY NOT NULL,
user_id int,
token text,
expiry_date datetime,
FOREIGN KEY (user_id) REFERENCES user(id)
);