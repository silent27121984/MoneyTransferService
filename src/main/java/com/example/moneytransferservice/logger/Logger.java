package com.example.moneytransferservice.logger;

import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
@NoArgsConstructor
@Value
@Component
public class Logger {

    private final AtomicInteger numberMessage = new AtomicInteger();

    private final Date data = new Date();

    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public synchronized void getLog(String msg) {
        try (FileOutputStream fos = new FileOutputStream("Log", false)) {
            String str = String.format (numberMessage.getAndIncrement() + " " + formatter.format(data) + " " + msg + "\n");
            byte[] bytes = str.getBytes();
            fos.write(bytes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}