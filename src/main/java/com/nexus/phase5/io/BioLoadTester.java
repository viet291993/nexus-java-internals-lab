package com.nexus.phase5.io;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Client để kiểm tra hiệu năng của BioEchoServer.
 * Mở hàng loạt luồng, mỗi luồng giữ một kết nối Socket.
 */
public class BioLoadTester {
    private static final int PORT = 7777; // Cổng mặc định của BIO
    private static final int THREAD_COUNT = 50; // Thử nghiệm với 50 luồng

    public static void main(String[] args) {
        System.out.println("--- BIO Load Tester starting (Port " + PORT + ") ---");
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int clientId = i;
            Thread t = new Thread(() -> {
                try (Socket socket = new Socket("localhost", PORT);
                     PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                     Scanner in = new Scanner(socket.getInputStream())) {
                    
                    String msg = "Hello from BioClient #" + clientId;
                    out.println(msg);
                    
                    if (in.hasNextLine()) {
                        String response = in.nextLine();
                        System.out.println("[CLIENT #" + clientId + "] Received: " + response);
                    }
                    
                    // Giữ kết nối một lúc để thấy số lượng Thread tăng lên trên Server
                    Thread.sleep(5000);
                    
                } catch (Exception e) {
                    System.err.println("[CLIENT #" + clientId + "] Error: " + e.getMessage());
                }
            });
            t.start();
            threads.add(t);
        }

        // Chờ tất cả kết thúc
        for (Thread t : threads) {
            try { t.join(); } catch (InterruptedException e) {}
        }
        
        System.out.println("--- BIO Load Test Finished ---");
    }
}
