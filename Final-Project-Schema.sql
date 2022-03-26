CREATE DATABASE DB2;

USE DB2;

-- Users table represents all relavent user information to the application
CREATE TABLE Users (
	user_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    ssn VARCHAR(11) DEFAULT "000-00-0000",
    username VARCHAR(20) UNIQUE NOT NULL,
    pass VARCHAR(100) NOT NULL
);

-- *Optional* Addresses table represents a user's address
CREATE TABLE Addresses (
	address_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    city VARCHAR(20) DEFAULT "Citytown",
    state VARCHAR(20) DEFAULT "AL",
    zipcode VARCHAR(10) DEFAULT "54321",
    street_address VARCHAR(30) DEFAULT "1424 Alpine Drive",
    user_id INT NOT NULL REFERENCES Users(user_id)
);


-- Accounts table represents a bank account
CREATE TABLE Accounts (
	account_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    account_name VARCHAR(15) DEFAULT "New Account",
    user_id INT NOT NULL REFERENCES Users(user_id),
    account_type ENUM('Checking', 'Savings') NOT NULL,
	account_number MEDIUMINT UNIQUE NOT NULL,
	available_balance DECIMAL(13, 2) NOT NULL DEFAULT 0.00,
    total_balance DECIMAL(13, 2) NOT NULL DEFAULT 0.00,
    date_created DATETIME NOT NULL DEFAULT LOCALTIME,
    last_updated TIMESTAMP NOT NULL ON UPDATE LOCALTIMESTAMP DEFAULT LOCALTIMESTAMP
);


-- heap table to store all transactions
CREATE TABLE Transactions (
    transaction_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    account_number MEDIUMINT NOT NULL NOT NULL REFERENCES Accounts (account_number),
    amount DECIMAL(13 , 2 ) NOT NULL,
    transaction_type ENUM('credit', 'debit') NOT NULL,
    description VARCHAR(90),
    processed TIMESTAMP NOT NULL ON UPDATE LOCALTIMESTAMP DEFAULT LOCALTIMESTAMP
);

-- NewAccountNumber function generates a unique account number
DELIMITER $$

CREATE FUNCTION NewAccountNumber()
	RETURNS MEDIUMINT 
	READS SQL DATA
BEGIN

	DECLARE value MEDIUMINT;
	DECLARE bool BOOLEAN DEFAULT TRUE;
    
    WHILE bool = TRUE DO
    
		SELECT FLOOR(RAND()*(110000-119999+1)+119999)
		INTO value;
    
		IF ((SELECT account_number FROM Accounts HAVING account_number = value) IS NOT NULL) THEN
			SET bool = TRUE;
		ELSE 
			SET bool = FALSE;
		END IF;

	END WHILE;
    
    RETURN value;
END $$

DELIMITER ;

-- NEW_ACCOUNT procedure inserts a new row into Accounts
-- * must call this procedure from the application when inserting a new row *
-- * in order to auto-generate a new account number * 
DELIMITER $$
SET SQL_SAFE_UPDATES = 0;

CREATE PROCEDURE NEW_ACCOUNT(IN name VARCHAR(15), IN type VARCHAR(15), IN user INT)
BEGIN
    DECLARE acct_name VARCHAR(15);
    DECLARE acct_type ENUM('Checking', 'Savings');
    DECLARE acct_number MEDIUMINT;
    DECLARE id INT;
    
    SET acct_name = name;
	SET acct_type = type;
    SELECT NewAccountNumber() INTO acct_number;
    SET id = user;
    
	INSERT INTO Accounts (account_name, user_id, account_type, account_number) VALUES 
		(acct_name, id, acct_type, NewAccountNumber());
END$$
SET SQL_SAFE_UPDATES = 1;
DELIMITER ;

-- NEW_USER procedure inserts a new row into Users
-- * must call this procedure from the application when inserting a new row *
DELIMITER $$
SET SQL_SAFE_UPDATES = 0;

CREATE PROCEDURE NEW_USER(IN firstname VARCHAR(20), IN lastname VARCHAR(20), IN ssn VARCHAR(11), 
	IN user VARCHAR(20), IN pass VARCHAR(30))
    
BEGIN
	DECLARE name1 VARCHAR(20);
    DECLARE name2 VARCHAR(20);
    DECLARE ssNumber VARCHAR(11);
    DECLARE name3 VARCHAR(20);
    DECLARE passkey VARCHAR(100);
    
    SET name1 = firstname;
    SET name2 = lastname;
    SET ssNumber = ssn;
    SET name3 = user;
    SET passkey = PASSWORD(pass);
    
    INSERT INTO Users (first_name, last_name, ssn, username, pass) VALUES (
		name1, name2, ssNumber, name3, passkey
    );
END$$
SET SQL_SAFE_UPDATES = 1;
DELIMITER ;

-- UPDATE_TOTALS procedure simulates the bank processing deposits and updating the available_balance of an account
DELIMITER $$
SET SQL_SAFE_UPDATES = 0;

CREATE PROCEDURE UPDATE_TOTALS()
BEGIN
	DECLARE available DECIMAL(13, 2) DEFAULT 0.0;
    DECLARE total DECIMAL(13, 2) DEFAULT 0.0;
    DECLARE account INT;
    DECLARE finishedReading BOOL DEFAULT FALSE;
    
	DECLARE accountCursor CURSOR FOR
		SELECT accountID, availableBalance, totalBalance FROM Accounts;
        
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET finishedReading = TRUE;
        
	OPEN accountCursor;
    FETCH FROM accountCursor INTO account, available, total;
    
    WHILE NOT finishedReading DO
		IF available < total THEN
			UPDATE Accounts
            SET availableBalance = total
            WHERE accountID = account;
		END IF;
        
	    FETCH FROM accountCursor INTO account, available, total;

	END WHILE;
    
    CLOSE accountCursor;
    
END$$
SET SQL_SAFE_UPDATES = 1;
DELIMITER ;

     