/*
SQLyog Community
MySQL - 8.0.29 : Database - kinomania_backend
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE IF NOT EXISTS `cinemania_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `cinemania_db`;

/*Table structure for table `acting` */

DROP TABLE IF EXISTS `acting`;

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

/*Data for the table `acting` */

insert  into `acting`(`id`,`media_id`,`actor_id`,`is_starring`) values 
(1,1,2,1),
(2,1,3,1),
(3,1,4,1),
(4,1,5,0),
(5,1,6,0),
(6,2,4,1),
(7,2,7,1),
(8,2,8,1),
(9,2,9,1),
(10,2,10,0),
(11,2,11,0),
(12,2,12,0),
(13,3,13,1),
(14,3,20,1),
(15,3,21,1),
(16,3,22,0),
(17,3,23,0),
(18,3,24,0),
(19,3,25,0),
(20,4,28,1),
(21,4,29,1),
(22,4,30,1),
(23,5,35,1),
(24,5,36,1),
(25,5,37,1),
(26,5,38,0),
(27,5,39,0),
(28,5,40,0),
(29,5,41,0),
(30,5,42,0),
(31,5,43,0),
(32,5,44,0),
(33,5,45,0),
(34,6,46,1),
(35,6,47,1),
(36,6,49,1),
(37,6,50,0);

/*Table structure for table `acting_role` */

DROP TABLE IF EXISTS `acting_role`;

