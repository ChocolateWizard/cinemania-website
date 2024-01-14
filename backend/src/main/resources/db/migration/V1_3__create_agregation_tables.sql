CREATE TABLE `acting` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `media_id` bigint unsigned NOT NULL,
  `actor_id` bigint unsigned NOT NULL,
  `is_starring` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `actings_media_actor_unique` (`media_id`,`actor_id`),
  KEY `actor_id` (`actor_id`),
  CONSTRAINT `acting_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `acting_ibfk_2` FOREIGN KEY (`actor_id`) REFERENCES `actor` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `critique` (
  `user_critic_id` bigint unsigned NOT NULL,
  `media_id` bigint unsigned NOT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `rating` int NOT NULL,
  PRIMARY KEY (`user_critic_id`,`media_id`),
  KEY `media_id` (`media_id`),
  CONSTRAINT `critique_ibfk_1` FOREIGN KEY (`user_critic_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `critique_ibfk_2` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `CRITIQUE_RATING_DOMAIN` CHECK (((`rating` >= 0) and (`rating` <= 100)))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

