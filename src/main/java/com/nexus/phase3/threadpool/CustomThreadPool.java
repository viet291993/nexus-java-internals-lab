package com.nexus.phase3.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Issue #6: Custom ThreadPool Implementation with BlockingQueue
 * 
 * Một ThreadPool tự viết từ đầu để hiểu cơ chế hoạt động của Worker Threads.
 */
public class CustomThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final List<WorkerThread> workers;
    private volatile boolean isShutdown = false;

    public CustomThreadPool(int poolSize) {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.workers = new ArrayList<>(poolSize);

        // Khởi tạo các Worker Threads dựa trên poolSize
        for (int i = 0; i < poolSize; i++) {
            WorkerThread worker = new WorkerThread("CustomWorker-" + i);
            workers.add(worker);
            worker.start();
        }
    }

    /**
     * Nhận job (Runnable) và đưa vào hàng chờ.
     */
    public void execute(Runnable task) {
        if (isShutdown) {
            throw new IllegalStateException("ThreadPool has been shut down.");
        }
        try {
            taskQueue.put(task); // Chặn nếu hàng đợi đầy (nếu dùng giới hạn)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Dừng ThreadPool.
     */
    public void shutdown() {
        isShutdown = true;
        for (WorkerThread worker : workers) {
            worker.interrupt(); // Đánh thức các thread đang đợi để kết thúc
        }
    }

    /**
     * WorkerThread: Trái tim của ThreadPool.
     * Nhiệm vụ duy nhất là lấy job từ queue và chạy nó lặp đi lặp lại.
     */
    private class WorkerThread extends Thread {
        public WorkerThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            // Giải thích: Worker Thread không kết thúc sau khi xong một job
            // mà nằm trong vòng lặp while(true) để chờ job tiếp theo.
            while (!isShutdown || !taskQueue.isEmpty()) {
                try {
                    // Chờ và lấy job từ hàng đợi. put/take của BlockingQueue
                    // là mấu chốt để thread "ngủ" khi không có việc.
                    Runnable task = taskQueue.take();
                    System.out.println(getName() + " is executing task...");
                    task.run();
                    System.out.println(getName() + " finished task.");
                } catch (InterruptedException e) {
                    if (isShutdown) {
                        break; // Thoát vòng lặp nếu pool đã shutdown
                    }
                }
            }
            System.out.println(getName() + " stopped.");
        }
    }
}
