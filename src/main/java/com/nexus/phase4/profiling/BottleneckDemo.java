package com.nexus.phase4.profiling;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Issue #9: Performance Bottleneck Identification with JFR
 * 
 * Chương trình giả lập một server xử lý các loại request khác nhau.
 * Trong đó có chứa các "nút thắt cổ chai" cố ý để phân tích bằng JFR.
 */
public class BottleneckDemo {
    private static final Random random = new Random();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- Bottleneck Demo starting ---");
        System.out.println("[INFO] JFR is recording in the background...");
        System.out.println("[INFO] Press Ctrl+C to stop and save the recording.");

        while (true) {
            processRequests();
            Thread.sleep(10); // Giảm tải một chút để CPU không bị treo hoàn toàn
        }
    }

    private static void processRequests() {
        int type = random.nextInt(3);
        switch (type) {
            case 0:
                // Tác vụ bình thường
                fastTask();
                break;
            case 1:
                // Nút thắt cổ chai 1: Hot Method (Tính toán nặng không cần thiết)
                heavyCalculationTask();
                break;
            case 2:
                // Nút thắt cổ chai 2: Allocation Pressure (Tạo quá nhiều object rác)
                hugeAllocationTask();
                break;
        }
    }

    private static void fastTask() {
        double result = Math.sqrt(random.nextDouble());
        if (result < 0) System.out.print(""); // Ngăn JIT tối ưu hóa bỏ qua biến
    }

    private static void heavyCalculationTask() {
        // Giả lập một hàm tiêu tốn nhiều CPU
        long sum = 0;
        for (int i = 0; i < 1_000_000; i++) {
            sum += (long) Math.pow(i, 2);
        }
        if (sum == -1) System.out.print(""); // Ngăn JIT tối ưu hóa bỏ qua vòng lặp
    }

    private static void hugeAllocationTask() {
        // Giả lập việc tạo nhiều object tạm thời gây áp lực lên GC
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(new String("Garbage data " + i + " - " + random.nextLong()));
        }
        // List này sẽ bị thu hồi ngay sau khi hàm kết thúc
    }
}
