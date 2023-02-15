-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: acimage_sys
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
-- Table structure for table `tb_api`
--

DROP TABLE IF EXISTS `tb_api`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_api` (
  `id` int NOT NULL AUTO_INCREMENT,
  `path` varchar(200) NOT NULL,
  `method` varchar(20) NOT NULL,
  `permission_id` int NOT NULL,
  `enable` tinyint DEFAULT '1',
  `note` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `fk_api_permissionId` (`permission_id`),
  CONSTRAINT `fk_api_permissionId` FOREIGN KEY (`permission_id`) REFERENCES `tb_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_api`
--

LOCK TABLES `tb_api` WRITE;
/*!40000 ALTER TABLE `tb_api` DISABLE KEYS */;
INSERT INTO `tb_api` VALUES (6,'/api/community/topics/query/**','ALL',30,1,'查询话题','2023-02-06 14:15:49','2023-02-06 14:15:49',0),(7,'/api/community/topics/operate/**','ALL',35,1,'操作话题','2023-02-06 10:24:10','2023-02-06 10:24:10',0),(9,'/api/community/*/xxx','GET',36,1,NULL,'2023-02-06 09:52:13','2023-02-06 09:52:13',1),(10,'/api/community/topics/operate','POST',11,1,'发表话题','2023-02-06 10:16:05','2023-02-06 10:16:05',0),(11,'/api/community/comments/query/**','GET',29,1,'查看话题','2023-02-06 10:24:17','2023-02-06 10:24:17',0),(12,'/api/community/comments/operate/**','ALL',34,1,'操作话题','2023-02-06 15:07:01','2023-02-06 15:07:01',0),(13,'/api/community/categories/query/**','GET',42,1,'分类查询','2023-02-06 14:21:10','2023-02-06 14:21:10',0),(14,'/api/community/tags/query/**','GET',42,1,'标签查询','2023-02-06 14:20:47','2023-02-06 14:20:47',0),(15,'/api/community/stars/query/**','GET',43,1,'查询点赞','2023-02-06 14:53:29','2023-02-06 14:53:29',0),(16,'/api/community/stars/operate/**','ALL',44,1,'star操作','2023-02-06 14:57:27','2023-02-06 14:57:27',0),(17,'/api/community/topics/search/**','ALL',36,1,'搜索话题','2023-02-11 07:08:16','2023-02-11 07:08:16',0),(18,'/api/user/logins/**','ALL',47,1,'登录注册等','2023-02-09 08:58:18','2023-02-09 08:58:18',0),(19,'/api/user/verifies/**','ALL',47,1,'请求验证码','2023-02-09 09:00:41','2023-02-09 09:00:41',0),(20,'/api/image/homeCarousels/**','GET',47,1,'轮播图','2023-02-09 13:58:41','2023-02-09 13:58:41',1),(21,'/api/image/images/operate/**','ALL',35,1,'上传话题图片','2023-02-11 07:06:38','2023-02-11 07:06:38',0),(22,'/api/user/**','GET',43,1,NULL,'2023-02-09 15:14:00','2023-02-09 15:14:00',1),(23,'/api/user/users/query/**','GET',43,1,'查询用户信息','2023-02-14 06:02:16','2023-02-14 06:02:16',0),(24,'/api/user/users/operate/**','ALL',44,1,'用户操作自身信息','2023-02-09 15:24:48','2023-02-09 15:24:48',0),(25,'/api/community/users/rank/**','GET',47,1,'查询用户排名','2023-02-11 07:07:49','2023-02-11 07:07:49',0),(26,'/api/image/photos/operate/**','ALL',44,1,'头像操作','2023-02-11 07:07:37','2023-02-11 07:07:37',0),(27,'/api/admin/**','ALL',46,1,'admin操作','2023-02-11 12:22:04','2023-02-11 12:22:04',0),(28,'/api/community/topics/query/mine/**','GET',43,1,'查询用户话题','2023-02-11 13:57:07','2023-02-11 13:57:07',0),(29,'/api/community/homeCarousels/**','GET',47,1,'查询首页','2023-02-14 06:02:38','2023-02-14 06:02:38',0),(30,'/api/image/images/searchByImage','ALL',43,1,'以图搜图','2023-02-14 09:53:50','2023-02-14 09:53:50',0),(31,'/api/community/comments/query/mine/**','GET',43,1,'查询自己话题','2023-02-14 13:39:47','2023-02-14 13:39:47',0),(32,'/api/community/stars/query/mine/**','GET',43,1,'查询自己点赞','2023-02-14 13:41:42','2023-02-14 13:41:42',0),(33,'/api/admin/authorizes/operate/**','ALL',27,1,'操作权限','2023-02-15 08:49:16','2023-02-15 08:49:16',0),(34,'/api/admin/logins/**','ALL',47,1,'管理员登录','2023-02-15 08:51:42','2023-02-15 08:51:42',0);
/*!40000 ALTER TABLE `tb_api` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_authorize`
--

