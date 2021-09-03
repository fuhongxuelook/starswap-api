package org.starcoin.starswap.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.starcoin.starswap.api.data.model.*;
import org.starcoin.starswap.api.data.repo.*;
import org.starcoin.starswap.api.service.LiquidityPoolService;
import org.starcoin.starswap.api.service.LiquidityTokenService;

import java.math.BigInteger;
import java.util.List;

@SpringBootTest
class StarswapApiApplicationTests {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    LiquidityTokenRepository liquidityTokenRepository;

    @Autowired
    LiquidityPoolRepository liquidityPoolRepository;

    @Autowired
    LiquidityAccountRepository liquidityAccountRepository;

    @Autowired
    LiquidityTokenService liquidityTokenService;

    @Autowired
    LiquidityPoolService liquidityPoolService;

    @Autowired
    LiquidityTokenFarmRepository liquidityTokenFarmRepository;

    @Autowired
    LiquidityTokenFarmAccountRepository liquidityTokenFarmAccountRepository;

    @Test
    void contextLoads() {

        tryRun(() -> addTestToken("Bot", 90));

        tryRun(() -> addTestToken("Ddd", 99));

        tryRun(this::addTestTokenPair);

        tryRun(this::addTestLiquidityPool);

        tryRun(this::addTestLiquidityAccount);

        tryRun(this::addTestLiquidityTokenFarm);

        tryRun(this::addTestFarmAccount);

        // test queries...
        System.out.println(liquidityTokenService.findOneByTokenIdPair("Bot", "Ddd"));
        System.out.println(liquidityPoolService.findOneByTokenIdPair("Bot", "Ddd"));
    }

    private void tryRun(Runnable runnable) {
        try {
            runnable.run();
        } catch (RuntimeException runtimeException) {
            runtimeException.printStackTrace();
        }
    }

    private void addTestFarmAccount() {
        LiquidityTokenFarmAccount farmAccount = new LiquidityTokenFarmAccount();//  values (
        farmAccount.setFarmAccountId(new LiquidityTokenFarmAccountId(
                "0x07fa08a855753f0ff7292fdcbe871216", new LiquidityTokenFarmId(
                new LiquidityTokenId("Bot", "Ddd", "0x07fa08a855753f0ff7292fdcbe871216"),
                "0x07fa08a855753f0ff7292fdcbe871216")));//  '0x07fa08a855753f0ff7292fdcbe871216',
        //  '0x07fa08a855753f0ff7292fdcbe871216',
        //  'Bot',
        //  'Ddd',
        farmAccount.setCreatedAt(System.currentTimeMillis());//  unix_timestamp(now()),
        farmAccount.setCreatedBy("admin");//  'admin',
        farmAccount.setDeactived(false);//  false,
        farmAccount.setStakeAmount(BigInteger.valueOf(1000000L));//  10000000,
        farmAccount.setUpdatedAt(System.currentTimeMillis());//  unix_timestamp(now()),
        farmAccount.setUpdatedBy("admin");//  'admin'
        liquidityTokenFarmAccountRepository.save(farmAccount);
        //  )
        //  ;
    }


    private void addTestLiquidityTokenFarm() {
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

        LiquidityTokenFarm liquidityTokenFarm = new LiquidityTokenFarm();
        liquidityTokenFarm.setLiquidityTokenFarmId(new LiquidityTokenFarmId(
                new LiquidityTokenId("Bot", "Ddd", "0x07fa08a855753f0ff7292fdcbe871216"),
                "0x07fa08a855753f0ff7292fdcbe871216"));
        liquidityTokenFarm.setCreatedAt(System.currentTimeMillis());//  unix_timestamp(now()),
        liquidityTokenFarm.setCreatedBy("admin");//  'admin',
        liquidityTokenFarm.setDeactived(false);//  false,
        liquidityTokenFarm.setDescription("Bot<->Ddd Pool");
        //  'Bot<->Ddd Pool',
        liquidityTokenFarm.setSequenceNumber(11);//  11,
        liquidityTokenFarm.setUpdatedAt(System.currentTimeMillis());//  unix_timestamp(now()),
        liquidityTokenFarm.setUpdatedBy("admin");//  'admin'
        liquidityTokenFarmRepository.save(liquidityTokenFarm);

        List<LiquidityTokenFarm> liquidityTokenFarms = liquidityTokenFarmRepository.findByLiquidityTokenFarmIdTokenXIdAndLiquidityTokenFarmIdTokenYId("Bot", "Ddd");
        System.out.println("Get liquidity token farms: " + liquidityTokenFarms.size());
    }

