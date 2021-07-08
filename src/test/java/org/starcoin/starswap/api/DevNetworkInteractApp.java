package org.starcoin.starswap.api;

import java.io.*;

public class DevNetworkInteractApp {

    public static void main(String[] args) {
        String shellPath = "/bin/sh";
        String starcoinCmd = "/Users/yangjiefeng/Documents/starcoinorg/starcoin/target/debug/starcoin -n dev -d alice console";
        String moveProjectDir = "/Users/yangjiefeng/Documents/starcoinorg/starswap-core";

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                  new String[]{shellPath, "-c", starcoinCmd}
                    //new String[] {starcoinCmd, "-n", "dev", "console"}
            );
            processBuilder.directory(new File(moveProjectDir));
            //processBuilder.inheritIO();
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            String expectFor = "Start console,";

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            boolean expectOk = expect(reader, expectFor, 10);
            System.out.println("==============" + expectOk);
            //if (expectOk) {
                sendLine(writer,
                        "account import -i xxxx");
            //}
            expectOk = expect(reader, "\"ok\":", 10);
            System.out.println("==============" + expectOk);
            sendLine(writer, "account default 0x07fa08a855753f0ff7292fdcbe871216");
            expectOk = expect(reader, "\"ok\":", 10);
            System.out.println("==============" + expectOk);
            sendLine(writer, "dev get-coin 0x07fa08a855753f0ff7292fdcbe871216");
            expectOk = expect(reader, "\"ok\":", 10);
            System.out.println("==============" + expectOk);
            sendLine(writer, "account show");
            expectOk = expect(reader, "\"ok\":", 10);
            System.out.println("==============" + expectOk);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    private static void sendLine(BufferedWriter writer, String outputLine) throws IOException {
        writer.write(outputLine);
        writer.newLine();
        writer.flush();
    }

    private static boolean expect(BufferedReader reader, String expectFor, int timeOutSeconds) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        boolean expectOk = false;
        StringBuilder inputSB = new StringBuilder();
        while (true) {
            String bufferStr = null;
            while (reader.ready()) {
                char[] charBuffer = new char[200];
                int len = reader.read(charBuffer, 0, charBuffer.length);
                bufferStr = new String(charBuffer, 0, len);
                System.out.println("----" + bufferStr);
                inputSB.append(bufferStr);
            }
            if (bufferStr == null) {
                //System.out.println("=========== buffer str is null");
                Thread.sleep(100);
            }
            String inputStr = inputSB.toString();
            //System.out.println(inputStr);
            if (inputStr.contains(expectFor)) {
                expectOk = true;
                break;
            }
            if (startTime + timeOutSeconds * 1000 < System.currentTimeMillis()) {
                break;
            }
        }
        return expectOk;
    }
}
