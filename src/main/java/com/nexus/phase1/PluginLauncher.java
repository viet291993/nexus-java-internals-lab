package com.nexus.phase1;

public class PluginLauncher {
    public static void main(String[] args) {
        String pluginDir = "resources/plugins/out";
        String className = "com.nexus.phase1.SecretPlugin";

        System.out.println("--- Khởi động Plugin Loader ---");
        System.out.println("Thư mục tìm kiếm: " + pluginDir);

        try {
            // Khởi tạo CustomClassLoader trỏ tới thư mục chứa file .class
            CustomClassLoader loader = new CustomClassLoader(pluginDir);

            // Nạp class
            Class<?> pluginClass = loader.loadClass(className);
            System.out.println("Đã nạp class: " + pluginClass.getName());
            System.out.println("ClassLoader của class này là: " + pluginClass.getClassLoader());

            // Tạo instance và ép kiểu sang Interface Plugin
            // Lưu ý: Cả Main App và Plugin phải dùng chung Interface được nạp bởi AppClassLoader
            Object instance = pluginClass.getDeclaredConstructor().newInstance();
            
            if (instance instanceof Plugin) {
                Plugin plugin = (Plugin) instance;
                System.out.println("Phiên bản Plugin: " + plugin.getVersion());
                plugin.execute();
            } else {
                System.err.println("Class nạp được không kế thừa interface Plugin!");
            }

        } catch (Exception e) {
            System.err.println("Lỗi khi nạp plugin: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
