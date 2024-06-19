package com.example.deploy_tool_api.command;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class FileWatch {

    private final ArrayList<Process> processList = new ArrayList<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    Runtime runtime = Runtime.getRuntime();
    private final SimpMessagingTemplate template;

    @Autowired
    public FileWatch(SimpMessagingTemplate template) {
        this.template = template;
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

    public void watch(String file, String sendTo) {
        String cmd = generate_cmd(file);
        try {
            Process process = runtime.exec(cmd);
            processList.add(process);
            executorService.submit(new InputStreamReadSend(process.getInputStream(), template, sendTo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void init() {
        String file = "";
//        file = "D:\\develop\\project\\company-project\\deploy_tool_api\\1.txt";
//        watch(file, "front");

        file = "/opt/project/app/logs/api.log";
        watch(file, "api");
        file = "/opt/project/app/logs/front.log";
        watch(file, "front");
    }

    @PreDestroy
    public void destroy() {
        processList.forEach(Process::destroy);
        executorService.shutdownNow();
        System.out.println("executorService shutdownNow");
    }


}
