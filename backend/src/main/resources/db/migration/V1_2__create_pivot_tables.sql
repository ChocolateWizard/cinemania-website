/*Table structure for table `user_media` */

CREATE TABLE `user_media` (
  `user_id` bigint unsigned NOT NULL,
  `media_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`user_id`,`media_id`),
  KEY `media_id` (`media_id`),
  CONSTRAINT `user_media_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_media_ibfk_2` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `media_genres` */

CREATE TABLE `media_genres` (
  `media_id` bigint unsigned NOT NULL,
  `genre_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`media_id`,`genre_id`),
  KEY `id_genre` (`genre_id`),
  CONSTRAINT `media_genres_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `media_genres_ibfk_2` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `media_directors` */

CREATE TABLE `media_directors` (
  `media_id` bigint unsigned NOT NULL,
  `director_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`media_id`,`director_id`),
  KEY `id_person` (`director_id`),
  CONSTRAINT `media_directors_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `media_directors_ibfk_2` FOREIGN KEY (`director_id`) REFERENCES `director` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `media_writers` */

CREATE TABLE `media_writers` (
  `media_id` bigint unsigned NOT NULL,
  `writer_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`media_id`,`writer_id`),
  KEY `id_person` (`writer_id`),
  CONSTRAINT `media_writers_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `media_writers_ibfk_2` FOREIGN KEY (`writer_id`) REFERENCES `writer` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `acting` */