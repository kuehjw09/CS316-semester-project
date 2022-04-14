CREATE DATABASE DB2;

USE DB2;

-- Users table represents all relavent user information to the application
CREATE TABLE Users (
	user_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(20) NOT NULL,
    last_name VARCHAR(20) NOT NULL,
    ssn VARCHAR(11) DEFAULT "000-00-0000",
    birth_date DATE,
    username VARCHAR(20) UNIQUE NOT NULL,
    pass VARCHAR(100) NOT NULL,
    default_account MEDIUMINT REFERENCES Accounts(account_number)
);

-- Accounts table represents a bank account
CREATE TABLE Accounts (
	account_id INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    account_name VARCHAR(25) DEFAULT "New Account",
    user_id INT NOT NULL REFERENCES Users(user_id)
		ON UPDATE CASCADE
        ON DELETE CASCADE,
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
    account_number MEDIUMINT NOT NULL NOT NULL REFERENCES Accounts (account_number)
		ON UPDATE CASCADE
        ON DELETE CASCADE,
    amount DECIMAL(13 , 2 ) NOT NULL,
    transaction_type ENUM('credit', 'debit') NOT NULL,
    description VARCHAR(90),
    status ENUM('pending', 'processed') NOT NULL,
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

CREATE PROCEDURE NEW_ACCOUNT(IN name VARCHAR(25), IN type VARCHAR(15), IN user INT)
BEGIN
    DECLARE acct_name VARCHAR(25);
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

CREATE PROCEDURE NEW_USER(IN firstname VARCHAR(20), IN lastname VARCHAR(20), IN birthdate DATE, 
	IN ssn VARCHAR(11), IN user VARCHAR(20), IN pass VARCHAR(30))
    
BEGIN
	DECLARE name1 VARCHAR(20);
    DECLARE name2 VARCHAR(20);
    DECLARE bday DATE;
    DECLARE ssNumber VARCHAR(11);
    DECLARE name3 VARCHAR(20);
    DECLARE passkey VARCHAR(100);
    
    SET name1 = firstname;
    SET name2 = lastname;
    SET bday = birthdate;
    SET ssNumber = ssn;
    SET name3 = user;
    SET passkey = MD5(pass);
    
    INSERT INTO Users (first_name, last_name, birth_date, ssn, username, pass) VALUES (
		name1, name2, bday, ssNumber, name3, passkey
    );
    
    CALL NEW_ACCOUNT("Default Checking", "checking", (SELECT user_id FROM Users WHERE username = user));
    CALL SET_DEFAULT_ACCOUNT((SELECT user_id FROM Users WHERE username = name3), (SELECT account_number FROM Accounts WHERE user_id = 
    (SELECT user_id FROM Users WHERE username = name3)));
END$$
SET SQL_SAFE_UPDATES = 1;
DELIMITER ;

-- SET_DEFAULT_ACCOUNT procedure to set the default account for a user
DELIMITER $$
SET SQL_SAFE_UPDATES = 0;
CREATE PROCEDURE SET_DEFAULT_ACCOUNT(IN userID INT, IN accountNum MEDIUMINT)
BEGIN
	DECLARE id INT;
    DECLARE acctid MEDIUMINT;
    
    SET id = userID;
    SET acctid = accountNum;
    
	UPDATE Users SET default_account = acctid WHERE user_id = id; 
END$$
SET SQL_SAFE_UPDATES = 1;
DELIMITER ;


-- CHECK_PASS procedure to verify a password for a given user
DELIMITER $$
CREATE PROCEDURE CHECK_PASS(IN id INT, IN word VARCHAR(50), OUT VERIFIED BOOLEAN)
BEGIN
	DECLARE hashkey VARCHAR(32);    
    SELECT pass FROM Users WHERE user_id = id INTO hashkey;
    
    IF hashkey = word THEN SET VERIFIED = TRUE;
	ELSE SET VERIFIED = FALSE;
    END IF;
    
END $$
DELIMITER ;

-- UPDATE_TOTALS procedure simulates the bank processing deposits and updating the balances of an account
DELIMITER $$
SET SQL_SAFE_UPDATES = 0;

CREATE PROCEDURE UPDATE_TOTALS()
BEGIN
    DECLARE total DECIMAL(13, 2) DEFAULT 0.0;
    DECLARE account INT;
    DECLARE operation ENUM("credit", "debit");
    DECLARE transactionID INT;
	DECLARE finishedReading BOOL DEFAULT FALSE;

	DECLARE transactionCursor CURSOR FOR
		SELECT account_number, amount, transaction_type, transaction_id FROM Transactions
			WHERE status = "pending";
            
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET finishedReading = TRUE;
            
	OPEN transactionCursor;
    FETCH FROM transactionCursor INTO account, total, operation, transactionID;
    
    WHILE NOT finishedReading DO
    
		IF operation = "debit" THEN
			UPDATE Accounts SET total_balance = (total_balance - total) WHERE account_number = account;
		END IF;
		IF operation = "credit" THEN
			UPDATE Accounts SET available_balance = (available_balance + total) WHERE account_number = account;
		END IF;
        
		UPDATE Transactions SET status = "processed" WHERE transaction_id = transactionID;
     
		FETCH FROM transactionCursor INTO account, total, operation, transactionID;

	END WHILE;
    
    CLOSE transactionCursor;
    
END$$
SET SQL_SAFE_UPDATES = 1;
DELIMITER ;

     