/*
SQLyog Community v13.1.9 (64 bit)
MySQL - 8.0.29 : Database - kinomania_backend
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
-- CREATE DATABASE /*!32312 IF NOT EXISTS*/`kinomania_backend` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
-- 
-- USE `kinomania_backend`;



-- DDL


DROP TABLE IF EXISTS `country`;

CREATE TABLE `country` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(300) NOT NULL,
  `official_state_name` varchar(300) NOT NULL,
  `code` varchar(2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `countries_code_unique` (`code`)
);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `profile_image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'src\\main\\resources\\database\\images\\user\\Default_profile_photo.png',
  `username` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `country_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `users_username_unique` (`username`),
  UNIQUE KEY `users_email_unique` (`email`),
  KEY `UX_users_country_id_isfrom_countries_id` (`country_id`),
  CONSTRAINT `FK_users_country_id_isfrom_countries_id` FOREIGN KEY (`country_id`) REFERENCES `country` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT `CONST_USER_GENDER` CHECK ((`gender` in (_utf8mb4'M',_utf8mb4'F',_utf8mb4'O')))
);

/*Table structure for table `user_critic` */

DROP TABLE IF EXISTS `user_critic`;

CREATE TABLE `user_critic` (
  `user_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `user_critic_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);


/*Table structure for table `user_regular` */

DROP TABLE IF EXISTS `user_regular`;

CREATE TABLE `user_regular` (
  `user_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `user_regular_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `person` */

DROP TABLE IF EXISTS `person`;

CREATE TABLE `person` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `profile_photo_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '/',
  PRIMARY KEY (`id`),
  CONSTRAINT `CONST_PERSON_GENDER` CHECK ((`gender` in (_utf8mb4'M',_utf8mb4'F',_utf8mb4'O')))
);

/*Table structure for table `writer` */

DROP TABLE IF EXISTS `writer`;

CREATE TABLE `writer` (
  `person_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`person_id`),
  CONSTRAINT `writer_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `director` */

DROP TABLE IF EXISTS `director`;

CREATE TABLE `director` (
  `person_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`person_id`),
  CONSTRAINT `director_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `actor` */

DROP TABLE IF EXISTS `actor`;

CREATE TABLE `actor` (
  `person_id` bigint unsigned NOT NULL,
  `is_star` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`person_id`),
  CONSTRAINT `actor_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `genre` */

DROP TABLE IF EXISTS `genre`;

CREATE TABLE `genre` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `NAME_UNIQUE` (`name`)
);

/*Table structure for table `media` */

DROP TABLE IF EXISTS `media`;

CREATE TABLE `media` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `release_date` date NOT NULL,
  `cover_image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `rating` int NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `RATING_DOMAIN` CHECK (((`rating` >= 0) and (`rating` <= 100)))
);



/*Table structure for table `movie` */

DROP TABLE IF EXISTS `movie`;

CREATE TABLE `movie` (
  `media_id` bigint unsigned NOT NULL,
  `length` int NOT NULL,
  PRIMARY KEY (`media_id`),
  CONSTRAINT `movie_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `tv_show` */

DROP TABLE IF EXISTS `tv_show`;

CREATE TABLE `tv_show` (
  `media_id` bigint unsigned NOT NULL,
  `number_of_seasons` int NOT NULL,
  PRIMARY KEY (`media_id`),
  CONSTRAINT `tv_show_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);







-- --------------------PIVOT---------------------------------------------
/*Table structure for table `user_media` */

DROP TABLE IF EXISTS `user_media`;

CREATE TABLE `user_media` (
  `user_id` bigint unsigned NOT NULL,
  `media_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`user_id`,`media_id`),
  KEY `media_id` (`media_id`),
  CONSTRAINT `user_media_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_media_ibfk_2` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `critique` */

DROP TABLE IF EXISTS `critique`;

CREATE TABLE `critique` (
  `user_critic_id` bigint unsigned NOT NULL,
  `media_id` bigint unsigned NOT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `rating` int NOT NULL,
  PRIMARY KEY (`user_critic_id`,`media_id`),
  KEY `media_id` (`media_id`),
  CONSTRAINT `critique_ibfk_1` FOREIGN KEY (`user_critic_id`) REFERENCES `user_critic` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `critique_ibfk_2` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `CRITIQUE_RATING_DOMAIN` CHECK (((`rating` >= 0) and (`rating` <= 100)))
);

