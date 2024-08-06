/*Table structure for table `country` */

CREATE TABLE `country` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `official_state_name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `countries_code_unique` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


/*Table structure for table `user` */

CREATE TABLE `user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `profile_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `profile_image` varchar(110) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `username` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime,
  `country_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_username_unique` (`username`),
  UNIQUE KEY `users_email_unique` (`email`),
  UNIQUE KEY `users_profile_name_unique` (`profile_name`),
  KEY `UX_users_country_id_isfrom_countries_id` (`country_id`),
  CONSTRAINT `FK_users_country_id_isfrom_countries_id` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `CONST_USER_GENDER` CHECK ((`gender` in (_utf8mb4'M',_utf8mb4'F',_utf8mb4'O'))),
  CONSTRAINT `CONST_USER_ROLE` CHECK ((`role` in (_utf8mb4'REGULAR',_utf8mb4'CRITIC',_utf8mb4'ADMINISTRATOR')))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `genre` */

CREATE TABLE `genre` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `NAME_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `media` */

CREATE TABLE `media` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `release_date` date NOT NULL,
  `cover_image` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `audience_rating` int NOT NULL,
  `critics_rating` int DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime,
  PRIMARY KEY (`id`),
  CONSTRAINT `RATING_DOMAIN` CHECK (((`audience_rating` >= 0) and (`audience_rating` <= 100)))
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `movie` */

CREATE TABLE `movie` (
  `media_id` bigint unsigned NOT NULL,
  `length` int unsigned NOT NULL,
  PRIMARY KEY (`media_id`),
  CONSTRAINT `movie_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `tv_show` */

CREATE TABLE `tv_show` (
  `media_id` bigint unsigned NOT NULL,
  `number_of_seasons` int unsigned NOT NULL,
  PRIMARY KEY (`media_id`),
  CONSTRAINT `tv_show_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `person` */

CREATE TABLE `person` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `profile_photo` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime,
  PRIMARY KEY (`id`),
  CONSTRAINT `CONST_PERSON_GENDER` CHECK ((`gender` in (_utf8mb4'M',_utf8mb4'F',_utf8mb4'O')))
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Table structure for table `director` */

CREATE TABLE `director` (
  `person_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`person_id`),
  CONSTRAINT `director_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `writer` */

CREATE TABLE `writer` (
  `person_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`person_id`),
  CONSTRAINT `writer_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `actor` */

CREATE TABLE `actor` (
  `person_id` bigint unsigned NOT NULL,
  `is_star` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`person_id`),
  CONSTRAINT `actor_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

