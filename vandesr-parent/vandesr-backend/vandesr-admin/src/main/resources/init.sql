drop table if exists vandesr_user;
create table vandesr_user(
  id int primary key auto_increment ,
  email varchar (32) not null ,
  login_name varchar (32) not null,
  user_code varchar(32) not null comment '',
  user_name varchar(16) not null comment '',
  pwd varchar(64) not null ,
  delete_flag tinyint default 0,
  avatar varchar (64) default '',
  create_date datetime(3) not null ,
  update_date datetime(3) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists vandesr_role;
create table vandesr_role(
  id int primary key auto_increment,
  role_code varchar(32) not null ,
  role_name varchar (32) not null ,
  delete_flag tinyint default 0,
  create_date datetime(3) not null ,
  create_user_code varchar(32) not null ,
  create_user_name varchar (16) not null ,
  update_date datetime(3) not null,
  update_user_code varchar(32) not null ,
  update_user_name varchar (16) not null
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists vandesr_user_role;

create table vandesr_user_role (
  id int primary key auto_increment,
  role_id int not null ,
  user_id int not null ,
  role_code varchar(32) not null ,
  role_name varchar (32) not null ,
  delete_flag tinyint default 0,
  create_date datetime(3) not null ,
  create_user_code varchar(32) not null ,
  create_user_name varchar (16) not null ,
  update_date datetime(3) not null,
  update_user_code varchar(32) not null ,
  update_user_name varchar (16) not null
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

drop table if exists vandesr_menu;
create table vandesr_menu (
  id int primary key auto_increment,
  menu_code varchar (32) not null ,
  menu_name varchar (32) not null ,
  menu_icon varchar (16) not null ,
  menu_url varchar (32) not null,
  parent_id int default null ,
  parent_ids varchar(64) default '' ,
  permissions varchar (32) default '',
  delete_flag tinyint default 0,
  create_date datetime(3) not null ,
  create_user_code varchar(32) not null ,
  create_user_name varchar (16) not null ,
  update_date datetime(3) not null,
  update_user_code varchar(32) not null ,
  update_user_name varchar (16) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
drop table if exists vandesr_role_menu;
create table vandesr_role_menu (
  id int primary key auto_increment,
  role_id int not null ,
  menu_id int not  null ,
  delete_flag tinyint default 0,
  create_date datetime(3) not null ,
  create_user_code varchar(32) not null ,
  create_user_name varchar (16) not null ,
  update_date datetime(3) not null,
  update_user_code varchar(32) not null ,
  update_user_name varchar (16) not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


