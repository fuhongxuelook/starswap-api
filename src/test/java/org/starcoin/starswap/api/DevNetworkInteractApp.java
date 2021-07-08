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
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    new String[]{shellPath, "-c", starcoinCmd}
                    //new String[] {starcoinCmd, "-n", "dev", "console"}
            );
            processBuilder.directory(new File(moveProjectDir));
            //processBuilder.inheritIO();
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            CommandLineInteractor commandLineInteractor = new CommandLineInteractor(process);
            String firstPrivateKey = args[0];
            String secondPrivateKey = args[1];
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
            // todo
            //.sendLine()
            ;


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
