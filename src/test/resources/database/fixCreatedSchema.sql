CREATE TABLE `assets` (
  `price` float NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `asset_type` varchar(255) DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `exchange` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `symbol` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12592 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `cash` (
  `value` float NOT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE `operation_history` (
  `price` float NOT NULL,
  `quantity` float NOT NULL,
  `value_operation` float NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `wallet_id` bigint DEFAULT NULL,
  `exchange` varchar(255) DEFAULT NULL,
  `position_currency` varchar(255) DEFAULT NULL,
  `symbol` varchar(255) DEFAULT NULL,
  `wallet_currency` varchar(255) DEFAULT NULL,
  `wallet_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKg1t9ajl8isamsc83om24oikkq` (`wallet_id`),
  CONSTRAINT `FKg1t9ajl8isamsc83om24oikkq` FOREIGN KEY (`wallet_id`) REFERENCES `wallets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `positions` (
  `actual_price` float NOT NULL,
  `average_purchase_price` float NOT NULL,
  `daily_price_change` float NOT NULL,
  `percentage_of_the_portfolio` float NOT NULL,
  `quantity` float NOT NULL,
  `rate_of_return` float NOT NULL,
  `result` float NOT NULL,
  `value_based_on_purchase_price` float NOT NULL,
  `value_of_position` float NOT NULL,
  `actual_price_date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `position_opening_date` datetime(6) DEFAULT NULL,
  `basic_currency` varchar(255) DEFAULT NULL,
  `exchange` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `symbol` varchar(255) DEFAULT NULL,
  `user_currency` varchar(255) DEFAULT NULL,
  `wallet_currency` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` enum('ADMIN','USER') DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_cash_transactions` (
  `value` float NOT NULL,
  `date` datetime(6) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `wallet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK526smjsarqfh9gnofb29byn7g` (`wallet_id`),
  CONSTRAINT `FK526smjsarqfh9gnofb29byn7g` FOREIGN KEY (`wallet_id`) REFERENCES `wallets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `user_roles` (
  `role_id` int NOT NULL,
  `user_id` bigint NOT NULL,
  PRIMARY KEY (`role_id`,`user_id`),
  KEY `FKhfh9dx7w3ubf1co1vdev94g3f` (`user_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK3g1j96g94xpk3lpxl2qbl985x` (`name`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `users_wallets` (
  `user_entity_id` bigint NOT NULL,
  `wallets_id` bigint NOT NULL,
  PRIMARY KEY (`user_entity_id`,`wallets_id`),
  UNIQUE KEY `UK_lv7t9sfnkja9mqf9hiqoh907v` (`wallets_id`),
  CONSTRAINT `FKkm4x2jd7lxl3j3hi3530d6aev` FOREIGN KEY (`wallets_id`) REFERENCES `wallets` (`id`),
  CONSTRAINT `FKoodpjdells3n6u114mggwk1od` FOREIGN KEY (`user_entity_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `wallets` (
  `actual_value` float NOT NULL,
  `basic_value` float NOT NULL,
  `cash_entity_id` bigint DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `currency` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_h5h9wb7bcuo372dqetbhuodys` (`cash_entity_id`),
  KEY `FKc1foyisidw7wqqrkamafuwn4e` (`user_id`),
  CONSTRAINT `FKc1foyisidw7wqqrkamafuwn4e` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKdveagqrcorh1ppjrwvtovo295` FOREIGN KEY (`cash_entity_id`) REFERENCES `cash` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `wallets_positions` (
  `positions_id` bigint NOT NULL,
  `wallet_entity_id` bigint NOT NULL,
  PRIMARY KEY (`positions_id`,`wallet_entity_id`),
  UNIQUE KEY `UK_or99asctu6f61gb2eerrx1mna` (`positions_id`),
  KEY `FKb9w1ej0r9oi0xx79288qdf13t` (`wallet_entity_id`),
  CONSTRAINT `FK250h20l23vx4blfilvoesevgf` FOREIGN KEY (`positions_id`) REFERENCES `positions` (`id`),
  CONSTRAINT `FKb9w1ej0r9oi0xx79288qdf13t` FOREIGN KEY (`wallet_entity_id`) REFERENCES `wallets` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



