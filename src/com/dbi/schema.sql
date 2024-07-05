CREATE TABLE users(
    email VARCHAR(50),
    password VARCHAR(20) NOT NULL ,
    full_name VARCHAR(50) NOT NULL ,
    PRIMARY KEY (email)
);

CREATE TABLE accounts(
    acc_no BIGINT,
    security_pin CHAR(4) NOT NULL,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    email VARCHAR(50),
    PRIMARY KEY (acc_no),
    FOREIGN KEY (email) REFERENCES users(email)
);
