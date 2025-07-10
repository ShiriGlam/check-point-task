package com.checkpoint.productmanagement.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.Queue;

@Service
public class LoggingService {
    private final Queue<String> pendingOperations = new LinkedList<>();
    private final String LOG_FILE_PATH = "operations.log";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Object fileWriteLock = new Object();

    public void logOperation(String operation, Long id, String name, Integer quantity) {
        String logEntry = String.format("[%s] %s: ID = %d, Name = %s, Quantity = %d", 
            LocalDateTime.now().format(formatter), operation, id, name, quantity);
        
        synchronized (fileWriteLock) {
            pendingOperations.offer(logEntry);
            
            //write to file if we have 5 or more operations
            if (pendingOperations.size() >= 5) {
                writeToFile();
            }
        }
    }
//10 minutes= 600000 millisec
    @Scheduled(fixedRate = 600000) 
    public void scheduledLogWrite() {
        synchronized (fileWriteLock) {
            if (!pendingOperations.isEmpty()) {
                writeToFile();
            }
        }
    }

    private void writeToFile() {
        if (pendingOperations.isEmpty()) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
            
            while (!pendingOperations.isEmpty()) {
                String operation = pendingOperations.poll();
                writer.println(operation);
            }
            
            writer.println();
            
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
        

    }

    public int getPendingOperationsCount() {
        synchronized (fileWriteLock) {
            return pendingOperations.size();
        }
    }
} 