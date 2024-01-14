CREATE TABLE `acting_role` (
  `acting_id` bigint unsigned NOT NULL,
  `id` bigint unsigned NOT NULL,
  `name` varchar(300) NOT NULL,
  PRIMARY KEY (`acting_id`,`id`),
  CONSTRAINT `acting_role_ibfk_1` FOREIGN KEY (`acting_id`) REFERENCES `acting` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