/*Table structure for table `acting` */

DROP TABLE IF EXISTS `acting`;

CREATE TABLE `acting` (
  `media_id` bigint unsigned NOT NULL,
  `actor_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`media_id`,`actor_id`),
  KEY `person_id` (`actor_id`),
  CONSTRAINT `acting_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `acting_ibfk_2` FOREIGN KEY (`actor_id`) REFERENCES `actor` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `acting_role` */

DROP TABLE IF EXISTS `acting_role`;

CREATE TABLE `acting_role` (
  `acting_media_id` bigint unsigned NOT NULL,
  `acting_actor_id` bigint unsigned NOT NULL,
  `id` bigint unsigned NOT NULL,
  `name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`acting_media_id`,`acting_actor_id`,`id`),
  KEY `order_num` (`id`),
  KEY `media_actors_media_id` (`acting_actor_id`),
  CONSTRAINT `acting_role_ibfk_1` FOREIGN KEY (`acting_media_id`) REFERENCES `acting` (`media_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `acting_role_ibfk_2` FOREIGN KEY (`acting_actor_id`) REFERENCES `acting` (`actor_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `media_directors` */

DROP TABLE IF EXISTS `media_directors`;

CREATE TABLE `media_directors` (
  `media_id` bigint unsigned NOT NULL,
  `director_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`media_id`,`director_id`),
  KEY `id_person` (`director_id`),
  CONSTRAINT `media_directors_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `media_directors_ibfk_2` FOREIGN KEY (`director_id`) REFERENCES `director` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `media_genres` */

DROP TABLE IF EXISTS `media_genres`;

CREATE TABLE `media_genres` (
  `media_id` bigint unsigned NOT NULL,
  `genre_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`media_id`,`genre_id`),
  KEY `id_genre` (`genre_id`),
  CONSTRAINT `media_genres_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `media_genres_ibfk_2` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `media_writers` */

DROP TABLE IF EXISTS `media_writers`;

CREATE TABLE `media_writers` (
  `media_id` bigint unsigned NOT NULL,
  `writer_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`media_id`,`writer_id`),
  KEY `id_person` (`writer_id`),
  CONSTRAINT `media_writers_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `media_writers_ibfk_2` FOREIGN KEY (`writer_id`) REFERENCES `writer` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
);

-- ==============================================



/*Data for the table `acting` */

insert  into `acting`(`media_id`,`actor_id`) values 
(18,22),
(18,23),
(18,24);



/*Data for the table `acting_role` */

insert  into `acting_role`(`acting_media_id`,`acting_actor_id`,`id`,`name`) values 
(18,22,1,'Betty Elms'),
(18,23,1,'Rita'),
(18,24,1,'Adam');



/*Data for the table `actor` */

insert  into `actor`(`person_id`,`is_star`) values 
(22,1),
(23,1),
(24,1);

/*Table structure for table `country` */



/*Data for the table `country` */

insert  into `country`(`id`,`name`,`official_state_name`,`code`) values 
(1,'Afghanistan','The Islamic Republic of Afghanistan','AF'),
(2,'Åland Islands','Åland','AX'),
(3,'Albania','The Republic of Albania','AL'),
(4,'Algeria','The People\'s Democratic Republic of Algeria','DZ'),
(5,'American Samoa','The Territory of American Samoa','AS'),
(6,'Andorra','The Principality of Andorra','AD'),
(7,'Angola','The Republic of Angola','AO'),
(8,'Anguilla','Anguilla','AI'),
(9,'Antarctica','All land and ice shelves south of the 60th parallel south','AQ'),
(10,'Antigua and Barbuda','Antigua and Barbuda','AG'),
(11,'Argentina','The Argentine Republic','AR'),
(12,'Armenia','The Republic of Armenia','AM'),
(13,'Aruba','Aruba','AW'),
(14,'Australia','The Commonwealth of Australia','AU'),
(15,'Austria','The Republic of Austria','AT'),
(16,'Azerbaijan','The Republic of Azerbaijan','AZ'),
(17,'Bahamas','The Commonwealth of The Bahamas','BS'),
(18,'Bahrain','The Kingdom of Bahrain','BH'),
(19,'Bangladesh','The People\'s Republic of Bangladesh','BD'),
(20,'Barbados','Barbados','BB'),
(21,'Belarus','The Republic of Belarus','BY'),
(22,'Belgium','The Kingdom of Belgium','BE'),
(23,'Belize','Belize','BZ'),
(24,'Benin','The Republic of Benin','BJ'),
(25,'Bermuda','Bermuda','BM'),
(26,'Bhutan','The Kingdom of Bhutan','BT'),
(27,'Bolivia','The Plurinational State of Bolivia','BO'),
(28,'Bonaire, Sint Eustatius and Saba','Bonaire, Sint Eustatius and Saba','BQ'),
(29,'Bosnia and Herzegovina','Bosnia and Herzegovina','BA'),
(30,'Botswana','The Republic of Botswana','BW'),
(31,'Bouvet Island','Bouvet Island','BV'),
(32,'Brazil','The Federative Republic of Brazil','BR'),
(33,'British Indian Ocean Territory','The British Indian Ocean Territory','IO'),
(34,'Brunei Darussalam','The Nation of Brunei, the Abode of Peace','BN'),
(35,'Bulgaria','The Republic of Bulgaria','BG'),
(36,'Burkina Faso','Burkina Faso','BF'),
(37,'Burundi','The Republic of Burundi','BI'),
(38,'Cabo Verde','The Republic of Cabo Verde','CV'),
(39,'Cambodia','The Kingdom of Cambodia','KH'),
(40,'Cameroon','The Republic of Cameroon','CM'),
(41,'Canada','Canada','CA'),
(42,'Cayman Islands','The Cayman Islands','KY'),
(43,'Central African Republic','The Central African Republic','CF'),
(44,'Chad','The Republic of Chad','TD'),
(45,'Chile','The Republic of Chile','CL'),
(46,'China','The People\'s Republic of China','CN'),
(47,'Christmas Island','The Territory of Christmas Island','CX'),
(48,'Cocos (Keeling) Islands','The Territory of Cocos (Keeling) Islands','CC'),
(49,'Colombia','The Republic of Colombia','CO'),
(50,'Comoros','The Union of the Comoros','KM'),
(51,'Congo','The Republic of the Congo','CG'),
(52,'DR Congo','The Democratic Republic of the Congo','CD'),
(53,'Cook Islands','The Cook Islands','CK'),
(54,'Costa Rica','The Republic of Costa Rica','CR'),
(55,'Côte d\'Ivoire','The Republic of Côte d\'Ivoire','CI'),
(56,'Croatia','The Republic of Croatia','HR'),
(57,'Cuba','The Republic of Cuba','CU'),
(58,'Curaçao','The Country of Curaçao','CW'),
(59,'Cyprus','The Republic of Cyprus','CY'),
(60,'Czech Republic','The Czech Republic','CZ'),
(61,'Denmark','The Kingdom of Denmark','DK'),
(62,'Djibouti','The Republic of Djibouti','DJ'),
(63,'Dominica','The Commonwealth of Dominica','DM'),
(64,'Dominican Republic','The Dominican Republic','DO'),
(65,'Ecuador','The Republic of Ecuador','EC'),
(66,'Egypt','The Arab Republic of Egypt','EG'),
(67,'El Salvador','The Republic of El Salvador','SV'),
(68,'Equatorial Guinea','The Republic of Equatorial Guinea','GQ'),
(69,'Eritrea','The State of Eritrea','ER'),
(70,'Estonia','The Republic of Estonia','EE'),
(71,'Eswatini','The Kingdom of Eswatini','SZ'),
(72,'Ethiopia','The Federal Democratic Republic of Ethiopia','ET'),
(73,'Falkland Islands (Malvinas)','The Falkland Islands','FK'),
(74,'Faroe Islands','The Faroe Islands','FO'),
(75,'Fiji','The Republic of Fiji','FJ'),
(76,'Finland','The Republic of Finland','FI'),
(77,'France','The French Republic','FR'),
(78,'French Guiana','Guyane','GF'),
(79,'French Polynesia','French Polynesia','PF'),
(80,'French Southern Territories','The French Southern and Antarctic Lands','TF'),
(81,'Gabon','The Gabonese Republic','GA'),
(82,'Gambia','The Republic of The Gambia','GM'),
(83,'Georgia','Georgia','GE'),
(84,'Germany','The Federal Republic of Germany','DE'),
(85,'Ghana','The Republic of Ghana','GH'),
(86,'Gibraltar','Gibraltar','GI'),
(87,'Greece','The Hellenic Republic','GR'),
(88,'Greenland','Kalaallit Nunaat','GL'),
(89,'Grenada','Grenada','GD'),
(90,'Guadeloupe','Guadeloupe','GP'),
(91,'Guam','The Territory of Guam','GU'),
(92,'Guatemala','The Republic of Guatemala','GT'),
(93,'Guernsey','The Bailiwick of Guernsey','GG'),
(94,'Guinea','The Republic of Guinea','GN'),
(95,'Guinea-Bissau','The Republic of Guinea-Bissau','GW'),
(96,'Guyana','The Co-operative Republic of Guyana','GY'),
(97,'Haiti','The Republic of Haiti','HT'),
(98,'Heard Island and McDonald Islands','The Territory of Heard Island and McDonald Islands','HM'),
(99,'Holy See (Vatican City State)','The Holy See','VA'),
(100,'Honduras','The Republic of Honduras','HN'),
(101,'Hong Kong','The Hong Kong Special Administrative Region of China','HK'),
(102,'Hungary','Hungary','HU'),
(103,'Iceland','Iceland','IS'),
(104,'India','The Republic of India','IN'),
(105,'Indonesia','The Republic of Indonesia','ID'),
(106,'Iran','The Islamic Republic of Iran','IR'),
(107,'Iraq','The Republic of Iraq','IQ'),
(108,'Ireland','Ireland','IE'),
(109,'Isle of Man','The Isle of Man','IM'),
(110,'Israel','The State of Israel','IL'),
(111,'Italy','The Italian Republic','IT'),
(112,'Jamaica','Jamaica','JM'),
(113,'Japan','Japan','JP'),
(114,'Jersey','The Bailiwick of Jersey','JE'),
(115,'Jordan','The Hashemite Kingdom of Jordan','JO'),
(116,'Kazakhstan','The Republic of Kazakhstan','KZ'),
(117,'Kenya','The Republic of Kenya','KE'),
(118,'Kiribati','The Republic of Kiribati','KI'),
(119,'North Korea','The Democratic People\'s Republic of Korea','KP'),
(120,'South Korea','The Republic of Korea','KR'),
(121,'Kuwait','The State of Kuwait','KW'),
(122,'Kyrgyzstan','The Kyrgyz Republic','KG'),
(123,'Lao People\'s Democratic Republic','The Lao People\'s Democratic Republic','LA'),
(124,'Latvia','The Republic of Latvia','LV'),
(125,'Lebanon','The Lebanese Republic','LB'),
(126,'Lesotho','The Kingdom of Lesotho','LS'),
(127,'Liberia','The Republic of Liberia','LR'),
(128,'Libya','The State of Libya','LY'),
(129,'Liechtenstein','The Principality of Liechtenstein','LI'),
(130,'Lithuania','The Republic of Lithuania','LT'),
(131,'Luxembourg','The Grand Duchy of Luxembourg','LU'),
(132,'Macao','The Macao Special Administrative Region of China','MO'),
(133,'North Macedonia','The Republic of North Macedonia','MK'),
(134,'Madagascar','The Republic of Madagascar','MG'),
(135,'Malawi','The Republic of Malawi','MW'),
(136,'Malaysia','Malaysia','MY'),
(137,'Maldives','The Republic of Maldives','MV'),
(138,'Mali','The Republic of Mali','ML'),
(139,'Malta','The Republic of Malta','MT'),
(140,'Marshall Islands','The Republic of the Marshall Islands','MH'),
(141,'Martinique','Martinique','MQ'),
(142,'Mauritania','The Islamic Republic of Mauritania','MR'),
(143,'Mauritius','The Republic of Mauritius','MU'),
(144,'Mayotte','The Department of Mayotte','YT'),
(145,'Mexico','The United Mexican States','MX'),
(146,'Micronesia','The Federated States of Micronesia','FM'),
(147,'Moldova','The Republic of Moldova','MD'),
(148,'Monaco','The Principality of Monaco','MC'),
(149,'Mongolia','Mongolia','MN'),
(150,'Montenegro','Montenegro','ME'),
(151,'Montserrat','Montserrat','MS'),
(152,'Morocco','The Kingdom of Morocco','MA'),
(153,'Mozambique','The Republic of Mozambique','MZ'),
(154,'Myanmar','The Republic of the Union of Myanmar','MM'),
(155,'Namibia','The Republic of Namibia','NA'),
(156,'Nauru','The Republic of Nauru','NR'),
(157,'Nepal','The Federal Democratic Republic of Nepal','NP'),
(158,'Netherlands','The Kingdom of the Netherlands','NL'),
(159,'New Caledonia','New Caledonia','NC'),
(160,'New Zealand','New Zealand','NZ'),
(161,'Nicaragua','The Republic of Nicaragua','NI'),
(162,'Niger','The Republic of the Niger','NE'),
(163,'Nigeria','The Federal Republic of Nigeria','NG'),
(164,'Niue','Niue','NU'),
(165,'Norfolk Island','The Territory of Norfolk Island','NF'),
(166,'Northern Mariana Islands','The Commonwealth of the Northern Mariana Islands','MP'),
(167,'Norway','The Kingdom of Norway','NO'),
(168,'Oman','The Sultanate of Oman','OM'),
(169,'Pakistan','The Islamic Republic of Pakistan','PK'),
(170,'Palau','The Republic of Palau','PW'),
(171,'Palestine','The State of Palestine','PS'),
(172,'Panama','The Republic of Panamá','PA'),
(173,'Papua New Guinea','The Independent State of Papua New Guinea','PG'),
(174,'Paraguay','The Republic of Paraguay','PY'),
(175,'Peru','The Republic of Perú','PE'),
(176,'Philippines','The Republic of the Philippines','PH'),
(177,'Pitcairn','The Pitcairn, Henderson, Ducie and Oeno Islands','PN'),
(178,'Poland','The Republic of Poland','PL'),
(179,'Portugal','The Portuguese Republic','PT'),
(180,'Puerto Rico','The Commonwealth of Puerto Rico','PR'),
(181,'Qatar','The State of Qatar','QA'),
(182,'Réunion','Réunion','RE'),
(183,'Romania','Romania','RO'),
(184,'Russia','The Russian Federation','RU'),
(185,'Rwanda','The Republic of Rwanda','RW'),
(186,'Saint Barthélemy','The Collectivity of Saint-Barthélemy','BL'),
(187,'Saint Helena, Ascension and Tristan da Cunha','Saint Helena, Ascension and Tristan da Cunha','SH'),
(188,'Saint Kitts and Nevis','Saint Kitts and Nevis','KN'),
(189,'Saint Lucia','Saint Lucia','LC'),
(190,'Saint Martin (French part)','The Collectivity of Saint-Martin','MF'),
(191,'Saint Pierre and Miquelon','The Overseas Collectivity of Saint-Pierre and Miquelon','PM'),
(192,'Saint Vincent and the Grenadines','Saint Vincent and the Grenadines','VC'),
(193,'Samoa','The Independent State of Samoa','WS'),
(194,'San Marino','The Republic of San Marino','SM'),
(195,'Sao Tome and Principe','The Democratic Republic of São Tomé and Príncipe','ST'),
(196,'Saudi Arabia','The Kingdom of Saudi Arabia','SA'),
(197,'Senegal','The Republic of Senegal','SN'),
(198,'Serbia','The Republic of Serbia','RS'),
(199,'Seychelles','The Republic of Seychelles','SC'),
(200,'Sierra Leone','The Republic of Sierra Leone','SL'),
(201,'Singapore','The Republic of Singapore','SG'),
(202,'Sint Maarten (Dutch part)','Sint Maarten','SX'),
(203,'Slovakia','The Slovak Republic','SK'),
(204,'Slovenia','The Republic of Slovenia','SI'),
(205,'Solomon Islands','The Solomon Islands','SB'),
(206,'Somalia','The Federal Republic of Somalia','SO'),
(207,'South Africa','The Republic of South Africa','ZA'),
(208,'South Georgia and the South Sandwich Islands','South Georgia and the South Sandwich Islands','GS'),
(209,'South Sudan','The Republic of South Sudan','SS'),
(210,'Spain','The Kingdom of Spain','ES'),
(211,'Sri Lanka','The Democratic Socialist Republic of Sri Lanka','LK'),
(212,'Sudan','The Republic of the Sudan','SD'),
(213,'Suriname','The Republic of Suriname','SR'),
(214,'Svalbard and Jan Mayen','Svalbard and Jan Mayen','SJ'),
(215,'Sweden','The Kingdom of Sweden','SE'),
(216,'Switzerland','The Swiss Confederation','CH'),
(217,'Syrian Arab Republic','The Syrian Arab Republic','SY'),
(218,'Taiwan','The Republic of China','TW'),
(219,'Tajikistan','The Republic of Tajikistan','TJ'),
(220,'Tanzania','The United Republic of Tanzania','TZ'),
(221,'Thailand','The Kingdom of Thailand','TH'),
(222,'Timor-Leste','The Democratic Republic of Timor-Leste','TL'),
(223,'Togo','The Togolese Republic','TG'),
(224,'Tokelau','Tokelau','TK'),
(225,'Tonga','The Kingdom of Tonga','TO'),
(226,'Trinidad and Tobago','The Republic of Trinidad and Tobago','TT'),
(227,'Tunisia','The Republic of Tunisia','TN'),
(228,'Turkiye','The Republic of Türkiye','TR'),
(229,'Turkmenistan','Turkmenistan','TM'),
(230,'Turks and Caicos Islands','The Turks and Caicos Islands','TC'),
(231,'Tuvalu','Tuvalu','TV'),
(232,'Uganda','The Republic of Uganda','UG'),
(233,'Ukraine','Ukraine','UA'),
(234,'United Arab Emirates','The United Arab Emirates','AE'),
(235,'United Kingdom','The United Kingdom of Great Britain and Northern Ireland','GB'),
(236,'United States','The United States of America','US'),
(237,'United States Minor Outlying Islands','Baker Island, Howland Island, Jarvis Island, Johnston Atoll, Kingman Reef, Midway Atoll, Navassa Island, Palmyra Atoll, and Wake Island','UM'),
(238,'Uruguay','The Oriental Republic of Uruguay','UY'),
(239,'Uzbekistan','The Republic of Uzbekistan','UZ'),
(240,'Vanuatu','The Republic of Vanuatu','VU'),
(241,'Venezuela','The Bolivarian Republic of Venezuela','VE'),
(242,'Viet Nam','The Socialist Republic of Viet Nam','VN'),
(243,'Virgin Islands, British','The Virgin Islands','VG'),
(244,'Virgin Islands, U.S.','The Virgin Islands of the United States','VI'),
(245,'Wallis and Futuna','The Territory of the Wallis and Futuna Islands','WF'),
(246,'Western Sahara','The Sahrawi Arab Democratic Republic','EH'),
(247,'Yemen','The Republic of Yemen','YE'),
(248,'Zambia','The Republic of Zambia','ZM'),
(249,'Zimbabwe','The Republic of Zimbabwe','ZW');



/*Data for the table `critique` */



/*Data for the table `director` */

insert  into `director`(`person_id`) values 
(21);



/*Data for the table `genre` */

insert  into `genre`(`id`,`name`) values 
(1,'Drama'),
(2,'Mystery'),
(3,'Thriller');



/*Data for the table `media` */

insert  into `media`(`id`,`title`,`release_date`,`cover_image_url`,`description`,`rating`) values 
(18,'Mullholand Drive','2023-07-12','/src/images','Really good movie',89),
(19,'Arcane','2023-07-06','/src/images','Super TV show',79);



/*Data for the table `media_directors` */

insert  into `media_directors`(`media_id`,`director_id`) values 
(18,21);



/*Data for the table `media_genres` */

insert  into `media_genres`(`media_id`,`genre_id`) values 
(18,1),
(18,2),
(18,3);



/*Data for the table `media_writers` */

insert  into `media_writers`(`media_id`,`writer_id`) values 
(18,21);



/*Data for the table `movie` */

insert  into `movie`(`media_id`,`length`) values 
(18,209);



/*Data for the table `person` */

insert  into `person`(`id`,`first_name`,`last_name`,`gender`,`profile_photo_url`) values 
(21,'David','Lynch','M','/'),
(22,'Naomi','Watts','F','/'),
(23,'Laura','Harring','F','/'),
(24,'Justin','Theroux','M','/');



/*Data for the table `tv_show` */



/*Data for the table `user` */

insert  into `user`(`id`,`first_name`,`last_name`,`gender`,`profile_image_url`,`username`,`email`,`password`,`country_id`) values 
(13,'Marko','Markovic','M','src\\main\\resources\\database\\images\\user\\Default_profile_photo.png','a','a@gmail.com','a',198);



/*Data for the table `user_regular` */



/*Data for the table `writer` */

insert  into `writer`(`person_id`) values 
(21);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
