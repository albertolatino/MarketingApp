-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: localhost    Database: db_marketing
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `answer`
--

DROP TABLE IF EXISTS `answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `answer` (
  `question_id` int NOT NULL,
  `user_id` int NOT NULL DEFAULT '0',
  `answer` varchar(255) NOT NULL,
  PRIMARY KEY (`question_id`,`user_id`),
  KEY `answer_author_idx` (`user_id`),
  CONSTRAINT `answer_author` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `answer_to_question` FOREIGN KEY (`question_id`) REFERENCES `question` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `answer`
--

LOCK TABLES `answer` WRITE;
/*!40000 ALTER TABLE `answer` DISABLE KEYS */;
INSERT INTO `answer` VALUES (1,2,'yes'),(1,3,'yes'),(2,2,'yes'),(2,3,'no'),(3,2,'red'),(3,3,'blue');
/*!40000 ALTER TABLE `answer` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `update_score_on_answer` AFTER INSERT ON `answer` FOR EACH ROW update user set user.score = user.score + 1
where new.user_id = user.id */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `decrement_on_delete_answer` AFTER DELETE ON `answer` FOR EACH ROW UPDATE user
SET user.score = user.score - 1
WHERE user.id = old.user_id */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `log`
--

DROP TABLE IF EXISTS `log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log` (
  `log_id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `datetime` datetime NOT NULL,
  PRIMARY KEY (`log_id`),
  KEY `fk_access_log_1_idx` (`user_id`),
  CONSTRAINT `fk_access_log_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=242 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log`
--

