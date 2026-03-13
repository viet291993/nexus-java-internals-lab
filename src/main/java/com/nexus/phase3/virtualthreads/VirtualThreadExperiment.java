package com.nexus.phase3.virtualthreads;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * Issue #7: Virtual Threads & Scalability Experimentation
 * So sánh hiệu năng giữa Platform Threads (luồng hệ điều hành) và Virtual Threads (luồng ảo).
 */
public class VirtualThreadExperiment {
    private static final int TASKS_COUNT = 10_000;

    public static void main(String[] args) {
        System.out.println("--- Bắt đầu thí nghiệm Virtual Threads ---");
        
        // 1. Thử nghiệm với Platform Threads (Giới hạn 1,000 threads)
        System.out.println("\n[1] Thử nghiệm Platform Threads (1,000 threads)...");
        runExperiment(Executors.newFixedThreadPool(1000), "Platform Thread Pool");

        // 2. Thử nghiệm với Virtual Threads (Mỗi task một thread ảo)
        System.out.println("\n[2] Thử nghiệm Virtual Threads (Java 21+)...");
        runExperiment(Executors.newVirtualThreadPerTaskExecutor(), "Virtual Thread Executor");
    }

    private static void runExperiment(java.util.concurrent.ExecutorService executor, String label) {
        long startTime = System.currentTimeMillis();
        AtomicInteger completedTasks = new AtomicInteger(0);

        try (executor) {
            IntStream.range(0, TASKS_COUNT).forEach(i -> {
                executor.submit(() -> {
                    try {
                        // Giả lập một I/O task tốn 1 giây (ví dụ: gọi API, đọc DB)
                        Thread.sleep(Duration.ofSeconds(1));
                        completedTasks.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            });
        } // ExecutorService tự động đóng và chờ hoàn thành trong Java 19+

        long endTime = System.currentTimeMillis();
        System.out.printf("[%s] Hoàn thành %d tasks trong %d ms\n", 
                label, completedTasks.get(), (endTime - startTime));
    }
}
