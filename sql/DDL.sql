show databases; 

use CSCI5308_8_DEVINT;
show tables;

CREATE TABLE `user_details` (
`user_id` int NOT NULL AUTO_INCREMENT,
`first_name` varchar(255) DEFAULT NULL,
`last_name` varchar(255) DEFAULT NULL,
`email` varchar(255) NOT NULL unique,
`password` varchar(255) NOT NULL,
`contact` varchar(10) DEFAULT NULL,
`register_at` datetime  DEFAULT NULL,
`security_code` varchar(200)  DEFAULT NULL,
PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `sales` (
`order_id` int NOT NULL AUTO_INCREMENT ,
`product` text,
`quantity_ordered` int ,
`price_each` double,
`order_date` datetime,
`purchase_address` text,
PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


CREATE TABLE `location` (
`location_id` int NOT NULL AUTO_INCREMENT ,
`location_name` varchar(255) DEFAULT NULL,
`zipcode` varchar(255) DEFAULT NULL,
`province` varchar(255) DEFAULT NULL,
`country` varchar(255) DEFAULT NULL,
PRIMARY KEY (`location_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


DROP TABLE IF EXISTS `merchant_details`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `merchant_details` (
`merchant_id` int NOT NULL AUTO_INCREMENT,
`merchant_name` varchar(255) DEFAULT NULL,
`email` varchar(255) NOT NULL unique,
`password` varchar(255) NOT NULL,
`security_code` varchar(200)  DEFAULT NULL,
PRIMARY KEY (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


CREATE TABLE `store` (
`store_id` int NOT NULL AUTO_INCREMENT,
`merchant_id` int NOT NULL,
`store_name` varchar(255) DEFAULT NULL,
`location_id` int NOT NULL,
`start_time` time DEFAULT NULL,
`end_time` time DEFAULT NULL,
`type_of_business` varchar(255) DEFAULT NULL,
`contact` varchar(10) DEFAULT NULL,
PRIMARY KEY (`store_id`),
KEY `store_loc_fk` (`location_id`),
KEY `store_merch_fk` (`merchant_id`),
CONSTRAINT `store_loc_fk` FOREIGN KEY (`location_id`) REFERENCES `location` (`location_id`),
CONSTRAINT `store_merch_fk` FOREIGN KEY (`merchant_id`) REFERENCES `merchant_details` (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
`product_id` int NOT NULL AUTO_INCREMENT,
`product_name` varchar(255) DEFAULT NULL,
`product_description` varchar(255) DEFAULT NULL,
PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `product_category`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_category` (
`product_category_id` int NOT NULL AUTO_INCREMENT,
`product_id` int NOT NULL ,
`product_category_name` varchar(255) DEFAULT NULL,
`product_category_description` varchar(255) DEFAULT NULL,
PRIMARY KEY (`product_category_id`),
KEY `prod_cat_fk` (`product_id`),
CONSTRAINT `prod_cat_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


DROP TABLE IF EXISTS `product_inventory`;
CREATE TABLE `product_inventory` (
`inventory_id` int NOT NULL AUTO_INCREMENT,
`store_id` int NOT NULL,
`product_id` int NOT NULL,
`price` float DEFAULT NULL,
`stock` float DEFAULT NULL,
PRIMARY KEY (`inventory_id`),
KEY `prodinv_store_fk` (`store_id`),
KEY `prodinv_prod_fk` (`product_id`),
CONSTRAINT `prodinv_store_fk` FOREIGN KEY (`store_id`) REFERENCES `store` (`store_id`),
CONSTRAINT `prodinv_prod_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

DROP TABLE IF EXISTS `subscription`;
CREATE TABLE `subscription` (
`subscrptn_id` int NOT NULL AUTO_INCREMENT,
`user_id` int NOT NULL,
`product_id` int NOT NULL,
`price_alert` float,
PRIMARY KEY (`subscrptn_id`),
KEY `user_subscrptn_fk` (`user_id`),
KEY `subs_prod_fk` (`product_id`),
CONSTRAINT `user_subscrptn_fk` FOREIGN KEY (`user_id`) REFERENCES `user_details` (`user_id`),
CONSTRAINT `subs_prod_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `tags` (
`tag_id` int NOT NULL AUTO_INCREMENT,
`product_id` int NOT NULL,
`tag_name` varchar(255) DEFAULT NULL,
PRIMARY KEY (`tag_id`),
KEY `tag_product_fk` (`product_id`),
CONSTRAINT `tag_prodinv_fk` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `cart` (
`cart_id` int NOT NULL AUTO_INCREMENT,
`user_id` int NOT NULL,
`status` varchar(255) DEFAULT NULL,
`shared_status` varchar(255)  DEFAULT NULL,
PRIMARY KEY (`cart_id`),
KEY `cart_user_fk` (`user_id`),
CONSTRAINT `cart_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user_details` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;




CREATE TABLE `product_in_cart` (
`product_in_cart_id` int NOT NULL AUTO_INCREMENT,
`cart_id` int NOT NULL,
`product_id` int NOT NULL,
`store_id` int NOT NULL,
`quantity` int NOT NULL,
`price` float NOT NULL,
PRIMARY KEY (`product_in_cart_id`),
KEY `prodincart_cart_fk` (`cart_id`),
KEY `prodincart_prod_fk` (`product_id`),
KEY `prodincart_store_fk` (`store_id`),
CONSTRAINT `prodincart_cart_fk` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`cart_id`),
CONSTRAINT `prodincart_product_fk` FOREIGN KEY (`product_id`) REFERENCES `product`(`product_id`),
CONSTRAINT `prodincart_store_fk` FOREIGN KEY (`store_id`) REFERENCES `store`(`store_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;



CREATE TABLE `cart_shared` (
`cart_shared_id` int NOT NULL AUTO_INCREMENT,
`cart_id` int NOT NULL,
`user_id` int NOT NULL,
PRIMARY KEY (`cart_shared_id`),
KEY `cartshared_cart_fk` (`cart_id`),
KEY `cartshared_user_fk` (`user_id`),
CONSTRAINT `cartshared_cart_fk` FOREIGN KEY (`cart_id`) REFERENCES `cart` (`cart_id`),
CONSTRAINT `cartshared_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user_details`(`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


CREATE TABLE `combo` (
`combo_id` int NOT NULL AUTO_INCREMENT,
`combo_name` varchar(255) DEFAULT NULL,
`merchant_id` int NOT NULL,
PRIMARY KEY (`combo_id`),
KEY `combo_merchant_fk` (`merchant_id`),
CONSTRAINT `combo_merchant_fk` FOREIGN KEY (`merchant_id`) REFERENCES `merchant_details` (`merchant_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;


CREATE TABLE `deals` (
`deal_id` int NOT NULL AUTO_INCREMENT,
`deal_name` varchar(255) DEFAULT NULL,
`combo_id` int NOT NULL,
`inventory_id` int NOT NULL,
`percentage_off` float NOT NULL,
PRIMARY KEY (`deal_id`),
KEY `deals_combo_fk` (`combo_id`),
KEY `deals_inventory_fk` (`inventory_id`),
CONSTRAINT `deals_combo_fk` FOREIGN KEY (`combo_id`) REFERENCES `combo` (`combo_id`),
CONSTRAINT `deals_inventory_fk` FOREIGN KEY (`inventory_id`) REFERENCES `product_inventory`(`inventory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

LOAD DATA LOCAL INFILE 'pick the sales.csv from the table data'
INTO TABLE   CSCI5308_8_TEST.sales
FIELDS TERMINATED BY ','
ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;