LOCK TABLES `log` WRITE;
/*!40000 ALTER TABLE `log` DISABLE KEYS */;
/*!40000 ALTER TABLE `log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `offensive_word`
--

DROP TABLE IF EXISTS `offensive_word`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `offensive_word` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `word` varchar(255) DEFAULT NULL,
  `language` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `word` (`word`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2298 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `offensive_word`
--

LOCK TABLES `offensive_word` WRITE;
/*!40000 ALTER TABLE `offensive_word` DISABLE KEYS */;
INSERT INTO `offensive_word` VALUES (1,'anal','en'),(2,'anus','en'),(3,'arse','en'),(4,'ass','en'),(5,'balls','en'),(6,'ballsack','en'),(7,'bastard','en'),(8,'biatch','en'),(9,'bitch','en'),(10,'bloody','en'),(11,'blow job','en'),(12,'blowjob','en'),(13,'bollock','en'),(14,'bollok','en'),(15,'boner','en'),(16,'boob','en'),(17,'bugger','en'),(18,'bum','en'),(19,'butt','en'),(20,'buttplug','en'),(21,'clitoris','en'),(22,'cock','en'),(23,'coon','en'),(24,'crap','en'),(25,'cunt','en'),(26,'damn','en'),(27,'dick','en'),(28,'dildo','en'),(29,'dyke','en'),(30,'f u c k','en'),(31,'fag','en'),(32,'feck','en'),(33,'felching','en'),(34,'fellate','en'),(35,'fellatio','en'),(36,'flange','en'),(37,'fuck','en'),(38,'fudge packer','en'),(39,'fudgepacker','en'),(40,'God damn','en'),(41,'Goddamn','en'),(42,'hell','en'),(43,'homo','en'),(44,'jerk','en'),(45,'jizz','en'),(46,'knob end','en'),(47,'knobend','en'),(48,'labia','en'),(49,'lmao','en'),(50,'lmfao','en'),(51,'muff','en'),(52,'nigga','en'),(53,'nigger','en'),(54,'omg','en'),(55,'penis','en'),(56,'piss','en'),(57,'poop','en'),(58,'prick','en'),(59,'pube','en'),(60,'pussy','en'),(61,'queer','en'),(62,'s hit','en'),(63,'scrotum','en'),(64,'sex','en'),(65,'sh1t','en'),(66,'shit','en'),(67,'slut','en'),(68,'smegma','en'),(69,'spunk','en'),(70,'tit','en'),(71,'tosser','en'),(72,'turd','en'),(73,'twat','en'),(74,'vagina','en'),(75,'wank','en'),(76,'whore','en'),(77,'wtf','en'),(864,'allupato','it'),(865,'ammucchiata','it'),(866,'anale','it'),(867,'arrapato','it'),(868,'arrusa','it'),(869,'arruso','it'),(870,'assatanato','it'),(871,'bagascia','it'),(872,'bagassa','it'),(873,'bagnarsi','it'),(874,'baldracca','it'),(875,'balle','it'),(876,'battere','it'),(877,'battona','it'),(878,'belino','it'),(879,'biga','it'),(880,'bocchinara','it'),(881,'bocchino','it'),(882,'bofilo','it'),(883,'boiata','it'),(884,'bordello','it'),(885,'brinca','it'),(886,'bucaiolo','it'),(887,'budiÔö£Ôòúlo','it'),(888,'buona donna','it'),(889,'busone','it'),(890,'cacca','it'),(891,'caccati in mano e prenditi a schiaffi','it'),(892,'caciocappella','it'),(893,'cadavere','it'),(894,'cagare','it'),(895,'cagata','it'),(896,'cagna','it'),(897,'cammello','it'),(898,'cappella','it'),(899,'carciofo','it'),(900,'caritÔö£├í','it'),(901,'casci','it'),(902,'cazzata','it'),(903,'cazzimma','it'),(904,'cazzo','it'),(905,'checca','it'),(906,'chiappa','it'),(907,'chiavare','it'),(908,'chiavata','it'),(909,'ciospo','it'),(910,'ciucciami il cazzo','it'),(911,'coglione','it'),(912,'coglioni','it'),(913,'cornuto','it'),(914,'cozza','it'),(915,'culattina','it'),(916,'culattone','it'),(917,'di merda','it'),(918,'ditalino','it'),(919,'duro','it'),(920,'fare unaÔö╝├í','it'),(921,'fava','it'),(922,'femminuccia','it'),(923,'fica','it'),(924,'figa','it'),(925,'figlio di buona donna','it'),(926,'figlio di puttana','it'),(927,'figone','it'),(928,'finocchio','it'),(929,'fottere','it'),(930,'fottersi','it'),(931,'fracicone','it'),(932,'fregna','it'),(933,'frocio','it'),(934,'froscio','it'),(935,'fuori come un balcone','it'),(936,'goldone','it'),(937,'grilletto','it'),(938,'guanto','it'),(939,'guardone','it'),(940,'incazzarsi','it'),(941,'incoglionirsi','it'),(942,'ingoio','it'),(943,'l\'arte bolognese','it'),(944,'leccaculo','it'),(945,'lecchino','it'),(946,'lofare','it'),(947,'loffa','it'),(948,'loffare','it'),(949,'lumaca','it'),(950,'manico','it'),(951,'mannaggia','it'),(952,'merda','it'),(953,'merdata','it'),(954,'merdoso','it'),(955,'mignotta','it'),(956,'minchia','it'),(957,'minchione','it'),(958,'mona','it'),(959,'monta','it'),(960,'montare','it'),(961,'mussa','it'),(962,'nave scuola','it'),(963,'nerchia','it'),(964,'nudo','it'),(965,'padulo','it'),(966,'palle','it'),(967,'palloso','it'),(968,'patacca','it'),(969,'patonza','it'),(970,'pecorina','it'),(971,'pesce','it'),(972,'picio','it'),(973,'pincare','it'),(974,'pipa','it'),(975,'pippone','it'),(976,'pirla','it'),(977,'pisciare','it'),(978,'piscio','it'),(979,'pisello','it'),(980,'pistola','it'),(981,'pistolotto','it'),(982,'pomiciare','it'),(983,'pompa','it'),(984,'pompino','it'),(985,'porca','it'),(986,'porca madonna','it'),(987,'porca miseria','it'),(988,'porca puttana','it'),(989,'porco due','it'),(990,'porco zio','it'),(991,'potta','it'),(992,'puppami','it'),(993,'puttana','it'),(994,'quaglia','it'),(995,'recchione','it'),(996,'regina','it'),(997,'rincoglionire','it'),(998,'rizzarsi','it'),(999,'rompiballe','it'),(1000,'ruffiano','it'),(1001,'sbattere','it'),(1002,'sbattersi','it'),(1003,'sborra','it'),(1004,'sborrata','it'),(1005,'sborrone','it'),(1006,'sbrodolata','it'),(1007,'scopare','it'),(1008,'scopata','it'),(1009,'scorreggiare','it'),(1010,'sega','it'),(1011,'slinguare','it'),(1012,'slinguata','it'),(1013,'smandrappata','it'),(1014,'soccia','it'),(1015,'socmel','it'),(1016,'sorca','it'),(1017,'spagnola','it'),(1018,'spompinare','it'),(1019,'sticchio','it'),(1020,'stronza','it'),(1021,'stronzata','it'),(1022,'stronzo','it'),(1023,'succhiami','it'),(1024,'sveltina','it'),(1025,'sverginare','it'),(1026,'tarzanello','it'),(1027,'terrone','it'),(1028,'testa di cazzo','it'),(1029,'tette','it'),(1030,'tirare','it'),(1031,'topa','it'),(1032,'troia','it'),(1033,'trombare','it'),(1034,'uccello','it'),(1035,'vacca','it'),(1036,'vaffanculo','it'),(1037,'vangare','it'),(1038,'venire','it'),(1039,'zinne','it'),(1040,'zio cantante','it'),(1041,'zoccola','it');
/*!40000 ALTER TABLE `offensive_word` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `text` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `owner_questionnaire` (`date`),
  CONSTRAINT `owner_questionnaire` FOREIGN KEY (`date`) REFERENCES `questionnaire` (`date`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES (1,'2021-01-10','Is it good for hiking?'),(2,'2021-01-20','Does it last enough?'),(3,'2021-01-20','What color is it?');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `questionnaire`
--

DROP TABLE IF EXISTS `questionnaire`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `questionnaire` (
  `date` date NOT NULL,
  `title` varchar(125) NOT NULL,
  `image` longblob,
  PRIMARY KEY (`date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `questionnaire`
--

LOCK TABLES `questionnaire` WRITE;
/*!40000 ALTER TABLE `questionnaire` DISABLE KEYS */;
INSERT INTO `questionnaire` VALUES ('2021-01-10','PoliMi Backpack',NULL),('2021-01-20','Penna PoliMi',NULL);
/*!40000 ALTER TABLE `questionnaire` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `questionnaire_cleanup` BEFORE DELETE ON `questionnaire` FOR EACH ROW BEGIN
	DELETE FROM answer WHERE answer.question_id in (SELECT question.id FROM question WHERE question.date = old.date);
	DELETE FROM stat_answer WHERE stat_answer.date = old.date;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `id` int NOT NULL AUTO_INCREMENT,
  `date` date NOT NULL,
  `review` varchar(256) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `date_idx` (`date`),
  CONSTRAINT `date` FOREIGN KEY (`date`) REFERENCES `questionnaire` (`date`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (1,'2021-01-10','Good value overall'),(2,'2021-01-20','It broke immediately!!!'),(3,'2021-01-20','Good for taking exams');
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stat_answer`
--

DROP TABLE IF EXISTS `stat_answer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `stat_answer` (
  `date` date NOT NULL,
  `user_id` int NOT NULL DEFAULT '0',
  `age` int DEFAULT NULL,
  `sex` enum('M','F','N') DEFAULT NULL,
  `expertise` enum('H','M','L') DEFAULT NULL,
  PRIMARY KEY (`date`,`user_id`),
  KEY `stat_answer_author` (`user_id`),
  CONSTRAINT `stat_answer_author` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `stat_answer_to_question` FOREIGN KEY (`date`) REFERENCES `questionnaire` (`date`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stat_answer`
--

LOCK TABLES `stat_answer` WRITE;
/*!40000 ALTER TABLE `stat_answer` DISABLE KEYS */;
INSERT INTO `stat_answer` VALUES ('2021-01-10',2,NULL,NULL,NULL),('2021-01-10',3,22,NULL,'H'),('2021-01-20',2,NULL,NULL,NULL),('2021-01-20',3,22,NULL,'M');
/*!40000 ALTER TABLE `stat_answer` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `update_score_on_statistics` AFTER INSERT ON `stat_answer` FOR EACH ROW update user
set user.score = (user.score +
	2*(IF((select age from stat_answer where stat_answer.user_id = new.user_id and stat_answer.date = new.date) is not null, 1, 0))
	+
	2*(IF((select sex from stat_answer where stat_answer.user_id = new.user_id and stat_answer.date = new.date) is not null, 1, 0))
	+
	2*(IF((select expertise from stat_answer where stat_answer.user_id = new.user_id and stat_answer.date = new.date) is not null, 1, 0)))
where user.id = new.user_id */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `decrement_on_delete_stat_answer` AFTER DELETE ON `stat_answer` FOR EACH ROW UPDATE user
SET user.score = user.score - (
	2*(IF((SELECT age FROM stat_answer WHERE stat_answer.user_id = old.user_id AND stat_answer.date = old.date) IS NOT NULL, 1, 0))
	+
	2*(IF((SELECT sex FROM stat_answer WHERE stat_answer.user_id = old.user_id AND stat_answer.date = old.date) IS NOT NULL, 1, 0))
	+
	2*(IF((SELECT expertise FROM stat_answer WHERE stat_answer.user_id = old.user_id AND stat_answer.date = old.date) IS NOT NULL, 1, 0)))
