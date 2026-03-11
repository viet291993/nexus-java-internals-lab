package com.nexus.phase1;

/**
 * Issue #1: Khởi tạo Project & JIT Compilation Analysis
 * Mục tiêu: Hiểu cách JIT (Just-In-Time) Compiler tối ưu hóa các hot methods.
 */
public class JitAnalysis {

    public static void main(String[] args) {
        System.out.println("--- Bắt đầu JIT Analysis ---");
        
        // Chạy vòng lặp lớn để "hâm nóng" JVM (Warm-up)
        // Khi một method được gọi đủ nhiều (threshold), JIT sẽ compile nó sang mã máy.
        long start = System.currentTimeMillis();
        
        int iterations = 1_000_000;
        int count = 0;
        
        for (int i = 0; i < iterations; i++) {
            if (isPrime(i)) {
                count++;
            }
            
            // In ra định kỳ để thấy tiến trình (không nên in quá nhiều vì làm chậm JIT)
            if (i % 200_000 == 0) {
                System.out.println("Đang xử lý đến số: " + i);
            }
        }
        
        long end = System.currentTimeMillis();
        System.out.println("--- Hoàn thành ---");
        System.out.println("Số lượng số nguyên tố tìm thấy: " + count);
        System.out.println("Thời gian thực hiện: " + (end - start) + "ms");
    }

    /**
     * Hàm kiểm tra số nguyên tố - Một tác vụ tính toán nặng.
     */
    public static boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        
        for (int i = 5; i * i <= n; i = i + 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
}
