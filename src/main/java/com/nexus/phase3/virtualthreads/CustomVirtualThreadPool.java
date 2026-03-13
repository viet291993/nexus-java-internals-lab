package com.nexus.phase3.virtualthreads;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Phiên bản CustomThreadPool đã được "chuyển đổi" sang Virtual Threads.
 * Thay vì Platform Threads, các Worker Threads bây giờ là Virtual Threads.
 */
public class CustomVirtualThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private volatile boolean isShutdown = false;

    public CustomVirtualThreadPool(int poolSize) {
        this.taskQueue = new LinkedBlockingQueue<>();

        // Khởi tạo các Virtual Worker Threads
        for (int i = 0; i < poolSize; i++) {
            int workerId = i;
            // Virtual threads rất nhẹ, chúng ta khởi tạo và chạy ngay lập tức.
            // Chúng được gắn vào các Carrier Threads bên dưới do JVM quản lý.
            Thread.ofVirtual()
                  .name("VirtualWorker-" + workerId)
                  .start(() -> workerLoop());
        }
    }

    public void execute(Runnable task) {
        if (isShutdown) throw new IllegalStateException("Pool shutdown");
        taskQueue.add(task);
    }

    private void workerLoop() {
        while (!isShutdown || !taskQueue.isEmpty()) {
            try {
                Runnable task = taskQueue.take();
                task.run();
            } catch (InterruptedException e) {
                if (isShutdown) break;
            }
        }
    }

    public void shutdown() {
        isShutdown = true;
    }
}