WHERE user.id = old.user_id */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `email` varchar(64) NOT NULL,
  `password` varchar(45) NOT NULL,
  `name` varchar(45) NOT NULL,
  `surname` varchar(45) NOT NULL,
  `score` int NOT NULL,
  `is_blocked` tinyint DEFAULT NULL,
  `is_admin` tinyint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'ferruccio','ferruccio@polimi.it','password','Ferruccio','Resta',0,0,1),(2,'luigi','luigi@mail.polimi.it','password','Luigi','Fusco',3,0,0),(3,'francesco','francesco@mail.polimi.it','password','Francesco','Gonzales',11,0,0),(4,'alberto','alberto@mail.polimi.it','password','Alberto','Latino',0,1,0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_questionnaire_submitted`
--

DROP TABLE IF EXISTS `user_questionnaire_submitted`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_questionnaire_submitted` (
  `user_id` int NOT NULL,
  `date` date NOT NULL,
  `is_submitted` tinyint(1) NOT NULL,
  PRIMARY KEY (`user_id`,`date`),
  KEY `questionnaire_submitted` (`date`),
  CONSTRAINT `questionnaire_submitted` FOREIGN KEY (`date`) REFERENCES `questionnaire` (`date`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `user_submitted` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_questionnaire_submitted`
--

LOCK TABLES `user_questionnaire_submitted` WRITE;
/*!40000 ALTER TABLE `user_questionnaire_submitted` DISABLE KEYS */;
INSERT INTO `user_questionnaire_submitted` VALUES (2,'2021-01-10',1),(2,'2021-01-20',1),(3,'2021-01-10',1),(3,'2021-01-20',1);
/*!40000 ALTER TABLE `user_questionnaire_submitted` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-03 15:42:58
