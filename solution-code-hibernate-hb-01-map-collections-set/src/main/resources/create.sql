CREATE TABLE IF NOT EXISTS `student`
(
    `id`         int(11) NOT NULL AUTO_INCREMENT,
    `first_name` varchar(45) DEFAULT NULL,
    `last_name`  varchar(45) DEFAULT NULL,
    `email`      varchar(45) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 101
  DEFAULT CHARSET = latin1;



CREATE TABLE IF NOT EXISTS `image`
(
    `student_id` int(11) NOT NULL,
    `file_name`  varchar(45) DEFAULT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = latin1;