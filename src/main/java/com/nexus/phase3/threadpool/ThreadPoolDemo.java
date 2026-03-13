package com.nexus.phase3.threadpool;

/**
 * Lớp chạy demo để kiểm chứng CustomThreadPool.
 */
public class ThreadPoolDemo {
    public static void main(String[] args) throws InterruptedException {
        // Tạo Pool với 3 workers
        CustomThreadPool pool = new CustomThreadPool(3);

        // Gửi 10 công việc vào Pool
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            pool.execute(() -> {
                try {
                    System.out.println("Processing Task #" + taskId + " on thread: " + Thread.currentThread().getName());
                    Thread.sleep(1000); // Giả lập công việc tốn thời gian
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Chờ một chút để xem các worker xử lý
        Thread.sleep(5000);

        System.out.println("Shutting down pool...");
        pool.shutdown();
    }
}
