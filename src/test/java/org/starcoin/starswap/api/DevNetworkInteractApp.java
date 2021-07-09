package org.starcoin.starswap.api;

import java.io.File;
import java.io.IOException;

public class DevNetworkInteractApp {

    public static void main(String[] args) {
        String shellPath = "/bin/sh";
        String starcoinCmd = "/Users/yangjiefeng/Documents/starcoinorg/starcoin/target/debug/starcoin -n dev -d alice console";
        String moveProjectDir = "/Users/yangjiefeng/Documents/starcoinorg/starswap-core";
        if (args.length < 2) {
            throw new IllegalArgumentException("Please enter two account private keys");
        }
        String firstPrivateKey = args[0];
        String secondPrivateKey = args[1];
        Process process = null;
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    new String[]{shellPath, "-c", starcoinCmd}
                    //new String[] {starcoinCmd, "-n", "dev", "console"}
            );
            processBuilder.directory(new File(moveProjectDir));
            //processBuilder.inheritIO();
            processBuilder.redirectErrorStream(true);
            process = processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        if (process == null) {
            throw new NullPointerException();
        }

        CommandLineInteractor commandLineInteractor = new CommandLineInteractor(process);
        commandLineInteractor.expect("Start console,", 10)
                // 导入账户，部署合约
                .sendLine("account import -i " + firstPrivateKey)
                .expect("\"ok\":", 10)
                .sendLine("account import -i " + secondPrivateKey)
                .expect("\"ok\":", 10)
                .sendLine("account default 0x07fa08a855753f0ff7292fdcbe871216")
                .expect("\"ok\":", 10)
                .sendLine("account unlock 0x07fa08a855753f0ff7292fdcbe871216")
                .expect("\"ok\":", 10)
                .sendLine("account unlock 0xff2794187d72cc3a9240198ca98ac7b6")
                .expect("\"ok\":", 10)
                .sendLine("dev get-coin 0x07fa08a855753f0ff7292fdcbe871216")
                .expect("\"ok\":", 10)
                .sendLine("dev get-coin 0xff2794187d72cc3a9240198ca98ac7b6")
                .expect("\"ok\":", 10)
                .sendLine("dev deploy storage/0x07fa08a855753f0ff7292fdcbe871216/modules/TokenSwap.mv -b")
                .expect("\"ok\":", 10)
                .sendLine("dev deploy storage/0x07fa08a855753f0ff7292fdcbe871216/modules/TokenSwapRouter.mv -b")
                .expect("\"ok\":", 10)
                .sendLine("dev deploy storage/0x07fa08a855753f0ff7292fdcbe871216/modules/TokenSwapScripts.mv -b")
                .expect("\"ok\":", 10)
                .sendLine("dev deploy storage/0x07fa08a855753f0ff7292fdcbe871216/modules/Bot.mv -b")
                .expect("\"ok\":", 10)
                .sendLine("dev deploy storage/0x07fa08a855753f0ff7292fdcbe871216/modules/Ddd.mv -b")
                .expect("\"ok\":", 10)
                // 注册代币资源、发币
                .sendLine("account execute-function -s 0x07fa08a855753f0ff7292fdcbe871216 --function 0x07fa08a855753f0ff7292fdcbe871216::Ddd::init -b")
                .expect("\"ok\":", 10)
                .sendLine("account execute-function -s 0x07fa08a855753f0ff7292fdcbe871216 --function 0x07fa08a855753f0ff7292fdcbe871216::Ddd::mint --arg 100000000999u128 -b")
                .expect("\"ok\":", 10)
                .sendLine("account execute-function -s 0x07fa08a855753f0ff7292fdcbe871216 --function 0x07fa08a855753f0ff7292fdcbe871216::Bot::init -b")
                .expect("\"ok\":", 10)
                .sendLine("account execute-function -s 0x07fa08a855753f0ff7292fdcbe871216 --function 0x07fa08a855753f0ff7292fdcbe871216::Bot::mint --arg 100000000999u128 -b")
                .expect("\"ok\":", 10)
                // 转一部分币给账户2
                .sendLine("account default 0xff2794187d72cc3a9240198ca98ac7b6")
                .expect("\"ok\":", 10)
                .sendLine("account unlock")
                .expect("\"ok\":", 10)
                .sendLine("account accept-token 0x07fa08a855753f0ff7292fdcbe871216::Bot::Bot")
                .expect("\"ok\":", 10)
                .waitSeconds(10)// or -b
                .sendLine("account accept-token 0x07fa08a855753f0ff7292fdcbe871216::Ddd::Ddd")
                .expect("\"ok\":", 10)
                .waitSeconds(10)
                .sendLine("account show")
                .expect("\"ok\":", 10)
                .sendLine("account default 0x07fa08a855753f0ff7292fdcbe871216")
                .expect("\"ok\":", 10)
                .sendLine("account unlock")
                .expect("\"ok\":", 10)
                .sendLine("account show")
                .expect("\"ok\":", 10)
                .sendLine("account execute-function -s 0x07fa08a855753f0ff7292fdcbe871216 --function 0x1::TransferScripts::peer_to_peer_v2 -t 0x07fa08a855753f0ff7292fdcbe871216::Bot::Bot --arg 0xff2794187d72cc3a9240198ca98ac7b6 --arg 100000u128 -b")
                .expect("\"ok\":", 10)
                .sendLine("account execute-function -s 0x07fa08a855753f0ff7292fdcbe871216 --function 0x1::TransferScripts::peer_to_peer_v2 -t 0x07fa08a855753f0ff7292fdcbe871216::Ddd::Ddd --arg 0xff2794187d72cc3a9240198ca98ac7b6 --arg 100000u128 -b")
                .expect("\"ok\":", 10)
                // 注册交易对
                .sendLine("account execute-function -s 0x07fa08a855753f0ff7292fdcbe871216 --function 0x07fa08a855753f0ff7292fdcbe871216::TokenSwapScripts::register_swap_pair -t 0x07fa08a855753f0ff7292fdcbe871216::Bot::Bot -t 0x07fa08a855753f0ff7292fdcbe871216::Ddd::Ddd -b")
                .expect("\"ok\":", 10)
                // 注册STC&lt;-&gt;BOT交易对
                .sendLine("account execute-function -s 0x07fa08a855753f0ff7292fdcbe871216 --function 0x07fa08a855753f0ff7292fdcbe871216::TokenSwapScripts::register_swap_pair -t 0x1::STC::STC -t 0x07fa08a855753f0ff7292fdcbe871216::Bot::Bot -b")
                .expect("\"ok\":", 10)
                // 增加流动性
                .sendLine("account execute-function -s 0x07fa08a855753f0ff7292fdcbe871216 --function 0x07fa08a855753f0ff7292fdcbe871216::TokenSwapScripts::add_liquidity -t 0x07fa08a855753f0ff7292fdcbe871216::Bot::Bot -t 0x07fa08a855753f0ff7292fdcbe871216::Ddd::Ddd --arg 10000u128 --arg 100000u128 --arg 5000u128 --arg 50000u128 -b")
                .expect("\"ok\":", 10)
                // 查询整体流动性
                .sendLine("dev call --function 0x07fa08a855753f0ff7292fdcbe871216::TokenSwapRouter::total_liquidity -t 0x07fa08a855753f0ff7292fdcbe871216::Bot::Bot -t 0x07fa08a855753f0ff7292fdcbe871216::Ddd::Ddd")
                .expect("\"ok\":", 10)
                // 查询swap pair存在性
                .sendLine("dev call --function 0x07fa08a855753f0ff7292fdcbe871216::TokenSwapRouter::swap_pair_exists -t 0x07fa08a855753f0ff7292fdcbe871216::Bot::Bot -t 0x07fa08a855753f0ff7292fdcbe871216::Ddd::Ddd")
                .expect("\"ok\":", 10)
                // 查询当前用户流动性
                .sendLine("dev call --function 0x07fa08a855753f0ff7292fdcbe871216::TokenSwapRouter::liquidity -t 0x07fa08a855753f0ff7292fdcbe871216::Bot::Bot -t 0x07fa08a855753f0ff7292fdcbe871216::Ddd::Ddd --arg 0x07fa08a855753f0ff7292fdcbe871216")
                .expect("\"ok\":", 10)
                // 置换代币
                .sendLine("account execute-function -s 0x07fa08a855753f0ff7292fdcbe871216 --function 0x07fa08a855753f0ff7292fdcbe871216::TokenSwapScripts::swap_exact_token_for_token -t 0x07fa08a855753f0ff7292fdcbe871216::Bot::Bot -t 0x07fa08a855753f0ff7292fdcbe871216::Ddd::Ddd --arg 100u128 --arg 100u128 -b")
                .expect("\"ok\":", 10)
                // 获取代币存量
                .sendLine("dev call --function 0x07fa08a855753f0ff7292fdcbe871216::TokenSwapRouter::get_reserves -t 0x07fa08a855753f0ff7292fdcbe871216::Bot::Bot -t 0x07fa08a855753f0ff7292fdcbe871216::Ddd::Ddd")
                .expect("\"ok\":", 10)
                // 获取代币换出额度
                .sendLine("dev call --function 0x07fa08a855753f0ff7292fdcbe871216::TokenSwapRouter::get_amount_out --arg 1000u128 --arg 1000u128 --arg 2000u128")
                .expect("\"ok\":", 10)
                // 获取流动性计算
                .sendLine("dev call --function 0x07fa08a855753f0ff7292fdcbe871216::TokenSwapRouter::quote --arg 1000u128 --arg 1000u128 --arg 2000u128")
                .expect("\"ok\":", 10)
                // 移除流动性
                .sendLine("account execute-function -s 0x07fa08a855753f0ff7292fdcbe871216 --function 0x07fa08a855753f0ff7292fdcbe871216::TokenSwapScripts::remove_liquidity -t 0x07fa08a855753f0ff7292fdcbe871216::Bot::Bot -t 0x07fa08a855753f0ff7292fdcbe871216::Ddd::Ddd --arg 1000u128 --arg 1u128 --arg 1u128 -b")
                .expect("\"ok\":", 10)
                // 检查代币发布
                .sendLine("dev call --function 0x07fa08a855753f0ff7292fdcbe871216::TokenSwap::assert_is_token -t 0x07fa08a855753f0ff7292fdcbe871216::Bot::Bot")
                .expect("\"ok\":", 10)
        ;
    }

}
