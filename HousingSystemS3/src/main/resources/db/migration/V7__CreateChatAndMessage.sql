
CREATE TABLE chat
(
    id INT  AUTO_INCREMENT PRIMARY KEY NOT NULL,
    home_seeker_id INT NOT NULL,
    homeowner_id INT NOT NULL
);


CREATE TABLE message
(
    id INT  AUTO_INCREMENT PRIMARY KEY NOT NULL,
    content TEXT NOT NULL,
    sender_id INT NOT NULL,
    chat_id INT NOT NULL
);
