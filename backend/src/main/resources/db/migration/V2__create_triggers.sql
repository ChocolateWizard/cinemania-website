CREATE TRIGGER `aftcritins` AFTER INSERT ON `critique` FOR EACH ROW 
UPDATE media SET critic_rating=(SELECT ROUND(AVG(rating)) FROM critique WHERE media.id = critique.media_id)
WHERE id=NEW.media_id;

CREATE  TRIGGER `aftcritupd` AFTER UPDATE ON `critique` FOR EACH ROW 
UPDATE media SET critic_rating=(SELECT ROUND(AVG(rating)) FROM critique WHERE media.id = critique.media_id)
WHERE id=NEW.media_id;

CREATE  TRIGGER `aftcritdel` AFTER DELETE ON `critique` FOR EACH ROW 
UPDATE media SET critic_rating=(SELECT ROUND(AVG(rating)) FROM critique WHERE media.id = critique.media_id)
WHERE id=OLD.media_id;

