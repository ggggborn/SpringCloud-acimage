-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: acimage_community
-- ------------------------------------------------------
-- Server version	8.0.29

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
-- Table structure for table `tb_category`
--

DROP TABLE IF EXISTS `tb_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `label` varchar(50) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `label` (`label`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_category`
--

LOCK TABLES `tb_category` WRITE;
/*!40000 ALTER TABLE `tb_category` DISABLE KEYS */;
INSERT INTO `tb_category` VALUES (1,'番剧茶馆','2023-01-28 12:30:59','2023-01-28 12:30:59',0),(2,'漫画杂谈','2023-01-28 12:30:59','2023-01-28 12:30:59',0),(3,'游戏交流','2023-01-28 12:30:59','2023-01-28 12:30:59',0),(4,'新番点评','2023-01-28 12:31:39','2023-01-28 12:31:39',0),(5,'漫画推荐','2023-01-29 18:10:46','2023-01-29 18:10:46',0),(6,'美图分享','2023-01-28 12:31:56','2023-01-28 12:31:56',0),(7,'技术','2023-02-11 22:53:12','2023-02-11 22:53:12',0),(8,'生活','2023-02-12 01:03:46','2023-02-12 01:03:46',0),(9,'其它','2023-02-17 23:32:57','2023-02-17 23:32:57',0);
/*!40000 ALTER TABLE `tb_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_cmty_user`
--

DROP TABLE IF EXISTS `tb_cmty_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_cmty_user` (
  `id` bigint NOT NULL,
  `username` varchar(12) NOT NULL,
  `photo_url` varchar(60) DEFAULT NULL,
  `topic_count` int DEFAULT '0',
  `star_count` int DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_cmty_user`
--

LOCK TABLES `tb_cmty_user` WRITE;
/*!40000 ALTER TABLE `tb_cmty_user` DISABLE KEYS */;
INSERT INTO `tb_cmty_user` VALUES (1572443275490078720,'xlg','/acfile-test/userPhoto/2023/02/15/1625783460550000640.webp',20,2),(1626532367060037632,'真·站长','',1,0),(1626603310067335168,'站长','',0,0);
/*!40000 ALTER TABLE `tb_cmty_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_comment`
--

DROP TABLE IF EXISTS `tb_comment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_comment` (
  `id` bigint NOT NULL,
  `topic_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `content` varchar(200) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_comment_user_id` (`user_id`),
  KEY `fk_comment_topic_id` (`topic_id`),
  CONSTRAINT `fk_comment_topic_id` FOREIGN KEY (`topic_id`) REFERENCES `tb_topic` (`id`),
  CONSTRAINT `fk_comment_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_cmty_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_comment`
--

LOCK TABLES `tb_comment` WRITE;
/*!40000 ALTER TABLE `tb_comment` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_comment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_home_carousel`
--

DROP TABLE IF EXISTS `tb_home_carousel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_home_carousel` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(30) DEFAULT NULL,
  `url` varchar(60) NOT NULL,
  `link` varchar(100) NOT NULL,
  `size` int DEFAULT NULL,
  `location` int DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_home_carousel`
--

LOCK TABLES `tb_home_carousel` WRITE;
/*!40000 ALTER TABLE `tb_home_carousel` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_home_carousel` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_star`
--

DROP TABLE IF EXISTS `tb_star`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_star` (
  `user_id` bigint NOT NULL,
  `topic_id` bigint NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`topic_id`),
  KEY `index_star_topic_id` (`topic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_star`
--

LOCK TABLES `tb_star` WRITE;
/*!40000 ALTER TABLE `tb_star` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_star` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_tag`
--

DROP TABLE IF EXISTS `tb_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_tag` (
  `id` int NOT NULL AUTO_INCREMENT,
  `label` varchar(30) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `label` (`label`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_tag`
--

LOCK TABLES `tb_tag` WRITE;
/*!40000 ALTER TABLE `tb_tag` DISABLE KEYS */;
INSERT INTO `tb_tag` VALUES (1,'冒险','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(2,'恋爱','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(3,'剧情','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(4,'搞笑','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(5,'交流','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(6,'温馨','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(7,'原创','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(8,'热血','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(9,'科幻','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(10,'运动','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(11,'竞技','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(12,'吐槽','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(13,'治愈','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(14,'致郁','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(15,'音乐','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(16,'美食','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(17,'异世界','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(18,'日常','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(19,'校园','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(20,'资讯','2023-01-28 16:51:16','2023-01-28 16:51:16',0),(21,'新番','2023-01-28 16:54:03','2023-01-28 16:54:03',0),(22,'老番','2023-01-28 16:54:03','2023-01-28 16:54:03',0),(23,'技术','2023-02-12 01:34:03','2023-02-12 01:34:03',0),(24,'超能力','2023-02-15 21:25:50','2023-02-15 21:25:50',0),(25,'美少女','2023-02-15 21:26:05','2023-02-15 21:26:05',0);
/*!40000 ALTER TABLE `tb_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_tag_topic`
--

DROP TABLE IF EXISTS `tb_tag_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_tag_topic` (
  `id` bigint NOT NULL,
  `topic_id` bigint NOT NULL,
  `tag_id` int NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `topic_id` (`topic_id`),
  KEY `tag_id` (`tag_id`),
  CONSTRAINT `tb_tag_topic_ibfk_1` FOREIGN KEY (`topic_id`) REFERENCES `tb_topic` (`id`),
  CONSTRAINT `tb_tag_topic_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `tb_tag` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_tag_topic`
--

LOCK TABLES `tb_tag_topic` WRITE;
/*!40000 ALTER TABLE `tb_tag_topic` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_tag_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_topic`
--

DROP TABLE IF EXISTS `tb_topic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_topic` (
  `id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  `title` varchar(30) NOT NULL COMMENT '长度≥4',
  `content` varchar(600) NOT NULL,
  `cover_image_url` varchar(100) DEFAULT NULL,
  `star_count` int DEFAULT '0',
  `comment_count` int DEFAULT '0',
  `page_view` int DEFAULT '0',
  `activity_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  `category_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_topic_user_id` (`user_id`),
  KEY `fk_topic_category_id_idx` (`category_id`),
  CONSTRAINT `fk_topic_category_id` FOREIGN KEY (`category_id`) REFERENCES `tb_category` (`id`),
  CONSTRAINT `fk_topic_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_cmty_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_topic`
--

LOCK TABLES `tb_topic` WRITE;
/*!40000 ALTER TABLE `tb_topic` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_topic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_topic_html`
--

DROP TABLE IF EXISTS `tb_topic_html`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_topic_html` (
  `topic_id` bigint NOT NULL,
  `html` varchar(5000) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint DEFAULT '0',
  PRIMARY KEY (`topic_id`),
  CONSTRAINT `tb_topic_html_ibfk_1` FOREIGN KEY (`topic_id`) REFERENCES `tb_topic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_topic_html`
--

LOCK TABLES `tb_topic_html` WRITE;
/*!40000 ALTER TABLE `tb_topic_html` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_topic_html` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-17 23:34:07
