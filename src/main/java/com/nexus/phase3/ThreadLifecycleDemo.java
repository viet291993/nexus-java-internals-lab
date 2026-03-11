package com.nexus.phase3;

/**
 * Issue #5: Thread Lifecycle (Vòng đời của Thread)
 * Mục tiêu: Quan sát các trạng thái của một Thread từ khi sinh ra đến khi kết thúc.
 */
public class ThreadLifecycleDemo {

    public static void main(String[] args) throws InterruptedException {
        // 1. Trạng thái NEW: Thread được tạo nhưng chưa gọi start()
        Thread worker = new Thread(() -> {
            try {
                // Giả lập công việc tốn thời gian
                Thread.sleep(2000); 
                
                // Trạng thái sau khi ngủ dậy sẽ là RUNNABLE
                synchronized (ThreadLifecycleDemo.class) {
                    // Chờ ở đây để demo trạng thái BLOCKED nếu cần
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("1. Sau khi khởi tạo (NEW): " + worker.getState());

        // 2. Trạng thái RUNNABLE: Sau khi gọi start()
        worker.start();
        System.out.println("2. Sau khi start() (RUNNABLE): " + worker.getState());

        // Nghỉ một chút để thread worker kịp đi ngủ (sleep)
        Thread.sleep(500);

        // 3. Trạng thái TIMED_WAITING: Đang sleep hoặc chờ có thời hạn
        System.out.println("3. Đang trong Thread.sleep() (TIMED_WAITING): " + worker.getState());

        // Đợi thread worker hoàn thành
        worker.join();

        // 4. Trạng thái TERMINATED: Công việc đã xong
        System.out.println("4. Sau khi hoàn thành (TERMINATED): " + worker.getState());
    }
}
