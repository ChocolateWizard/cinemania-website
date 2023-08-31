
/*Table structure for table `country` */

CREATE TABLE country (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  name varchar(300) NOT NULL,
  official_state_name varchar(300) NOT NULL,
  code varchar(2) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY country_code_unique (code)
) AUTO_INCREMENT=1;

/*Table structure for table `genre` */

CREATE TABLE genre (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  name varchar(300) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY genre_name_unique (name)
) AUTO_INCREMENT=1;

/*Table structure for table `media` */

CREATE TABLE media (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  title varchar(300) NOT NULL,
  release_date date NOT NULL,
  cover_image varchar(500) DEFAULT NULL,
  description varchar(500) NOT NULL,
  audience_rating int NOT NULL,
  critic_rating int DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT rating_domain CHECK (((audience_rating >= 0) and (audience_rating <= 100)))
) AUTO_INCREMENT=1;

/*Table structure for table `movie` */

CREATE TABLE movie (
  media_id bigint unsigned NOT NULL,
  length int NOT NULL,
  PRIMARY KEY (media_id),
  CONSTRAINT movie_ibfk_1 FOREIGN KEY (media_id) REFERENCES media (id) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `tv_show` */

CREATE TABLE tv_show (
  media_id bigint unsigned NOT NULL,
  number_of_seasons int NOT NULL,
  PRIMARY KEY (media_id),
  CONSTRAINT tv_show_ibfk_1 FOREIGN KEY (media_id) REFERENCES media (id) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `person` */

CREATE TABLE person (
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  first_name varchar(100) NOT NULL,
  last_name varchar(100) NOT NULL,
  gender char(1) NOT NULL,
  profile_photo varchar(500) DEFAULT NULL,
  PRIMARY KEY (id),
  CONSTRAINT const_person_gender CHECK ((gender in ('M','F','O')))
) AUTO_INCREMENT=1;

/*Table structure for table `director` */

CREATE TABLE director (
  person_id bigint unsigned NOT NULL,
  PRIMARY KEY (person_id),
  CONSTRAINT director_ibfk_1 FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `writer` */

CREATE TABLE writer (
  person_id bigint unsigned NOT NULL,
  PRIMARY KEY (person_id),
  CONSTRAINT writer_ibfk_1 FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `actor` */

CREATE TABLE actor (
  person_id bigint unsigned NOT NULL,
  is_star tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (person_id),
  CONSTRAINT actor_ibfk_1 FOREIGN KEY (person_id) REFERENCES person (id) ON DELETE CASCADE ON UPDATE CASCADE
);



/*Table structure for table `acting` */


CREATE TABLE acting (
  media_id bigint unsigned NOT NULL,
  actor_id bigint unsigned NOT NULL,
  is_starring tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (media_id,actor_id),
  KEY person_id (actor_id),
  CONSTRAINT acting_ibfk_1 FOREIGN KEY (media_id) REFERENCES media (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT acting_ibfk_2 FOREIGN KEY (actor_id) REFERENCES actor (person_id) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `acting_role` */

CREATE TABLE acting_role (
  acting_media_id bigint unsigned NOT NULL,
  acting_actor_id bigint unsigned NOT NULL,
  id bigint unsigned NOT NULL,
  name varchar(300) NOT NULL,
  PRIMARY KEY (acting_media_id,acting_actor_id,id),
  KEY order_num (id),
  KEY media_actors_media_id (acting_actor_id),
  CONSTRAINT acting_role_ibfk_1 FOREIGN KEY (acting_media_id) REFERENCES acting (media_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT acting_role_ibfk_2 FOREIGN KEY (acting_actor_id) REFERENCES acting (actor_id) ON DELETE CASCADE ON UPDATE CASCADE
);


/*Table structure for table `media_directors` */

CREATE TABLE media_directors (
  media_id bigint unsigned NOT NULL,
  director_id bigint unsigned NOT NULL,
  PRIMARY KEY (media_id,director_id),
  KEY media_directors_id (media_id,director_id),
  CONSTRAINT media_directors_ibfk_1 FOREIGN KEY (media_id) REFERENCES media (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT media_directors_ibfk_2 FOREIGN KEY (director_id) REFERENCES director (person_id) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `media_genres` */

CREATE TABLE media_genres (
  media_id bigint unsigned NOT NULL,
  genre_id bigint unsigned NOT NULL,
  PRIMARY KEY (media_id,genre_id),
  KEY media_genres_id (media_id,genre_id),
  CONSTRAINT media_genres_ibfk_1 FOREIGN KEY (media_id) REFERENCES media (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT media_genres_ibfk_2 FOREIGN KEY (genre_id) REFERENCES genre (id) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `media_writers` */

CREATE TABLE media_writers (
  media_id bigint unsigned NOT NULL,
  writer_id bigint unsigned NOT NULL,
  PRIMARY KEY (media_id,writer_id),
  KEY media_writers_id (media_id,writer_id),
  CONSTRAINT media_writers_ibfk_1 FOREIGN KEY (media_id) REFERENCES media (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT media_writers_ibfk_2 FOREIGN KEY (writer_id) REFERENCES writer (person_id) ON DELETE CASCADE ON UPDATE CASCADE
);




/*Table structure for table `user` */

CREATE TABLE "user"(
  id bigint unsigned NOT NULL AUTO_INCREMENT,
  first_name varchar(100) NOT NULL,
  last_name varchar(100) NOT NULL,
  gender char(1) NOT NULL,
  profile_image varchar(500) NOT NULL DEFAULT 'Default_profile_photo.png',
  username varchar(300) NOT NULL,
  email varchar(300) NOT NULL,
  password varchar(300) NOT NULL,
  country_id bigint unsigned NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY user_username_unique (username),
  UNIQUE KEY user_email_unique (email),
  KEY UX_users_country_id_isfrom_countries_id (country_id),
  CONSTRAINT FK_users_country_id_isfrom_countries_id FOREIGN KEY (country_id) REFERENCES country (id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT const_user_gender CHECK ((gender in ('M','F','O')))
) AUTO_INCREMENT=1;

/*Table structure for table `user_regular` */

CREATE TABLE user_regular (
  user_id bigint unsigned NOT NULL,
  PRIMARY KEY (user_id),
  CONSTRAINT user_regular_ibfk_1 FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE CASCADE
);

/*Table structure for table `user_critic` */

CREATE TABLE user_critic (
  user_id bigint unsigned NOT NULL,
  PRIMARY KEY (user_id),
  CONSTRAINT user_critic_ibfk_1 FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE CASCADE
);


/*Table structure for table `user_media` */

CREATE TABLE user_media (
  user_id bigint unsigned NOT NULL,
  media_id bigint unsigned NOT NULL,
  PRIMARY KEY (user_id,media_id),
  KEY user_media_id (user_id,media_id),
  CONSTRAINT user_media_ibfk_1 FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT user_media_ibfk_2 FOREIGN KEY (media_id) REFERENCES media (id) ON DELETE CASCADE ON UPDATE CASCADE
);


/*Table structure for table `critique` */

CREATE TABLE critique (
  user_critic_id bigint unsigned NOT NULL,
  media_id bigint unsigned NOT NULL,
  description varchar(500) NOT NULL,
  rating int NOT NULL,
  PRIMARY KEY (user_critic_id,media_id),
  KEY critique_id (user_critic_id,media_id),
  CONSTRAINT critique_ibfk_1 FOREIGN KEY (user_critic_id) REFERENCES user_critic (user_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT critique_ibfk_2 FOREIGN KEY (media_id) REFERENCES media (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT CRITIQUE_RATING_DOMAIN CHECK (((rating >= 0) and (rating <= 100)))
);


/* Trigger structure for table `critique` */

CREATE TRIGGER aftcritins AFTER INSERT ON critique FOR EACH ROW CALL "com.borak.kinweb.backend.h2.triggers.AftCritInsTrigger";

CREATE TRIGGER aftcritupd AFTER UPDATE ON critique FOR EACH ROW CALL "com.borak.kinweb.backend.h2.triggers.AftCritUpdTrigger";

CREATE TRIGGER aftcritdel AFTER DELETE ON critique FOR EACH ROW CALL "com.borak.kinweb.backend.h2.triggers.AftCritDelTrigger";

-- CREATE TRIGGER aftcritins AFTER INSERT ON critique FOR EACH ROW 
-- UPDATE media set critic_rating=(SELECT ROUND(AVG(rating)) FROM critique WHERE media.id = critique.media_id)
-- WHERE id=NEW.media_id;

-- CREATE TRIGGER aftcritupd AFTER UPDATE ON critique FOR EACH ROW 
-- UPDATE media SET critic_rating=(SELECT ROUND(AVG(rating)) FROM critique WHERE media.id = critique.media_id)
-- WHERE id=NEW.media_id;

-- CREATE TRIGGER aftcritdel AFTER DELETE ON critique FOR EACH ROW 
-- UPDATE media SET critic_rating=(SELECT ROUND(AVG(rating)) FROM critique WHERE media.id = critique.media_id)
-- WHERE id=OLD.media_id;

