package com.example.moneytransferservice.logger;

import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class Logger {

    private final AtomicInteger numberMessage = new AtomicInteger(0);

    public Logger() {

    }

    public synchronized void getLog(String msg) {
        Date time = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try (FileOutputStream fos = new FileOutputStream("log.txt", false)) {
            String str = numberMessage.getAndIncrement() + " " + formatter.format(time) + " " + msg + "\n";
            byte[] bytes = str.getBytes();
            fos.write(bytes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}