package com.nexus.phase1;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * CustomClassLoader để nạp các file .class từ một thư mục bên ngoài.
 */
public class CustomClassLoader extends ClassLoader {
    private String directory;

    public CustomClassLoader(String directory) {
        this.directory = directory;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] b = loadClassFromFile(name);
        return defineClass(name, b, 0, b.length);
    }

    private byte[] loadClassFromFile(String fileName) {
        // Chuyển đổi package name (com.nexus.phase1.HelloPlugin) thành đường dẫn file (HelloPlugin.class)
        // Trong bài này tớ giả định file nằm trực tiếp trong thư mục directory.
        String path = directory + File.separator + fileName.replace('.', File.separatorChar) + ".class";
        
        try (InputStream inputStream = new FileInputStream(path);
             ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            
            int nextValue;
            while ((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }
            return byteStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Không tìm thấy hoặc không đọc được file: " + path, e);
        }
    }
}
