CREATE DATABASE Project_Database;

CREATE TABLE Accounts (
	accountID INT PRIMARY KEY NOT NULL,
    accountPIN INT NOT NULL,
    availableBalance DECIMAL(13, 2) NOT NULL,
    totalBalance DECIMAL(13, 2) NOT NULL,
    date_created DATETIME NOT NULL DEFAULT NOW(),
    last_updated TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE Transactions (
	transaction_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    accountID INT NOT NULL REFERENCES Accounts(accountID),
    amount DECIMAL(13, 2) NOT NULL,
    transaction_type ENUM('credit', 'debit') NOT NULL,
    processed TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

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



