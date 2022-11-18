DROP DATABASE IF EXISTS `binotify-soap`;
CREATE DATABASE `binotify-soap`;

DROP TABLE IF EXISTS `binotify-soap`.`logging`;
CREATE TABLE `binotify-soap`.`logging` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) NOT NULL,
  `ip` varchar(16) NOT NULL,
  `endpoint` varchar(255) NOT NULL,
  `requested_at` timestamp NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `binotify-soap`.`logging` (`id`, `description`, `ip`, `endpoint`, `requested_at`) VALUES
  (1, 'first log', '127.0.0.1', 'localhost.com', '2008-01-01 00:00:01');

DROP TABLE IF EXISTS `binotify-soap`.`subscription`;
CREATE TABLE `binotify-soap`.`subscription` (
  `creator_id` int NOT NULL,
  `subscriber_id` int NOT NULL,
  `status` ENUM('PENDING','ACCEPTED','REJECTED') DEFAULT 'PENDING' NOT NULL,
  PRIMARY KEY (`creator_id`,`subscriber_id`)
);

INSERT INTO `binotify-soap`.`subscription` VALUES
('3','5',''),
('3','4','AAA'),
('1','2','ACCEPTED');