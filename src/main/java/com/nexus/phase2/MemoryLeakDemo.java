package com.nexus.phase2;

import java.util.ArrayList;
import java.util.List;

/**
 * Issue #3: Memory Leak & OutOfMemoryError Simulation
 * Mục tiêu: Hiểu cơ chế rò rỉ bộ nhớ (Memory Leak) khi GC không thể thu hồi được object.
 */
public class MemoryLeakDemo {

    // Đây là "thủ phạm": Một static list sẽ giữ tham chiếu đến mọi object được add vào.
    // Vì List này là static, nó sẽ tồn tại suốt vòng đời của ứng dụng.
    // GC (Garbage Collector) sẽ không bao giờ dám thu hồi các object trong này.
    private static final List<byte[]> leakedStorage = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("--- Bắt đầu mô phỏng Memory Leak ---");
        System.out.println("Cấu hình JVM hiện tại (Heap size): " + Runtime.getRuntime().maxMemory() / 1024 / 1024 + " MB");
        
        try {
            int i = 0;
            while (true) {
                // Tạo một mảng byte 1MB và ném vào list
                byte[] chunk = new byte[1024 * 1024]; // 1MB
                leakedStorage.add(chunk);
                
                i++;
                if (i % 10 == 0) {
                    long freeMemory = Runtime.getRuntime().freeMemory() / 1024 / 1024;
                    System.out.println("Đã nhét: " + i + " MB | Bộ nhớ còn lại: " + freeMemory + " MB");
                }
                
                // Nghỉ một chút để bạn kịp nhìn log (thực tế leak có thể nhanh hơn)
                Thread.sleep(50);
            }
        } catch (OutOfMemoryError e) {
            System.err.println("\n!!! LỖI RỒI: " + e.getMessage());
            System.err.println("Ứng dụng bị sập vì đầy bộ nhớ Heap!");
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
