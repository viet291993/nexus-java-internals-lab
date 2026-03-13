package com.nexus.phase5.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Client giả lập tải lớn để kiểm tra NioEchoServer.
 */
public class NioLoadTester {
    private static final int DEFAULT_PORT = 9999;
    private static final int CONNECTION_COUNT = 100;

    public static void main(String[] args) throws InterruptedException {
        int port = DEFAULT_PORT;
        try {
            // Đọc cổng thực tế mà server đang lắng nghe
            String portStr = Files.readString(Paths.get("out/server.port")).trim();
            port = Integer.parseInt(portStr);
        } catch (Exception e) {
            System.out.println("[INFO] Could not read out/server.port, using default: " + DEFAULT_PORT);
        }

        System.out.println("--- NIO Load Tester starting (Port " + port + ") ---");
        List<SocketChannel> clients = new ArrayList<>();

        try {
            // 1. Mở hàng loạt kết nối
            for (int i = 0; i < CONNECTION_COUNT; i++) {
                SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", port));
                client.configureBlocking(false);
                clients.add(client);
                if (i % 20 == 0) System.out.println("[INFO] Opened " + i + " connections...");
            }

            System.out.println("[SUCCESS] Total connections opened: " + clients.size());
            Thread.sleep(1000);

            // 2. Gửi dữ liệu đồng loạt
            ByteBuffer buffer = ByteBuffer.allocate(128);
            for (int i = 0; i < clients.size(); i++) {
                String msg = "Hello from Client #" + i;
                buffer.clear();
                buffer.put(msg.getBytes());
                buffer.flip();
                clients.get(i).write(buffer);
            }

            System.out.println("[INFO] Sent messages from all clients. Waiting for Echo responses...");
            Thread.sleep(2000);

            // 3. Đọc dữ liệu phản hổi (Echo)
            int echoCount = 0;
            for (SocketChannel client : clients) {
                buffer.clear();
                int read = client.read(buffer);
                if (read > 0) {
                    echoCount++;
                }
            }
            System.out.println("[RESULT] Received " + echoCount + " echo responses back from server.");

            // 4. Đóng toàn bộ
            for (SocketChannel client : clients) {
                client.close();
            }
            System.out.println("[INFO] All connections closed.");

        } catch (IOException e) {
            System.err.println("[ERROR] Lỗi trong quá trình load test: " + e.getMessage());
        }
    }
}
