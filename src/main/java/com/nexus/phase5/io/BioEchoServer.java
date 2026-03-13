package com.nexus.phase5.io;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Minh họa Blocking I/O (BIO) truyền thống.
 * Cơ chế: Mỗi kết nối mới (client) sẽ cần một Thread riêng biệt để xử lý.
 */
public class BioEchoServer {
    private static final int START_PORT = 7777;

    public static void main(String[] args) {
        int port = START_PORT;
        ServerSocket serverSocket = null;

        // Tự động tìm cổng trống
        while (port < START_PORT + 10) {
            try {
                serverSocket = new ServerSocket(port);
                break;
            } catch (IOException e) {
                System.out.println("[INFO] Port " + port + " is busy, trying " + (port + 1) + "...");
                port++;
            }
        }

        if (serverSocket == null) {
            System.err.println("[ERROR] Could not find any free port!");
            return;
        }

        final int finalPort = port;
        // Sử dụng try-with-resources để đảm bảo đóng socket
        try (ServerSocket ss = serverSocket) {
            System.out.println("--- BIO Echo Server starting on port " + finalPort + " ---");
            System.out.println("[HINT] Press Ctrl+C to stop the server gracefully.");

            // Shutdown Hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n[SHUTDOWN] Stopping BIO Server...");
            }));

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket clientSocket = ss.accept();
                    System.out.println("[ACCEPT] New client connected: " + clientSocket.getRemoteSocketAddress());
                    
                    Thread clientThread = new Thread(() -> handleClient(clientSocket));
                    clientThread.setDaemon(true);
                    clientThread.start();

                    // MONITORING
                    System.out.println("[MONITOR] Active Threads: " + Thread.activeCount());
                } catch (IOException e) {
                    if (ss.isClosed()) break;
                    System.err.println("[ERROR] Accept error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("[INFO] Server socket closed.");
        }
    }

    private static void handleClient(Socket socket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("[READ] From " + socket.getRemoteSocketAddress() + ": " + inputLine);
                out.println("Echo: " + inputLine);
            }
            System.out.println("[CLOSE] Client disconnected: " + socket.getRemoteSocketAddress());
        } catch (IOException e) {
            // Client disconnected
        } finally {
            try { 
                if (!socket.isClosed()) socket.close(); 
            } catch (IOException ignore) {}
        }
    }
}
