/*
SQLyog Community
MySQL - 8.0.29 : Database - cinemania_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

DROP TRIGGER IF EXISTS `aftcritins`;
DROP TRIGGER IF EXISTS `aftcritupd`;
DROP TRIGGER IF EXISTS `aftcritdel`;

DROP TABLE IF EXISTS `user_media`;
DROP TABLE IF EXISTS `media_genres`;
DROP TABLE IF EXISTS `media_directors`;
DROP TABLE IF EXISTS `media_writers`;
DROP TABLE IF EXISTS `acting_role`;
DROP TABLE IF EXISTS `acting`;
DROP TABLE IF EXISTS `critique`;
DROP TABLE IF EXISTS `director`;
DROP TABLE IF EXISTS `writer`;
DROP TABLE IF EXISTS `actor`;
DROP TABLE IF EXISTS `person`;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS `country`;
DROP TABLE IF EXISTS `genre`;
DROP TABLE IF EXISTS `movie`;
DROP TABLE IF EXISTS `tv_show`;
DROP TABLE IF EXISTS `media`;

/*===============================================================================================================*/

/*Table structure for table `country` */

CREATE TABLE `country` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `official_state_name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `countries_code_unique` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=250 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


/*Table structure for table `user` */

CREATE TABLE `user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `profile_name` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `profile_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
  `username` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `role` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `media` */

CREATE TABLE `media` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `release_date` date NOT NULL,
  `cover_image` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `audience_rating` int NOT NULL,
  `critic_rating` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `RATING_DOMAIN` CHECK (((`audience_rating` >= 0) and (`audience_rating` <= 100)))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  `profile_photo` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `CONST_PERSON_GENDER` CHECK ((`gender` in (_utf8mb4'M',_utf8mb4'F',_utf8mb4'O')))
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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

/*Table structure for table `acting_role` */

CREATE TABLE `acting_role` (
  `acting_id` bigint unsigned NOT NULL,
  `id` bigint unsigned NOT NULL,
  `name` varchar(300) NOT NULL,
  PRIMARY KEY (`acting_id`,`id`),
  CONSTRAINT `acting_role_ibfk_1` FOREIGN KEY (`acting_id`) REFERENCES `acting` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Table structure for table `critique` */

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


/*========================================================================================================================================*/

/* Triggers structure for table `critique` */

CREATE TRIGGER `aftcritins` AFTER INSERT ON `critique` FOR EACH ROW 
UPDATE media SET critic_rating=(SELECT ROUND(AVG(rating)) FROM critique WHERE media.id = critique.media_id)
WHERE id=NEW.media_id;

CREATE  TRIGGER `aftcritupd` AFTER UPDATE ON `critique` FOR EACH ROW 
UPDATE media SET critic_rating=(SELECT ROUND(AVG(rating)) FROM critique WHERE media.id = critique.media_id)
WHERE id=NEW.media_id;

CREATE  TRIGGER `aftcritdel` AFTER DELETE ON `critique` FOR EACH ROW 
UPDATE media SET critic_rating=(SELECT ROUND(AVG(rating)) FROM critique WHERE media.id = critique.media_id)
WHERE id=OLD.media_id;


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
