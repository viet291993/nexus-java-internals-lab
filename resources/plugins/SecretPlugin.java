package com.nexus.phase1;

public class SecretPlugin implements Plugin {
    @Override
    public void execute() {
        System.out.println("============================================");
        System.out.println("  [Plugin] Đã nạp thành công SecretPlugin!  ");
        System.out.println("  Logic này được nạp từ bên ngoài Classpath.");
        System.out.println("============================================");
    }

    @Override
    public String getVersion() {
        return "1.0.0-PRO";
    }
}
