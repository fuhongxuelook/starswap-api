
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

drop table if exists liquidity_account;
drop table if exists token;
drop table if exists token_pair;
drop table if exists token_pair_pool;

create table liquidity_account (account_address varchar(255) not null, pool_address varchar(50) not null, token_x_id varchar(50) not null, token_y_id varchar(50) not null, created_at bigint not null, created_by varchar(70) not null, deactived bit not null, liquidity decimal(19,2), updated_at bigint not null, updated_by varchar(70) not null, primary key (account_address, pool_address, token_x_id, token_y_id)) engine=InnoDB;

create table token (token_id varchar(50) not null, created_at bigint not null, created_by varchar(70) not null, deactived bit not null, description varchar(1000) not null, description_en varchar(1000) not null, icon_url varchar(1000) not null, sequence_number integer not null, token_struct_address varchar(255) not null, token_struct_module varchar(255) not null, token_struct_name varchar(255) not null, updated_at bigint not null, updated_by varchar(70) not null, primary key (token_id)) engine=InnoDB;

create table token_pair (token_x_id varchar(50) not null, token_y_id varchar(50) not null, created_at bigint not null, created_by varchar(70) not null, deactived bit not null, default_pool_address varchar(255), description varchar(1000) not null, description_en varchar(1000) not null, sequence_number integer not null, token_x_struct_address varchar(255) not null, token_x_struct_module varchar(255) not null, token_x_struct_name varchar(255) not null, token_y_struct_address varchar(255) not null, token_y_struct_module varchar(255) not null, token_y_struct_name varchar(255) not null, updated_at bigint not null, updated_by varchar(70) not null, primary key (token_x_id, token_y_id)) engine=InnoDB;

create table token_pair_pool (pool_address varchar(255) not null, token_x_id varchar(50) not null, token_y_id varchar(50) not null, created_at bigint not null, created_by varchar(70) not null, deactived bit not null, description varchar(1000) not null, description_en varchar(1000) not null, sequence_number integer not null, updated_at bigint not null, updated_by varchar(70) not null, primary key (pool_address, token_x_id, token_y_id)) engine=InnoDB;

alter table token add constraint UniqueTokenCode unique (token_struct_address, token_struct_module, token_struct_name);


SET FOREIGN_KEY_CHECKS = 1;


  