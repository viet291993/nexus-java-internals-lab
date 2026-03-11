package com.nexus.phase2;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Issue #4: GC Comparison & Configuration Analysis
 * Mục tiêu: Tạo áp lực bộ nhớ liên tục để quan sát cách các bộ GC (G1, ZGC) hoạt động.
 * Chúng ta sẽ tạo ra các object "sống ngắn" (short-lived) để GC phải làm việc vất vả.
 */
public class GcPressureDemo {

    public static void main(String[] args) {
        System.out.println("--- Bắt đầu tạo áp lực GC ---");
        System.out.println("JVM: " + System.getProperty("java.version"));
        
        // Queue này đóng vai trò giữ một lượng object nhất định để mô phỏng bộ nhớ đang dùng
        Queue<byte[]> memoryMover = new LinkedList<>();
        int maxSize = 40; // Giữ khoảng 40MB trong bộ nhớ

        try {
            for (int i = 0; i < 10000; i++) {
                // Tạo rác: 1MB mỗi lần
                byte[] garbage = new byte[1024 * 1024];
                memoryMover.add(garbage);
                
                // Khi vượt quá 40MB, ta bỏ bớt object cũ nhất (cho GC dọn dẹp)
                if (memoryMover.size() > maxSize) {
                    memoryMover.poll();
                }

                if (i % 100 == 0) {
                    System.out.println("Đã luân chuyển tổng cộng: " + i + " MB");
                }
                
                // Nghỉ cực ngắn để tăng áp lực luân chuyển
                Thread.sleep(5);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("--- Kết thúc Demo ---");
    }
}
