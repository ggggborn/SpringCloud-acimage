-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: acimage_user
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
-- Table structure for table `tb_user`
--

DROP TABLE IF EXISTS `tb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user` (
  `id` bigint NOT NULL,
  `username` varchar(12) NOT NULL,
  `photo_url` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user`
--

LOCK TABLES `tb_user` WRITE;
/*!40000 ALTER TABLE `tb_user` DISABLE KEYS */;
INSERT INTO `tb_user` VALUES (1571804585637974016,'xlg2',NULL),(1572443275490078720,'xlg','/acfile-test/userPhoto/2023/02/15/1625783460550000640.webp'),(1572517648460795904,'wyn',NULL),(1575444479153295360,'wk555','/acfile-test/userPhoto/2023/02/14/1625428004870627328.webp'),(1578720796628017152,'wyk',NULL),(1610563829040152576,'test','/acfile-test/userPhoto/2023/02/14/1625439247643779072.webp'),(1620054824748322816,'test646',NULL),(1621074047897554944,'mail','/acfile-test/userPhoto/2023/02/11/1624346161974960128.webp'),(1625487680777834496,'真站长',NULL),(1625809207310204928,'test3',NULL),(1625835011809308672,'test4',NULL);
/*!40000 ALTER TABLE `tb_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_msg`
--

DROP TABLE IF EXISTS `tb_user_msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_msg` (
  `user_id` bigint NOT NULL,
  `star_msg_count` int DEFAULT '0',
  `read_star_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `comment_msg_count` int DEFAULT '0',
  `read_comment_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `tb_user_msg_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_msg`
--

LOCK TABLES `tb_user_msg` WRITE;
/*!40000 ALTER TABLE `tb_user_msg` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_user_msg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_privacy`
--

DROP TABLE IF EXISTS `tb_user_privacy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_privacy` (
  `user_id` bigint NOT NULL,
  `pwd` char(32) NOT NULL,
  `salt` char(32) NOT NULL,
  `email` varchar(32) NOT NULL,
  `register_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `id` (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_privacy`
--

LOCK TABLES `tb_user_privacy` WRITE;
/*!40000 ALTER TABLE `tb_user_privacy` DISABLE KEYS */;
INSERT INTO `tb_user_privacy` VALUES (1571804585637974016,'5b5979fd4c02a39b34bfdf353c950c82','866dcdd58f5442bba34bb8077db02c1e','xlg2@qq.com','2022-09-19 18:13:10'),(1572443275490078720,'344183615e866894cabf12c820f6a1e8','a5bd3857d3af4b84b3260cc1641d627f','xlg@qq.com','2022-09-21 12:31:06'),(1572517648460795904,'782fb99908fac88829e7f069c1f8687a','725ae106f0c340a39c39f993b464baf7','wyn@qq.com','2022-09-21 17:26:38'),(1575444479153295360,'5a2f4894135a2e5ca86c5b20e9f565f8','ca2bdec3e3a74abd86689d9a3ef28cdb','wk@qq.com','2022-09-29 19:16:48'),(1578720796628017152,'df585735e0674ad4a7a071a36afdc61e','150b06082bcc4299b74cf0fc8eacaa4c','wyk@qq.com','2022-10-08 20:15:43'),(1610563829040152576,'686c5f3aa851ae0f6e98e123361df6e2','54709856870045449763425ceb690542','test@qq.com','2023-01-04 17:08:34'),(1620054824748322816,'d21bfc26301d89b1274455b6336c8ccd','571875f59e4e4df9bf74300ae8e91810','test2@qq.com','2023-01-30 21:42:24'),(1621074047897554944,'8ef907b7b63f77dcd7e43dba814c59c4','ce36520bf23e4f719fa50cd0b3a0be9e','mail@qq.com','2023-02-02 17:12:25'),(1625487680777834496,'9e362105ec1653ec2f5cc89b9a94f4b8','a69dab681fd2429cb48514e44be30421','123@qq.com','2023-02-14 21:30:37'),(1625809207310204928,'53a78fed176b632c5c82977ccaecbec9','8ce3a4c5334a4ae09501bd3d4fda1308','test3@qq.com','2023-02-15 18:48:15'),(1625835011809308672,'3d1d5f02b699ee7ea87ab1e1b9f698c6','99a75f2dfab748bc937d0503f2f9ddf7','1179836161@qq.com','2023-02-15 20:30:48');
/*!40000 ALTER TABLE `tb_user_privacy` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-02-15 21:18:19
