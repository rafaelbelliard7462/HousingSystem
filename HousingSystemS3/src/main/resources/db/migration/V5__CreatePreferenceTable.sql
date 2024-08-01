CREATE TABLE preference (
  id INT  AUTO_INCREMENT PRIMARY KEY NOT NULL,
  user_id int,
  city varchar(50),
  property_type int,
  price double,
  room int,
  FOREIGN KEY (user_id) REFERENCES user(id)
);