    private void addTestLiquidityAccount() {
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
        LiquidityAccount liquidityAccount = new LiquidityAccount();//  values (
        liquidityAccount.setLiquidityAccountId(new LiquidityAccountId(
                "0x07fa08a855753f0ff7292fdcbe871216", new LiquidityPoolId(
                new LiquidityTokenId("Bot", "Ddd", "0x07fa08a855753f0ff7292fdcbe871216"),
                "0x07fa08a855753f0ff7292fdcbe871216")));//  '0x07fa08a855753f0ff7292fdcbe871216',
        //  '0x07fa08a855753f0ff7292fdcbe871216',
        //  'Bot',
        //  'Ddd',
        liquidityAccount.setCreatedAt(System.currentTimeMillis());//  unix_timestamp(now()),
        liquidityAccount.setCreatedBy("admin");//  'admin',
        liquidityAccount.setDeactived(false);//  false,
        liquidityAccount.setLiquidity(BigInteger.valueOf(1000000L));//  10000000,
        liquidityAccount.setUpdatedAt(System.currentTimeMillis());//  unix_timestamp(now()),
        liquidityAccount.setUpdatedBy("admin");//  'admin'
        liquidityAccountRepository.save(liquidityAccount);
        //  )
        //  ;

    }

    private void addTestLiquidityPool() {
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
        LiquidityPool liquidityPool = new LiquidityPool();
        liquidityPool.setLiquidityPoolId(new LiquidityPoolId(
                new LiquidityTokenId("Bot", "Ddd", "0x07fa08a855753f0ff7292fdcbe871216"),
                "0x07fa08a855753f0ff7292fdcbe871216"));
        liquidityPool.setCreatedAt(System.currentTimeMillis());//  unix_timestamp(now()),
        liquidityPool.setCreatedBy("admin");//  'admin',
        liquidityPool.setDeactived(false);//  false,
        liquidityPool.setDescription("Bot<->Ddd Pool");
        //  'Bot<->Ddd Pool',
        liquidityPool.setSequenceNumber(11);//  11,
        liquidityPool.setUpdatedAt(System.currentTimeMillis());//  unix_timestamp(now()),
        liquidityPool.setUpdatedBy("admin");//  'admin'
        liquidityPoolRepository.save(liquidityPool);

        List<LiquidityPool> liquidityPools = liquidityPoolRepository.findByLiquidityPoolIdTokenXIdAndLiquidityPoolIdTokenYId("Bot", "Ddd");
        System.out.println("Get liquidity pools: " + liquidityPools.size());

        //  )
        //  ;
        //
    }

    private void addTestTokenPair() {
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
    }

    private void addTestToken(String tokenId, int seqNumber) {
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
        ddd.setTokenId(tokenId);//  values ( 'Ddd',
        ddd.setCreatedAt(System.currentTimeMillis());//  UNIX_TIMESTAMP(now()),
        ddd.setCreatedBy("admin");//  'admin',
        ddd.setDeactived(false);//  false,
        ddd.setDescription(tokenId);//  'Ddd',
        ddd.setIconUrl("http://starcoin.org/unknown-token-icon.jpg");
        ddd.setSequenceNumber(seqNumber);//  99,
        ddd.setTokenStructType(new StructType("0x07fa08a855753f0ff7292fdcbe871216",
                tokenId,
                tokenId));
        ddd.setUpdatedAt(System.currentTimeMillis());//  UNIX_TIMESTAMP(now()),
        ddd.setUpdatedBy("admin");//  'admin')
        //ddd.setVersion(2L);
        tokenRepository.save(ddd);
        //if (true) return;
    }

}