DROP TABLE IF EXISTS `tb_authorize`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_authorize` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL,
  `permission_id` int NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`,`permission_id`),
  KEY `fk_authorize_permission_id_idx` (`permission_id`),
  CONSTRAINT `fk_authorize_permission_id` FOREIGN KEY (`permission_id`) REFERENCES `tb_permission` (`id`),
  CONSTRAINT `tb_authorize_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=87 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_authorize`
--

LOCK TABLES `tb_authorize` WRITE;
/*!40000 ALTER TABLE `tb_authorize` DISABLE KEYS */;
INSERT INTO `tb_authorize` VALUES (2,2,14,'2023-01-16 22:31:31'),(3,1,15,'2023-01-16 22:32:06'),(10,3,18,'2023-01-16 22:37:00'),(12,3,21,'2023-01-16 22:37:04'),(13,3,22,'2023-01-16 22:37:05'),(14,3,24,'2023-01-16 22:37:07'),(15,2,29,'2023-01-16 23:56:39'),(16,2,30,'2023-01-16 23:56:46'),(17,1,29,'2023-01-16 23:58:10'),(18,1,30,'2023-01-16 23:58:12'),(19,3,31,'2023-01-16 23:58:18'),(20,3,32,'2023-01-16 23:58:21'),(21,3,33,'2023-01-16 23:58:26'),(22,3,26,'2023-01-16 23:58:28'),(23,6,18,'2023-01-16 23:58:55'),(25,6,31,'2023-01-16 23:58:58'),(26,6,21,'2023-01-16 23:58:59'),(27,6,22,'2023-01-16 23:59:01'),(28,6,32,'2023-01-16 23:59:02'),(29,6,24,'2023-01-16 23:59:04'),(30,6,33,'2023-01-16 23:59:06'),(31,6,26,'2023-01-16 23:59:08'),(34,6,27,'2023-02-03 22:01:50'),(36,6,11,'2023-02-03 22:02:28'),(40,2,42,'2023-02-05 23:54:08'),(41,2,36,'2023-02-05 23:55:04'),(42,1,35,'2023-02-05 23:55:41'),(43,1,36,'2023-02-05 23:55:42'),(44,1,42,'2023-02-05 23:55:45'),(45,1,34,'2023-02-05 23:56:02'),(47,7,11,'2023-02-06 18:14:56'),(48,1,43,'2023-02-06 22:54:10'),(49,1,44,'2023-02-06 22:55:41'),(51,2,47,'2023-02-09 16:55:38'),(52,1,47,'2023-02-09 16:55:45'),(53,3,47,'2023-02-09 16:55:52'),(54,6,47,'2023-02-09 16:55:56'),(55,7,47,'2023-02-09 16:56:01'),(56,6,46,'2023-02-11 20:22:30'),(57,7,35,'2023-02-11 20:23:07'),(58,7,36,'2023-02-11 20:23:09'),(59,7,30,'2023-02-11 20:23:11'),(60,7,34,'2023-02-11 20:23:12'),(61,7,29,'2023-02-11 20:23:14'),(63,7,43,'2023-02-11 20:24:10'),(64,7,44,'2023-02-11 20:24:11'),(65,7,15,'2023-02-11 20:24:18'),(66,3,15,'2023-02-11 20:24:36'),(67,3,29,'2023-02-11 20:24:37'),(68,3,34,'2023-02-11 20:24:38'),(69,3,11,'2023-02-11 20:24:40'),(70,3,30,'2023-02-11 20:24:43'),(71,3,35,'2023-02-11 20:24:44'),(72,3,36,'2023-02-11 20:24:47'),(73,3,42,'2023-02-11 20:24:48'),(74,3,43,'2023-02-11 20:25:01'),(75,3,44,'2023-02-11 20:25:04'),(76,6,29,'2023-02-11 20:25:21'),(77,6,34,'2023-02-11 20:25:23'),(78,6,15,'2023-02-11 20:25:25'),(79,6,30,'2023-02-11 20:25:26'),(80,6,35,'2023-02-11 20:25:28'),(81,6,36,'2023-02-11 20:25:29'),(82,6,42,'2023-02-11 20:25:31'),(83,6,43,'2023-02-11 20:25:35'),(84,6,44,'2023-02-11 20:25:37'),(86,1,11,'2023-02-14 22:28:44');
/*!40000 ALTER TABLE `tb_authorize` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_permission`
--

DROP TABLE IF EXISTS `tb_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `parent_id` int DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `note` varchar(20) DEFAULT NULL,
  `module` tinyint NOT NULL COMMENT '是权限模块还是处于叶子节点的权限',
  `label` varchar(20) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_tb_permission_parent_id_idx` (`parent_id`),
  CONSTRAINT `fk_tb_permission_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `tb_permission` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_permission`
--

LOCK TABLES `tb_permission` WRITE;
/*!40000 ALTER TABLE `tb_permission` DISABLE KEYS */;
INSERT INTO `tb_permission` VALUES (1,NULL,NULL,'用户社区权限模块',1,'社区','2023-01-16 16:16:53','2023-01-16 15:06:42'),(2,1,NULL,'',1,'用户','2023-01-16 16:36:49','2023-01-16 15:06:59'),(3,1,NULL,'',1,'评论','2023-01-16 16:37:48','2023-01-16 15:07:04'),(4,NULL,NULL,'',1,'管理','2023-01-16 16:43:22','2023-01-16 15:06:53'),(6,1,NULL,'',1,'话题','2023-01-16 16:46:07','2023-01-16 15:07:13'),(11,6,'topic:add','',0,'发表话题','2023-01-16 17:01:47','2023-01-16 15:07:47'),(14,2,'user:register','',0,'注册','2023-01-16 17:13:43','2023-01-16 15:08:17'),(15,2,'user:update','和用户隐私信息修改不同',0,'用户基本信息修改','2023-01-16 17:15:01','2023-01-16 15:08:38'),(16,4,NULL,'',1,'社区管理','2023-01-16 17:16:00','2023-01-16 15:51:03'),(17,16,NULL,'',1,'评论管理','2023-01-16 17:18:39','2023-01-16 15:51:10'),(18,17,'system:comment:operate','',0,'操作评论-管理','2023-01-16 17:19:38','2023-02-05 10:00:25'),(20,16,NULL,'',1,'话题管理','2023-01-16 17:21:21','2023-01-16 15:51:23'),(21,20,'system:topic:operate','',0,'操作话题-管理','2023-01-16 17:22:17','2023-02-05 09:50:20'),(22,20,'system:topic:delete','',0,'话题删除-管理','2023-01-16 17:23:26','2023-01-16 17:23:26'),(23,16,NULL,'',1,'用户管理','2023-01-16 17:25:22','2023-01-16 15:51:46'),(24,23,'system:user:operate','',0,'操作用户信息-管理','2023-01-16 17:26:16','2023-02-05 09:49:42'),(25,4,NULL,'',1,'权限管理','2023-01-16 18:18:56','2023-01-16 15:51:35'),(26,25,'auth:query','',0,'权限查看','2023-01-16 22:43:42','2023-01-16 22:43:42'),(27,25,'auth:operate','',0,'操作权限','2023-01-16 22:44:32','2023-02-05 10:01:43'),(29,3,'comment:query','',0,'查看评论','2023-01-16 22:47:19','2023-01-16 15:51:55'),(30,6,'topic:query','',0,'查看话题','2023-01-16 22:49:17','2023-01-16 15:57:03'),(31,17,'system:comment:query','',0,'查看评论-管理','2023-01-16 23:53:10','2023-01-16 15:53:25'),(32,20,'system:topic:query','',0,'查看话题-管理','2023-01-16 23:54:00','2023-01-16 15:54:19'),(33,23,'system:user:query','',0,'查看用户-管理','2023-01-16 23:55:02','2023-01-16 23:55:02'),(34,3,'comment:operate','',0,'操作评论','2023-02-05 17:44:46','2023-02-05 17:44:46'),(35,6,'topic:operate','',0,'操作话题','2023-02-05 17:48:34','2023-02-05 17:48:34'),(36,6,'topic:search','',0,'搜索话题','2023-02-05 18:02:27','2023-02-05 18:02:27'),(41,1,NULL,'',1,'总体','2023-02-05 23:51:30','2023-02-08 13:53:26'),(42,41,'visitor:base:query','',0,'基本查询','2023-02-05 23:51:54','2023-02-06 14:56:16'),(43,41,'user:base:query','',0,'注册用户基本查询权限','2023-02-06 22:52:44','2023-02-06 22:52:44'),(44,41,'user:base:operate','',0,'注册用户基本操作权限','2023-02-06 22:55:30','2023-02-06 22:55:30'),(45,4,NULL,'',1,'总体管理','2023-02-08 21:54:02','2023-02-08 21:54:02'),(46,45,'admin:operate','',0,'管理员权限','2023-02-08 21:54:37','2023-02-08 21:54:37'),(47,41,'none','',0,'无权限','2023-02-09 16:51:13','2023-02-09 16:51:13');
/*!40000 ALTER TABLE `tb_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_role`
--

DROP TABLE IF EXISTS `tb_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `note` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_role`
--

LOCK TABLES `tb_role` WRITE;
/*!40000 ALTER TABLE `tb_role` DISABLE KEYS */;
INSERT INTO `tb_role` VALUES (1,'user','2023-01-15 18:22:25','2023-01-21 14:15:59','社区用户'),(2,'visitor','2023-01-15 18:30:03','2023-01-15 18:30:03','游客'),(3,'admin','2023-01-15 18:30:15','2023-01-15 18:30:15','管理员'),(6,'superAdmin','2023-01-16 23:58:44','2023-01-16 23:58:44','超级管理员'),(7,'verifiedUser','2023-02-05 23:57:55','2023-02-05 23:57:55','认证用户');
/*!40000 ALTER TABLE `tb_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_user_role`
--

DROP TABLE IF EXISTS `tb_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tb_user_role` (
  `id` bigint NOT NULL,
  `user_id` mediumtext NOT NULL,
  `role_id` int NOT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `role_id` (`role_id`),
  CONSTRAINT `tb_user_role_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `tb_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_user_role`
--

LOCK TABLES `tb_user_role` WRITE;
/*!40000 ALTER TABLE `tb_user_role` DISABLE KEYS */;
INSERT INTO `tb_user_role` VALUES (1621894312265109504,'1572443275490078720',6,'2023-02-04 15:31:52');
/*!40000 ALTER TABLE `tb_user_role` ENABLE KEYS */;
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
