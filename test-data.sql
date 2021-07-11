

-- insert test tokens and token pair...
insert into token (
  `token_id`,
  `created_at`,
  `created_by`,
  `deactived`,
  `description`,
  `description_en`,
  `icon_url`,
  `sequence_number`,
  `token_struct_address`,
  `token_struct_module`,
  `token_struct_name`,
  `updated_at`,
  `updated_by`)
  values ( 'Ddd',
  UNIX_TIMESTAMP(now()),
  'admin',
  false,
  'Ddd',
  'Ddd',
  'http://starcoin.org/unknown-token-icon.jpg',
  99,
  '0x07fa08a855753f0ff7292fdcbe871216',
  'Ddd',
  'Ddd',
  UNIX_TIMESTAMP(now()),
  'admin')
;

insert into token (
  `token_id`,
  `created_at`,
  `created_by`,
  `deactived`,
  `description`,
  `description_en`,
  `icon_url`,
  `sequence_number`,
  `token_struct_address`,
  `token_struct_module`,
  `token_struct_name`,
  `updated_at`,
  `updated_by`)
  values ( 'Bot',
  UNIX_TIMESTAMP(now()),
  'admin',
  false,
  'Bot',
  'Bot',
  'http://starcoin.org/unknown-token-icon.jpg',
  90,
  '0x07fa08a855753f0ff7292fdcbe871216',
  'Bot',
  'Bot',
  UNIX_TIMESTAMP(now()),
  'admin')
;

-- token pair
insert into token_pair (  `token_x_id`, `token_y_id`,
  `created_at`,
  `created_by`,
  `deactived`,
  `description`,
  `description_en`,
  `sequence_number`,
  `default_pool_address`, -- 假设支持多个池子
  `token_x_struct_address`,
  `token_x_struct_module`,
  `token_x_struct_name`,
  `token_y_struct_address`,
  `token_y_struct_module`,
  `token_y_struct_name`,
  `updated_at`,
  `updated_by`)
  values ( 'Bot', 'Ddd',
  UNIX_TIMESTAMP(now()),
  'admin',
  false,
  'Bot<->Ddd',
  'Bot<->Ddd',
  99,
  '0x07fa08a855753f0ff7292fdcbe871216',
  '0x07fa08a855753f0ff7292fdcbe871216',
  'Bot',
  'Bot',
  '0x07fa08a855753f0ff7292fdcbe871216',
  'Ddd',
  'Ddd',
  UNIX_TIMESTAMP(now()),
  'admin')
;

-- 交易池子
insert into `token_pair_pool` (
  `pool_address`,
  `token_x_id`,
  `token_y_id`,
  `created_at`,
  `created_by`,
  `deactived`,
  `description`,
  `description_en`,
  `sequence_number`,
  `updated_at`,
  `updated_by`)
  values (
  '0x07fa08a855753f0ff7292fdcbe871216',
  'Bot',
  'Ddd',
  unix_timestamp(now()),
  'admin',
  false,
  'Bot<->Ddd Pool',
  'Bot<->Ddd Pool',
  11,
  unix_timestamp(now()),
  'admin'
  )
  ;

  -- 流动性账户（用户提供的流动性）
  insert into `liquidity_account` (
  `account_address`,
  `pool_address`,
  `token_x_id`,
  `token_y_id`,
  `created_at`,
  `created_by`,
  `deactived`,
  `liquidity`,
  `updated_at`,
  `updated_by`
  )
  values (
  '0x07fa08a855753f0ff7292fdcbe871216',
  '0x07fa08a855753f0ff7292fdcbe871216',
  'Bot',
  'Ddd',
  unix_timestamp(now()),
  'admin',
  false,
  10000000,
  unix_timestamp(now()),
  'admin'
  )
  ;

