-- Drop the existing table for coin price to reset the schema
DROP TABLE IF EXISTS COIN_PRICE;

-- Create a new table for managing coin prices
CREATE TABLE COIN_PRICE ( 
   id BIGINT NOT NULL auto_increment, -- Unique identifier for each record
   local_date_time DATETIME NOT NULL, -- Timestamp of the last update
   code VARCHAR(50) NOT NULL, -- Identifier for the coin
   price FLOAT(4) NOT NULL, -- Current price of the coin
   updated_by VARCHAR(50) -- Identifier of the updater
);

-- Insert an initial record into the coin price table
insert into COIN_PRICE(local_date_time,code,price) values(CURRENT_TIMESTAMP,'USDC',1.0);
insert into COIN_PRICE(local_date_time,code,price) values(CURRENT_TIMESTAMP,'USDT',1.0);
insert into COIN_PRICE(local_date_time,code,price) values(CURRENT_TIMESTAMP,'BUSD',1.0);
insert into COIN_PRICE(local_date_time,code,price) values(CURRENT_TIMESTAMP,'PYUSD',1.0);