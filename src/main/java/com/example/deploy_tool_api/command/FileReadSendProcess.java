package com.example.deploy_tool_api.command;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileReadSendProcess implements Runnable {
    Runtime runtime = Runtime.getRuntime();
    String logFile;
    String logFileSendTo;
    MessageSender messageSender;

    Process process;

    public FileReadSendProcess(String logFile, String logFileSendTo, MessageSender messageSender) {
        this.logFile = logFile;
        this.logFileSendTo = logFileSendTo;
        this.messageSender = messageSender;
    }

    public static boolean isLinux() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("linux");
    }

    public static String generate_cmd(String file) {
        String cmd = "tail -f " + file;
        if (!isLinux()) {
            cmd = "powershell -Command \"Get-Content -Path '" + file + "' -Wait\"";
        }
        return cmd;
    }

    public void read1() {
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new java.io.InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                messageSender.send(logFileSendTo, line);
            }
        } catch (IOException e) {
            System.out.println("read error");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void read2() {
        InputStream inputStream = process.getInputStream();
        BufferedInputStream stream = null;
        try {
            stream = new BufferedInputStream(inputStream);
            byte[] b = new byte[1000];
            int n;
            while ((n = stream.read(b, 0, b.length)) > 0) {
                String s = new String(b, 0, n, StandardCharsets.UTF_8);
                messageSender.send(logFileSendTo, s);
            }
        } catch (IOException e) {
            System.out.println("read error");
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            process = runtime.exec(generate_cmd(logFile));
            if (process.isAlive()) {
                read2();
                process.waitFor();
            }
        } catch (IOException e) {
            System.out.println(logFile + " file read process IOException!");
        } catch (InterruptedException e) {
            System.out.println(logFile + " file read process InterruptedException!");
        } finally {
            process = null;
        }
    }
}
