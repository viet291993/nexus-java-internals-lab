package com.nexus.phase4.deadlock;

/**
 * Issue #8: Deadlock Analysis & Thread Dump Exploration
 * 
 * Chương trình mô phỏng tình trạng Deadlock (Khóa chết) giữa hai luồng.
 */
public class DeadlockDemo {
    private static final Object LockA = new Object();
    private static final Object LockB = new Object();

    public static void main(String[] args) {
        System.out.println("--- Bắt đầu mô phỏng Deadlock ---");
        System.out.println("[Gợi ý] Hãy sử dụng lệnh 'jstack <PID>' để tìm ra nguyên nhân.");

        Thread thread1 = new Thread(() -> {
            synchronized (LockA) {
                System.out.println("Thread 1: Đang giữ LockA...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("Thread 1: Đang chờ LockB...");
                synchronized (LockB) {
                    System.out.println("Thread 1: Đã lấy được LockB!");
                }
            }
        }, "Deadlock-Thread-1");

        Thread thread2 = new Thread(() -> {
            synchronized (LockB) {
                System.out.println("Thread 2: Đang giữ LockB...");
                try { Thread.sleep(100); } catch (InterruptedException e) {}
                System.out.println("Thread 2: Đang chờ LockA...");
                synchronized (LockA) {
                    System.out.println("Thread 2: Đã lấy được LockA!");
                }
            }
        }, "Deadlock-Thread-2");

        thread1.start();
        thread2.start();
    }
}
