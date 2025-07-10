package com.checkpoint.productmanagement.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoggingService {
    private final List<String> pendingOperations = new ArrayList<>();
    private final String LOG_FILE_PATH = "operations.log";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logOperation(String operation, Long id, String name, Integer quantity) {
        String logEntry = String.format("[%s] %s: ID = %d, Name = %s, Quantity = %d", 
            LocalDateTime.now().format(formatter), operation, id, name, quantity);
        pendingOperations.add(logEntry);
        
        // Write to file if we have 5 or more operations
        if (pendingOperations.size() >= 5) {
            writeToFile();
        }
    }
// 10 minutes = 600,000 milliseconds
    @Scheduled(fixedRate = 600000) 
    public void scheduledLogWrite() {
        if (!pendingOperations.isEmpty()) {
            writeToFile();
        }
    }

    private void writeToFile() {
        if (pendingOperations.isEmpty()) {
            return;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.println("=== Operations Log ===");
            writer.println("Timestamp: " + LocalDateTime.now().format(formatter));
            writer.println("Operations count: " + pendingOperations.size());
            writer.println();
            
            for (String operation : pendingOperations) {
                writer.println(operation);
            }
            
            writer.println();
            
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
        

        pendingOperations.clear();
    }

    public int getPendingOperationsCount() {
        return pendingOperations.size();
    }
} 