CREATE TABLE `acting_role` (
  `acting_id` bigint unsigned NOT NULL,
  `id` bigint unsigned NOT NULL,
  `name` varchar(300) NOT NULL,
  PRIMARY KEY (`acting_id`,`id`),
  CONSTRAINT `acting_role_ibfk_1` FOREIGN KEY (`acting_id`) REFERENCES `acting` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `acting_role` */

insert  into `acting_role`(`acting_id`,`id`,`name`) values 
(1,1,'Betty Elms'),
(1,2,'Diane Selwyn'),
(2,1,'Rita'),
(2,2,'Camilla Rhodes'),
(3,1,'Adam'),
(4,1,'Dan'),
(5,1,'Irene'),
(6,1,'Devon Berk'),
(6,2,'Billy Side'),
(7,1,'Lost Girl'),
(8,1,'Phantom'),
(9,1,'Visitor #1'),
(10,1,'Nikki Grace'),
(10,2,'Susan Blue'),
(11,1,'Freddie Howard'),
(12,1,'Piotrek Krol'),
(13,1,'Vi'),
(14,1,'Jayce'),
(15,1,'Silico'),
(15,2,'Pim'),
(16,1,'Jinx'),
(17,1,'Caitlyn'),
(18,1,'Viktor'),
(19,1,'Heimerdinger'),
(19,2,'Duty Captain'),
(20,1,'Thomas Howard'),
(21,1,'Thomas Wake'),
(22,1,'Mermaid'),
(23,1,'Hugo \'Hurley\' Reyes'),
(24,1,'James \'Sawyer\' Ford'),
(25,1,'Sun-Hwa Kwon'),
(26,1,'Kate Austen'),
(27,1,'John Locke'),
(27,2,'Man in Black'),
(28,1,'Dr. Jack Shephard'),
(29,1,'Jin-Soo Kwon'),
(30,1,'Sayid Jarrah'),
(31,1,'Claire Littleton'),
(32,1,'Charlie Pace'),
(33,1,'Ben Linus'),
(33,2,'Henry Gale'),
(34,1,'Stan Marsh'),
(34,2,'Eric Cartman'),
(34,3,'Randy Marsh'),
(34,4,'Mr. Garrison'),
(34,5,'Mr. Mackey'),
(34,6,'Clyde'),
(34,7,'Jimmy Valmer'),
(34,8,'Stephen Stotch'),
(34,9,'Officer Barbrady'),
(34,10,'News Reporter'),
(34,11,'TV Announcer'),
(34,12,'Chris Stotch'),
(34,13,'Tom the News Reader'),
(34,14,'Timmy'),
(34,15,'Dr. Doctor'),
(34,16,'Narrator'),
(34,17,'Additional voices'),
(34,18,'PC Principal'),
(34,19,'Phillip'),
(34,20,'Doctor'),
(34,21,'Sgt. Yates'),
(34,22,'Clyde Donovan'),
(34,23,'Mrs. Garrison'),
(34,24,'Grandpa Marsh'),
(34,25,'Mr. Hankey'),
(34,26,'Satan'),
(34,27,'Santa'),
(35,1,'Kyle Broflovski'),
(35,2,'Kenny McCormick'),
(35,3,'Gerald Broflovski'),
(35,4,'Butters Stotch'),
(35,5,'Butters'),
(35,6,'Jimbo Kern'),
(35,7,'Craig'),
(35,8,'Craig Tucker'),
(35,9,'Stuart McCormick'),
(35,10,'Additional voices'),
(35,11,'Priest Maxi'),
(35,12,'Terrance'),
(35,13,'Jesus'),
(35,14,'Pip Pirrup'),
(35,15,'Ted'),
(35,16,'Tweek Tweak'),
(35,17,'Tweek'),
(35,18,'Scott Malkinson'),
(36,1,'Chef'),
(37,1,'Sheila Broflovski'),
(37,2,'Linda Stotch');

/*Table structure for table `actor` */

DROP TABLE IF EXISTS `actor`;

CREATE TABLE `actor` (
  `person_id` bigint unsigned NOT NULL,
  `is_star` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`person_id`),
  CONSTRAINT `actor_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `actor` */

insert  into `actor`(`person_id`,`is_star`) values 
(2,1),
(3,1),
(4,1),
(5,0),
(6,0),
(7,1),
(8,1),
(9,1),
(10,1),
(11,0),
(12,0),
(13,1),
(20,1),
(21,0),
(22,1),
(23,0),
(24,0),
(25,0),
(28,1),
(29,1),
(30,0),
(35,1),
(36,1),
(37,1),
(38,1),
(39,0),
(40,0),
(41,1),
(42,0),
(43,0),
(44,0),
(45,0),
(46,1),
(47,1),
(49,0),
(50,0);

/*Table structure for table `country` */

DROP TABLE IF EXISTS `country`;

CREATE TABLE `country` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `official_state_name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `code` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `countries_code_unique` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=250 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `country` */

insert  into `country`(`id`,`name`,`official_state_name`,`code`) values 
(1,'Afghanistan','The Islamic Republic of Afghanistan','AF'),
(2,'Aland Islands','Aland','AX'),
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
(55,'Co´te d\'Ivoire','The Republic of Co´te d\'Ivoire','CI'),
(56,'Croatia','The Republic of Croatia','HR'),
(57,'Cuba','The Republic of Cuba','CU'),
(58,'CuraÃ§ao','The Country of CuraÃ§ao','CW'),
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
(172,'Panama','The Republic of Panama','PA'),
(173,'Papua New Guinea','The Independent State of Papua New Guinea','PG'),
(174,'Paraguay','The Republic of Paraguay','PY'),
(175,'Peru','The Republic of Peru','PE'),
(176,'Philippines','The Republic of the Philippines','PH'),
(177,'Pitcairn','The Pitcairn, Henderson, Ducie and Oeno Islands','PN'),
(178,'Poland','The Republic of Poland','PL'),
(179,'Portugal','The Portuguese Republic','PT'),
(180,'Puerto Rico','The Commonwealth of Puerto Rico','PR'),
(181,'Qatar','The State of Qatar','QA'),
(182,'Reunion','Reunion','RE'),
(183,'Romania','Romania','RO'),
(184,'Russia','The Russian Federation','RU'),
(185,'Rwanda','The Republic of Rwanda','RW'),
(186,'Saint Barthelemy','The Collectivity of Saint-Barthelemy','BL'),
(187,'Saint Helena, Ascension and Tristan da Cunha','Saint Helena, Ascension and Tristan da Cunha','SH'),
(188,'Saint Kitts and Nevis','Saint Kitts and Nevis','KN'),
(189,'Saint Lucia','Saint Lucia','LC'),
(190,'Saint Martin (French part)','The Collectivity of Saint-Martin','MF'),
(191,'Saint Pierre and Miquelon','The Overseas Collectivity of Saint-Pierre and Miquelon','PM'),
(192,'Saint Vincent and the Grenadines','Saint Vincent and the Grenadines','VC'),
(193,'Samoa','The Independent State of Samoa','WS'),
(194,'San Marino','The Republic of San Marino','SM'),
(195,'Sao Tome and Principe','The Democratic Republic of Sao Tome and Principe','ST'),
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
(228,'Turkiye','The Republic of Turkiye','TR'),
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

/*Table structure for table `critique` */

DROP TABLE IF EXISTS `critique`;

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

/*Data for the table `critique` */

/*Table structure for table `director` */

DROP TABLE IF EXISTS `director`;

CREATE TABLE `director` (
  `person_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`person_id`),
  CONSTRAINT `director_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `director` */

insert  into `director`(`person_id`) values 
(1),
(14),
(15),
(26),
(31),
(32),
(33),
(46),
(47),
(48);

/*Table structure for table `genre` */

DROP TABLE IF EXISTS `genre`;

CREATE TABLE `genre` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `NAME_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `genre` */

insert  into `genre`(`id`,`name`) values 
(1,'Action'),
(2,'Adventure'),
(3,'Animation'),
(4,'Comedy'),
(5,'Devotional'),
(6,'Drama'),
(7,'Historical'),
(8,'Horror'),
(9,'Science fiction'),
(10,'Western'),
(11,'Thriller'),
(12,'Mystery'),
(13,'Fantasy');

/*Table structure for table `media` */

DROP TABLE IF EXISTS `media`;

CREATE TABLE `media` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `release_date` date NOT NULL,
  `cover_image` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `description` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `audience_rating` int NOT NULL,
  `critic_rating` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `RATING_DOMAIN` CHECK (((`audience_rating` >= 0) and (`audience_rating` <= 100)))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `media` */

insert  into `media`(`id`,`title`,`release_date`,`cover_image`,`description`,`audience_rating`,`critic_rating`) values 
(1,'Mulholland Drive','2001-05-16','1.jpg','After a car wreck on the winding Mulholland Drive renders a woman amnesiac, she and a perky Hollywood-hopeful search for clues and answers across Los Angeles in a twisting venture beyond dreams and reality.',79,NULL),
(2,'Inland Empire','2006-09-06','2.jpg','As an actress begins to adopt the persona of her character in a film, her world becomes nightmarish and surreal.',68,NULL),
(3,'Arcane','2021-11-06','3.jpg','Set in Utopian Piltover and the oppressed underground of Zaun, the story follows the origins of two iconic League Of Legends champions and the power that will tear them apart.',90,NULL),
(4,'The Lighthouse','2019-05-19',NULL,'Two lighthouse keepers try to maintain their sanity while living on a remote and mysterious New England island in the 1890s.',74,NULL),
(5,'Lost','2004-09-22',NULL,'The survivors of a plane crash are forced to work together in order to survive on a seemingly deserted tropical island.',83,NULL),
(6,'South Park','1997-08-13','6.jpg','Follows the misadventures of four irreverent grade-schoolers in the quiet, dysfunctional town of South Park, Colorado.',87,NULL);

/*Table structure for table `media_directors` */

DROP TABLE IF EXISTS `media_directors`;

CREATE TABLE `media_directors` (
  `media_id` bigint unsigned NOT NULL,
  `director_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`media_id`,`director_id`),
  KEY `id_person` (`director_id`),
  CONSTRAINT `media_directors_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `media_directors_ibfk_2` FOREIGN KEY (`director_id`) REFERENCES `director` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `media_directors` */

insert  into `media_directors`(`media_id`,`director_id`) values 
(1,1),
(2,1),
(3,14),
(3,15),
(4,26),
(5,31),
(5,32),
(5,33),
(6,46),
(6,47),
(6,48);

/*Table structure for table `media_genres` */

DROP TABLE IF EXISTS `media_genres`;

CREATE TABLE `media_genres` (
  `media_id` bigint unsigned NOT NULL,
  `genre_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`media_id`,`genre_id`),
  KEY `id_genre` (`genre_id`),
  CONSTRAINT `media_genres_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `media_genres_ibfk_2` FOREIGN KEY (`genre_id`) REFERENCES `genre` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `media_genres` */

insert  into `media_genres`(`media_id`,`genre_id`) values 
(3,1),
(3,2),
(5,2),
(3,3),
(6,3),
(6,4),
(1,6),
(2,6),
(4,6),
(5,6),
(4,8),
(1,11),
(1,12),
(2,12),
(2,13),
(4,13),
(5,13);

/*Table structure for table `media_writers` */

DROP TABLE IF EXISTS `media_writers`;

CREATE TABLE `media_writers` (
  `media_id` bigint unsigned NOT NULL,
  `writer_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`media_id`,`writer_id`),
  KEY `id_person` (`writer_id`),
  CONSTRAINT `media_writers_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `media_writers_ibfk_2` FOREIGN KEY (`writer_id`) REFERENCES `writer` (`person_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `media_writers` */

