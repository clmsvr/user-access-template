
-- -----------------------------------------------------
-- Table User
-- -----------------------------------------------------
CREATE TABLE User (
  id BIGINT NOT NULL AUTO_INCREMENT,
  email VARCHAR(100) NOT NULL COMMENT 'Será usado como user_name  para acesso ao sistema',
  pwd VARCHAR(200) NOT NULL,
  name VARCHAR(100) NOT NULL,
  city VARCHAR(100) NULL,
  state CHAR(2) NULL,
  num_blocks_subtitled INT NULL DEFAULT 0,
  num_blocks_translated INT NULL DEFAULT 0,
  comment TEXT NULL COMMENT 'descricao do proprio usuario.',
  sys_creation_date DATETIME NULL,
  sys_update_date DATETIME NULL,
  PRIMARY KEY (id),
  UNIQUE INDEX email_UNIQUE (email ASC) )
ENGINE = InnoDB default character set = utf8mb4;


-- -----------------------------------------------------
-- Table role
-- -----------------------------------------------------
CREATE TABLE role (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  description varchar(255),
  PRIMARY KEY (id))
ENGINE = InnoDB default character set = utf8mb4;


-- -----------------------------------------------------
-- Table User_Temp
-- -----------------------------------------------------
CREATE TABLE User_Temp (
  token VARCHAR(200) NOT NULL,
  email VARCHAR(100) NOT NULL COMMENT 'Será usado como user_name  para acesso ao sistema',
  pwd VARCHAR(200) NOT NULL,
  name VARCHAR(100) NULL,
  acessos INT(2) NOT NULL DEFAULT 0,
  email_sent BIT(1) NOT NULL DEFAULT 0,
  sys_creation_date DATETIME NOT NULL,
  PRIMARY KEY (token),
  INDEX user_temp_email (email ASC) )
ENGINE = InnoDB default character set = utf8mb4;


-- -----------------------------------------------------
-- Table Reset_Token
-- -----------------------------------------------------
CREATE TABLE Reset_Token (
  token VARCHAR(200) NOT NULL,
  email VARCHAR(100) NOT NULL COMMENT 'Será usado como user_name  para acesso ao sistema',
  sys_creation_date DATETIME NOT NULL,
  PRIMARY KEY (token),
  INDEX user_temp_email (email ASC) )
ENGINE = InnoDB default character set = utf8mb4;


-- -----------------------------------------------------
-- Table User_has_role
-- -----------------------------------------------------
CREATE TABLE User_has_role (
  User_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (User_id, role_id),
  INDEX fk_User_has_role_role1_idx (role_id ASC) ,
  INDEX fk_User_has_role_User1_idx (User_id ASC) ,
  CONSTRAINT fk_User_has_role_User1
    FOREIGN KEY (User_id)
    REFERENCES User (id)
  ,
  CONSTRAINT fk_User_has_role_role1
    FOREIGN KEY (role_id)
    REFERENCES role (id)
  )
ENGINE = InnoDB default character set = utf8mb4;


-- -----------------------------------------------------
-- Table Permission
-- -----------------------------------------------------
CREATE TABLE Permission (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(20) NOT NULL,
  description varchar(255),
  PRIMARY KEY (id))
ENGINE = InnoDB default character set = utf8mb4;


-- -----------------------------------------------------
-- Table role_has_Permission
-- -----------------------------------------------------
CREATE TABLE role_has_Permission (
  role_id BIGINT NOT NULL,
  Permission_id BIGINT NOT NULL,
  PRIMARY KEY (role_id, Permission_id),
  INDEX fk_role_has_Permission_Permission1_idx (Permission_id ASC) ,
  INDEX fk_role_has_Permission_role1_idx (role_id ASC) ,
  CONSTRAINT fk_role_has_Permission_role1
    FOREIGN KEY (role_id)
    REFERENCES role (id)
  ,
  CONSTRAINT fk_role_has_Permission_Permission1
    FOREIGN KEY (Permission_id)
    REFERENCES Permission (id)
  )
ENGINE = InnoDB default character set = utf8mb4;

