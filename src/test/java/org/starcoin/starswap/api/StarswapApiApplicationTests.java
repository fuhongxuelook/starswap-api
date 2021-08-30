package org.starcoin.starswap.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.starcoin.starswap.api.data.model.LiquidityToken;
import org.starcoin.starswap.api.data.model.LiquidityTokenId;
import org.starcoin.starswap.api.data.model.StructType;
import org.starcoin.starswap.api.data.model.Token;
import org.starcoin.starswap.api.data.repo.LiquidityTokenRepository;
import org.starcoin.starswap.api.data.repo.TokenRepository;

@SpringBootTest
class StarswapApiApplicationTests {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    LiquidityTokenRepository liquidityTokenRepository;

    @Test
    void contextLoads() {
//-- insert test tokens and token pair...
//insert into token (
//  `token_id`,
//  `created_at`,
//  `created_by`,
//  `deactived`,
//  `description`,
//  `description_en`,
//  `icon_url`,
//  `sequence_number`,
//  `token_struct_address`,
//  `token_struct_module`,
//  `token_struct_name`,
//  `updated_at`,
//  `updated_by`)
        Token ddd = new Token();
        ddd.setTokenId("Ddd");//  values ( 'Ddd',
        ddd.setCreatedAt(System.currentTimeMillis());//  UNIX_TIMESTAMP(now()),
        ddd.setCreatedBy("admin");//  'admin',
        ddd.setDeactived(false);//  false,
        ddd.setDescription("Ddd");//  'Ddd',
        ddd.setIconUrl("http://starcoin.org/unknown-token-icon.jpg");
        ddd.setSequenceNumber(99);//  99,
        ddd.setTokenStructType(new StructType("0x07fa08a855753f0ff7292fdcbe871216",
                "Ddd",
                "Ddd"));
        ddd.setUpdatedAt(System.currentTimeMillis());//  UNIX_TIMESTAMP(now()),
        ddd.setUpdatedBy("admin");//  'admin')
        tokenRepository.save(ddd);

//insert into token (
//  `token_id`,
//  `created_at`,
//  `created_by`,
//  `deactived`,
//  `description`,
//  `description_en`,
//  `icon_url`,
//  `sequence_number`,
//  `token_struct_address`,
//  `token_struct_module`,
//  `token_struct_name`,
//  `updated_at`,
//  `updated_by`)
        Token bot = new Token();
        bot.setTokenId("Bot");//  values ( 'Bot',
        bot.setCreatedAt(System.currentTimeMillis());//  UNIX_TIMESTAMP(now()),
        bot.setCreatedBy("admin");//  'admin',
        bot.setDeactived(false);//  false,
        bot.setDescription("Bot");//  'Bot',
        bot.setIconUrl("http://starcoin.org/unknown-token-icon.jpg");
        bot.setSequenceNumber(90);//  90,
        bot.setTokenStructType(new StructType("0x07fa08a855753f0ff7292fdcbe871216",
                "Bot",
                "Bot"));
        bot.setUpdatedAt(System.currentTimeMillis());//  UNIX_TIMESTAMP(now()),
        bot.setUpdatedBy("admin");//  'admin')
        tokenRepository.save(bot);

//-- token pair
//insert into token_pair (  `token_x_id`, `token_y_id`,
//  `created_at`,
//  `created_by`,
//  `deactived`,
//  `description`,
//  `description_en`,
//  `sequence_number`,
//  `default_pool_address`, -- 假设支持多个池子
//  `token_x_struct_address`,
//  `token_x_struct_module`,
//  `token_x_struct_name`,
//  `token_y_struct_address`,
//  `token_y_struct_module`,
//  `token_y_struct_name`,
//  `updated_at`,
//  `updated_by`)
        LiquidityToken botDdd = new LiquidityToken();
        botDdd.setLiquidityTokenId(new LiquidityTokenId("Bot", "Ddd", "0x07fa08a855753f0ff7292fdcbe871216"));//  values ( 'Bot', 'Ddd',
        botDdd.setCreatedAt(System.currentTimeMillis());//  UNIX_TIMESTAMP(now()),
        botDdd.setCreatedBy("admin");//  'admin',
        botDdd.setDeactived(false);//  false,
        botDdd.setDescription("Bot<->Ddd");
        botDdd.setSequenceNumber(99);
        botDdd.setTokenXStructType(new StructType("0x07fa08a855753f0ff7292fdcbe871216", "Bot", "Bot"));
        botDdd.setTokenYStructType(new StructType("0x07fa08a855753f0ff7292fdcbe871216", "Ddd", "Ddd"));
//  '0x07fa08a855753f0ff7292fdcbe871216',
//  'Bot',
//  'Bot',
//  '0x07fa08a855753f0ff7292fdcbe871216',
//  'Ddd',
//  'Ddd',
        botDdd.setUpdatedAt(System.currentTimeMillis());//  UNIX_TIMESTAMP(now()),
        botDdd.setUpdatedBy("admin");//  'admin')
        liquidityTokenRepository.save(botDdd);

//-- 交易池子
//insert into `token_pair_pool` (
//  `pool_address`,
//  `token_x_id`,
//  `token_y_id`,
//  `created_at`,
//  `created_by`,
//  `deactived`,
//  `description`,
//  `description_en`,
//  `sequence_number`,
//  `updated_at`,
//  `updated_by`)
//  values (
//  '0x07fa08a855753f0ff7292fdcbe871216',
//  'Bot',
//  'Ddd',
//  unix_timestamp(now()),
//  'admin',
//  false,
//  'Bot<->Ddd Pool',
//  'Bot<->Ddd Pool',
//  11,
//  unix_timestamp(now()),
//  'admin'
//  )
//  ;
//
//  -- 流动性账户（用户提供的流动性）
//  insert into `liquidity_account` (
//  `account_address`,
//  `pool_address`,
//  `token_x_id`,
//  `token_y_id`,
//  `created_at`,
//  `created_by`,
//  `deactived`,
//  `liquidity`,
//  `updated_at`,
//  `updated_by`
//  )
//  values (
//  '0x07fa08a855753f0ff7292fdcbe871216',
//  '0x07fa08a855753f0ff7292fdcbe871216',
//  'Bot',
//  'Ddd',
//  unix_timestamp(now()),
//  'admin',
//  false,
//  10000000,
//  unix_timestamp(now()),
//  'admin'
//  )
//  ;
//
//
// insert into pulling_event_task
//	(from_block_number,
//    created_at,
//    created_by,
//    status,
//    to_block_number,
//    updated_at,
//    updated_by
//    ) values (
//    0,
//    unix_timestamp(now()),
//    'ADMIN',
//    'CREATED',
//    200,
//    unix_timestamp(now()),
//    'ADMIN'
//    );

    }

}
