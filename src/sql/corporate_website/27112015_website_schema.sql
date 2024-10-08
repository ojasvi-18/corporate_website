create database corporate_website CHARACTER SET utf8 COLLATE utf8_bin;
use corporate_website;

CREATE TABLE newsletter_subscription (
  email varchar(50) COLLATE utf8_bin NOT NULL PRIMARY KEY,
  client_ip char(15) COLLATE utf8_bin DEFAULT NULL,
  gen_ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE contact_request (
  email varchar(50) NOT NULL,
  name  varchar(50) NOT NULL,
  phone varchar(20) NOT NULL,
  message text NOT NULL,
  querytype INT NOT NULL,
  client_ip char(15) DEFAULT NULL,
  gen_ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  ack tinyint(1) NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- alter table contact_request add column ack tinyint(1) null default 0;
-- alter table contact_request drop PRIMARY KEY;


CREATE TABLE popup_request (
  content text COLLATE utf8_bin NOT NULL,
  client_ip char(15) COLLATE utf8_bin DEFAULT NULL,
  start_time timestamp NULL DEFAULT NULL,
  end_time timestamp NULL DEFAULT NULL,
  is_active char(1) default 'N',
  is_approved char(1) default 'N',
  gen_ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


-- TABLE STRUTURE FRO USER_PROFILE
--
DROP TABLE IF EXISTS user_profile;
CREATE TABLE user_profile (
  profile_id int(10) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT,
  name varchar(100) COLLATE utf8_bin,
  country varchar(100) COLLATE utf8_bin DEFAULT NULL,
  dob varchar(15) COLLATE utf8_bin DEFAULT NULL,
  gender char(1) COLLATE utf8_bin DEFAULT NULL,
  pin varchar(15) COLLATE utf8_bin DEFAULT NULL,
  bloodgroup varchar(4) COLLATE utf8_bin DEFAULT NULL,
  contacts varchar(100) COLLATE utf8_bin DEFAULT NULL,
  passportNo varchar(25) COLLATE utf8_bin DEFAULT NULL,
  designation varchar(200),
  state varchar(50) COLLATE utf8_bin DEFAULT NULL,
  HNoStreet varchar(50) COLLATE utf8_bin DEFAULT NULL,
  picture varchar(50) COLLATE utf8_bin DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--  alter table user_profile add column designation varchar(200) ;
--  alter table user_profile add column picture varchar(50) COLLATE utf8_bin DEFAULT NULL;

--INSERT INTO `user_profile` VALUES (1,'System Admin','India','01-01-1950','M','','','P$|F$|O$','','','','', null);
-- user_profile should be 1 right now.
--INSERT INTO `website_users` VALUES (0,'BP64+ytMnpATi/M3IFXATr7EgO4Aw4zvUlquS52dIbwkFxNYoT4zvHrhdvNxpvMBjNHErpNMrAdvWjyUmbj+UA==','S','admin@zillious.com',NOW(),0,1,'E');

DROP TABLE IF EXISTS website_users;
CREATE TABLE website_users (
  user_id int(10) unsigned NOT NULL primary key,
  auth_token varchar(150) COLLATE utf8_bin DEFAULT NULL,
  role char(2) COLLATE utf8_bin NOT NULL,
  email varchar(100) CHARACTER SET utf8 NOT NULL,
  gen_ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  device_id int(10) unsigned not null,
  profile_id int(10) unsigned NULL,
  status char(1) COLLATE utf8_bin NOT NULL DEFAULT 'N',
  FOREIGN KEY (profile_id) REFERENCES user_profile(profile_id),
  UNIQUE KEY email (email),
  UNIQUE KEY device_id (device_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- alter table website_users add column device_id varchar(15) not null;
-- alter table website_users drop column is_enabled;
-- alter table website_users add column status char(1) COLLATE utf8_bin NOT NULL default 'D';
-- alter table website_users add constraint unique key device_id (device_id);
-- Dumping data for table website_users
--


-- INSERT INTO website_users VALUES (0,'Y','mHOfTKbTWjmOgq9Hg8gOcdAVIVX4abiq2rc+TKD0Bo8fgkWy4gKGitTHnaQ9Ev6zR3slqT0aJQOFOVIwLtZ/gQ==','A','admin@zillious.com','2016-11-21 07:57:42');
-- alter table website_users drop column name;
-- alter table website_users add column profile_id int(10) unsigned NULL;
-- ALTER TABLE website_users ADD CONSTRAINT fk_profile_id FOREIGN KEY (profile_id) REFERENCES user_profile(profile_id);


--
-- Table structure for table attendance_records
--

DROP TABLE IF EXISTS attendance_records;
CREATE TABLE attendance_records (
  dev_user_id int(10) unsigned NOT NULL,
  gen_ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  device_id char(2) COLLATE utf8_bin NOT NULL DEFAULT '',
  ipaddress varchar(15) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (dev_user_id,gen_ts,device_id),
  CONSTRAINT `fk_user_device` FOREIGN KEY (`dev_user_id`) REFERENCES `website_users` (`device_id`),
  KEY attn_rcrds (dev_user_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- alter table attendance_records add primary key (user_id, gen_ts, device_id);
-- alter table attendance_records drop index user_id;
-- ALTER TABLE attendance_records ADD CONSTRAINT fk_user_device FOREIGN KEY (user_id) REFERENCES website_users(device_id);
-- alter table attendance_records change user_id dev_user_id int(10) unsigned NOT NULL; 

--
-- Table structure for table website_users
--

-- Table structure for table user_mapper
--

DROP TABLE IF EXISTS user_mapper;
CREATE TABLE user_mapper (
  user_id int(10) unsigned NOT NULL,
  device_user int(10) unsigned NOT NULL,
  UNIQUE KEY constr_ID (user_id),
  KEY attn_rcrds (user_id) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Dumping data for table user_mapper
--

-- INSERT INTO user_mapper VALUES (1,1);


--
-- TABLE STRUCTURE FOR TEAM
--
Drop table if exists team;
CREATE TABLE team (
  team_id int(11) not null auto_increment primary key,
  EmailGroups varchar(100),
  TeamName varchar(100) not null,
  Supervisor_id int(11) unsigned not null,
  FOREIGN KEY (Supervisor_id) REFERENCES website_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

drop table if exists user_team;
CREATE TABLE user_team (
  user_id int(10) unsigned not null,
  team_id int(11) not null,
  PRIMARY KEY (user_id, team_id),
  CONSTRAINT UT_U_FK FOREIGN KEY (user_id) REFERENCES website_users(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT UT_T_FK FOREIGN KEY (team_id) REFERENCES team(team_id) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

Drop table if exists year_calendar;
CREATE TABLE year_calendar(
  id          int(10) NOT NULL  PRIMARY KEY AUTO_INCREMENT,
  name        varchar(255) NOT NULL,
  event_type  char(1) NOT NULL,
  start_date  Date NOT NULL,
  end_date    Date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

Drop table if exists leave_policy;
CREATE TABLE leave_policy(
	id  	int(10) NOT NULL  PRIMARY KEY AUTO_INCREMENT,
	name  	varchar(255) NOT NULL UNIQUE,
	days    int(10) NOT NULL 
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;


--Table structure for leaves request
Drop table if exists leave_requests;
CREATE TABLE leave_requests(
id          int(10) NOT NULL PRIMARY KEY AUTO_INCREMENT,
leave_type  int(10) NOT NULL,
employee_id int(10) unsigned NOT NULL,
start_date  Date NOT NULL,
end_date    Date NOT NULL,
reason      varchar(500) NOT NULL,
status      char(1) NOT NULL,
comments    varchar(500) NULL,
changed_by  int(10) unsigned NULL,
CONSTRAINT LEAVE_TYPE_FOREIGN_KEY FOREIGN KEY (leave_type) REFERENCES leave_policy(id),
CONSTRAINT LEAVE_USER_FOREIGN_KEY FOREIGN KEY (employee_id) REFERENCES website_users(user_id),
CONSTRAINT LEAVE_STATUS_CHANGER_FOREIGN_KEY FOREIGN KEY (changed_by) REFERENCES website_users(user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- alter table leave_requests add column comments    varchar(500) NULL; 

--
-- App Bootstrap Post Load
--
--

CREATE TABLE app_bootstrap_post_load (
  post_load_id        int(20) unsigned NOT NULL PRIMARY KEY AUTO_INCREMENT,
  gen_ts              timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  client_ip           varchar(60) COLLATE utf8_bin DEFAULT NULL,
  app_logs            TEXT DEFAULT NULL,
  trace_logs          TEXT DEFAULT NULL,
  
  KEY an_pl_cip (client_ip) USING BTREE,
  KEY an_pl_gen_ts (gen_ts) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
