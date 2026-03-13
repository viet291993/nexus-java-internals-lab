package com.nexus.phase3;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Issue #5: Race Condition & Atomic Synchronization Strategy
 * Mục tiêu: Chứng kiến cảnh nhiều luồng cùng tranh giành biến chung và cách khắc phục.
 */
public class RaceConditionDemo {

    // 1. Biến chung KHÔNG được bảo vệ (Sẽ gây ra Race Condition)
    private static int unsafeCounter = 0;

    // 2. Biến dùng Atomic (An toàn luồng - Lock-free)
    private static AtomicInteger atomicCounter = new AtomicInteger(0);

    // 3. Biến dùng synchronized (An toàn luồng - Lock-based)
    private static int syncCounter = 0;

    public static void main(String[] args) throws InterruptedException {
        int numberOfThreads = 100;
        int incrementsPerThread = 10000;
        int expectedValue = numberOfThreads * incrementsPerThread;

        System.out.println("--- Bắt đầu thí nghiệm Race Condition ---");
        System.out.println("Số luồng: " + numberOfThreads);
        System.out.println("Số lần tăng mỗi luồng: " + incrementsPerThread);
        System.out.println("Kết quả mong đợi: " + expectedValue);
        System.out.println("------------------------------------------");

        // Tạo danh sách các thread
        Thread[] threads = new Thread[numberOfThreads];

        for (int i = 0; i < numberOfThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < incrementsPerThread; j++) {
                    // Tăng biến không an toàn
                    unsafeCounter++; 

                    // Tăng biến dùng Atomic
                    atomicCounter.incrementAndGet();

                    // Tăng biến dùng Synchronized
                    incrementSync();
                }
            });
            threads[i].start();
        }

        // Chờ tất cả các thread hoàn thành
        for (Thread t : threads) {
            t.join();
        }

        System.out.println("KẾT QUẢ THỰC TẾ:");
        System.out.println("1. Unsafe Counter:   " + unsafeCounter + (unsafeCounter == expectedValue ? " (OK)" : " (SAI RỒI!)"));
        System.out.println("2. Atomic Counter:   " + atomicCounter.get() + (atomicCounter.get() == expectedValue ? " (OK)" : " (SAI!)"));
        System.out.println("3. Sync Counter:     " + syncCounter + (syncCounter == expectedValue ? " (OK)" : " (SAI!)"));
        
        System.out.println("\nLƯU Ý: Unsafe Counter bị sai vì 'count++' không phải là một thao tác đơn tử (atomic).");
        System.out.println("Nó bao gồm 3 bước: Đọc giá trị -> Cộng 1 -> Ghi lại.");
        System.out.println("Khi nhiều luồng cùng 'Đọc' một giá trị cũ, chúng sẽ 'Ghi đè' kết quả của nhau.");
    }

    // Hàm tăng giá trị có dùng synchronized
    private static synchronized void incrementSync() {
        syncCounter++;
    }
}