insert  into `media_writers`(`media_id`,`writer_id`) values 
(1,1),
(2,1),
(3,16),
(3,17),
(3,18),
(3,19),
(4,26),
(4,27),
(5,31),
(5,33),
(5,34),
(6,46),
(6,47);

/*Table structure for table `movie` */

DROP TABLE IF EXISTS `movie`;

CREATE TABLE `movie` (
  `media_id` bigint unsigned NOT NULL,
  `length` int unsigned NOT NULL,
  PRIMARY KEY (`media_id`),
  CONSTRAINT `movie_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `movie` */

insert  into `movie`(`media_id`,`length`) values 
(1,147),
(2,180),
(4,109);

/*Table structure for table `person` */

DROP TABLE IF EXISTS `person`;

CREATE TABLE `person` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `last_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `gender` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `profile_photo` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `CONST_PERSON_GENDER` CHECK ((`gender` in (_utf8mb4'M',_utf8mb4'F',_utf8mb4'O')))
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `person` */

insert  into `person`(`id`,`first_name`,`last_name`,`gender`,`profile_photo`) values 
(1,'David','Lynch','M','1.jpg'),
(2,'Naomi','Watts','F','2.jpg'),
(3,'Laura','Harring','F','3.jpg'),
(4,'Justin','Theroux','M','4.jpg'),
(5,'Patrick','Fischler','M','5.jpg'),
(6,'Jeanne','Bates','F','6.jpg'),
(7,'Karolina','Gruszka','F','7.jpg'),
(8,'Krzysztof','Majchrzak','M','8.jpg'),
(9,'Grace','Zabriskie','F','9.jpg'),
(10,'Laura','Dern','F','10.jpg'),
(11,'Harry Dean','Stanton','M','11.jpg'),
(12,'Peter J.','Lucas','M','12.jpg'),
(13,'Hailee','Steinfeld','F','13.jpg'),
(14,'Pascal','Charrue','M','14.jpg'),
(15,'Arnaud','Delord','M','15.jpg'),
(16,'Mollie Bickley','St. John','F',NULL),
(17,'Ash','Brannon','M','17.jpg'),
(18,'David','Dunne','M',NULL),
(19,'Christian','Linke','M','19.jpg'),
(20,'Kevin','Alejandro','M','20.jpg'),
(21,'Jason','Spisak','M','21.jpg'),
(22,'Ella','Purnell','F','22.jpg'),
(23,'Katie','Leung','F','23.jpg'),
(24,'Harry','Lloyd','M','24.jpg'),
(25,'Mick','Wingert','M','25.jpg'),
(26,'Robert','Eggers','M','26.jpg'),
(27,'Max','Eggers','M','27.jpg'),
(28,'Robert','Pattinson','M','28.jpg'),
(29,'Willem','Dafoe','M','29.jpg'),
(30,'Valeriia','Karaman','F','30.jpg'),
(31,'Jeffrey J.','Abrams','M','31.jpg'),
(32,'Jeffrey','Lieber','M',NULL),
(33,'Damon','Lindelof','M','33.jpg'),
(34,'Carlton','Cuse','M',NULL),
(35,'Jorge','Garcia','M','35.jpg'),
(36,'Josh','Holloway','M','36.jpg'),
(37,'Yunjin','Kim','F','37.jpg'),
(38,'Evangeline','Lilly','F','38.jpg'),
(39,'Terry','O\'Quinn','M','39.jpg'),
(40,'Matthew','Fox','M','40.jpg'),
(41,'Daniel','Dae Kim','M','41.jpg'),
(42,'Naveen','Andrews','M','42.jpg'),
(43,'Emilie','de Ravin','F','43.jpg'),
(44,'Dominic','Monaghan','M',NULL),
(45,'Michael','Emerson','M','45.jpg'),
(46,'Trey','Parker','M','46.jpg'),
(47,'Matt','Stone','M','47.jpg'),
(48,'Brian','Graden','M','48.jpg'),
(49,'Isaac','Hayes','M','49.jpg'),
(50,'Mona','Marshall','F',NULL);

/*Table structure for table `tv_show` */

DROP TABLE IF EXISTS `tv_show`;

CREATE TABLE `tv_show` (
  `media_id` bigint unsigned NOT NULL,
  `number_of_seasons` int unsigned NOT NULL,
  PRIMARY KEY (`media_id`),
  CONSTRAINT `tv_show_ibfk_1` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tv_show` */

insert  into `tv_show`(`media_id`,`number_of_seasons`) values 
(3,1),
(5,6),
(6,26);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

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

/*Data for the table `user` */
insert  into `user`(`id`,`first_name`,`last_name`,`gender`,`profile_name`,`profile_image`,`username`,`email`,`password`,`role`,`created_at`,`updated_at`,`country_id`) values 
(1,'Admin','Admin','O','Admin',NULL,'admin','admin@gmail.com','$2a$10$lLpAEM3iq6LLKhfBID8YhOcd8DCLj9HYO5VeMXcIFlM0C/myvjsnW','ADMINISTRATOR',STR_TO_DATE('2024-01-25 14:49:36', '%Y-%m-%d %H:%i:%s'),STR_TO_DATE('2024-01-25 14:49:36', '%Y-%m-%d %H:%i:%s'),198),
(2,'Regular','Regular','M','Regular','Regular.jpg','regular','regular@gmail.com','$2a$10$psEbqp2bRsmgxpVQ.Wj/hOH5Czj8ce9MQ05vz9BS21xJr0A4HEIRi','REGULAR',STR_TO_DATE('2024-02-28 14:49:36', '%Y-%m-%d %H:%i:%s'),STR_TO_DATE('2024-02-28 14:49:36', '%Y-%m-%d %H:%i:%s'),15),
(3,'Critic','Critic','F','Critic','Critic.jpg','critic','critic@gmail.com','$2a$10$BigpH6HHGlSn1//5rsIqhOCJOJ/TSHxtXQc7X43VJAStjayk9Hwh6','CRITIC',STR_TO_DATE('2023-11-25 14:49:36', '%Y-%m-%d %H:%i:%s'),STR_TO_DATE('2023-11-25 14:49:36', '%Y-%m-%d %H:%i:%s'),57);


/*Table structure for table `user_media` */

DROP TABLE IF EXISTS `user_media`;

CREATE TABLE `user_media` (
  `user_id` bigint unsigned NOT NULL,
  `media_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`user_id`,`media_id`),
  KEY `media_id` (`media_id`),
  CONSTRAINT `user_media_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_media_ibfk_2` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `user_media` */

/*Table structure for table `writer` */

DROP TABLE IF EXISTS `writer`;

CREATE TABLE `writer` (
  `person_id` bigint unsigned NOT NULL,
  PRIMARY KEY (`person_id`),
  CONSTRAINT `writer_ibfk_1` FOREIGN KEY (`person_id`) REFERENCES `person` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `writer` */

insert  into `writer`(`person_id`) values 
(1),
(16),
(17),
(18),
(19),
(26),
(27),
(31),
(33),
(34),
(46),
(47);

/* Trigger structure for table `critique` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `aftcritins` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'Despot'@'localhost' */ /*!50003 TRIGGER `aftcritins` AFTER INSERT ON `critique` FOR EACH ROW UPDATE media SET critic_rating=(SELECT ROUND(AVG(rating)) FROM critique WHERE media.id = critique.media_id) WHERE id=NEW.media_id */$$


DELIMITER ;

/* Trigger structure for table `critique` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `aftcritupd` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'Despot'@'localhost' */ /*!50003 TRIGGER `aftcritupd` AFTER UPDATE ON `critique` FOR EACH ROW UPDATE media SET critic_rating=(SELECT ROUND(AVG(rating)) FROM critique WHERE media.id = critique.media_id) WHERE id=NEW.media_id */$$


DELIMITER ;

/* Trigger structure for table `critique` */

DELIMITER $$

/*!50003 DROP TRIGGER*//*!50032 IF EXISTS */ /*!50003 `aftcritdel` */$$

/*!50003 CREATE */ /*!50017 DEFINER = 'Despot'@'localhost' */ /*!50003 TRIGGER `aftcritdel` AFTER DELETE ON `critique` FOR EACH ROW UPDATE media SET critic_rating=(SELECT ROUND(AVG(rating)) FROM critique WHERE media.id = critique.media_id) WHERE id=OLD.media_id */$$


DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
