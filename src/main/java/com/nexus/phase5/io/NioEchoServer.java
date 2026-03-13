package com.nexus.phase5.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

/**
 * Issue #10: High-Performance Echo Server with NIO Selector
 */
public class NioEchoServer {
    private static final int START_PORT = 9999;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) {
        int port = START_PORT;
        
        try (Selector selector = Selector.open();
             ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            
            // Tự động tìm cổng trống
            while (port < START_PORT + 10) {
                try {
                    serverChannel.bind(new InetSocketAddress(port));
                    break;
                } catch (IOException e) {
                    System.out.println("[INFO] Port " + port + " is busy, trying " + (port + 1) + "...");
                    port++;
                }
            }

            serverChannel.configureBlocking(false);
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);

            // Ghi cổng vào file để LoadTester biết đường tìm
            Files.writeString(Paths.get("out/server.port"), String.valueOf(port));
            
            final int finalPort = port;
            // Shutdown Hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\n[SHUTDOWN] Cleaning up NIO Server resources...");
                try {
                    Files.deleteIfExists(Paths.get("out/server.port"));
                } catch (IOException ignore) {}
            }));

            System.out.println("--- NIO Echo Server starting on port " + finalPort + " ---");
            System.out.println("[INFO] Server is ready. Waiting for connections...");
            System.out.println("[HINT] Press Ctrl+C to stop the server.");

            while (selector.isOpen()) {
                if (selector.select(1000) == 0) continue;

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> iter = selectedKeys.iterator();

                while (iter.hasNext()) {
                    SelectionKey key = iter.next();
                    iter.remove();

                    if (key.isAcceptable()) {
                        handleAccept(serverChannel, selector);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[ERROR] Server error: " + e.getMessage());
        }
    }

    private static void handleAccept(ServerSocketChannel serverChannel, Selector selector) throws IOException {
        SocketChannel clientChannel = serverChannel.accept();
        if (clientChannel != null) {
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
            System.out.println("[ACCEPT] New connection from: " + clientChannel.getRemoteAddress());
            System.out.println("[MONITOR] Active Threads: " + Thread.activeCount());
        }
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        
        try {
            int bytesRead = clientChannel.read(buffer);
            if (bytesRead == -1) {
                disconnect(key, clientChannel);
                return;
            }

            if (bytesRead > 0) {
                buffer.flip();
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                System.out.println("[READ] From " + clientChannel.getRemoteAddress() + ": " + new String(data).trim());
                
                buffer.rewind(); // Quay lại để write đúng dữ liệu
                clientChannel.write(buffer);
                buffer.clear();
            }
        } catch (IOException e) {
            disconnect(key, clientChannel);
        }
    }

    private static void disconnect(SelectionKey key, SocketChannel channel) throws IOException {
        System.out.println("[CLOSE] Client disconnected: " + channel.getRemoteAddress());
        key.cancel();
        channel.close();
    }
}
