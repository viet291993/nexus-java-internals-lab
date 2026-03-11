# Nexus: Java Internals Lab

Dự án nghiên cứu sâu về cơ chế vận hành của JVM, Memory và Concurrency dựa trên dự án thực hành xử lý tác vụ nền bằng Core Java (không dùng framework).

## 🚀 Lộ trình Thực hiện (Roadmap)

> [!TIP]
> **[GUIDE.md](file:///f:/nexus-java-internals-lab/GUIDE.md)**: Chứa quy trình Git, Cheat Sheet các câu lệnh JVM quan trọng bạn cần dùng.

### Phase 1: Khám phá kiến trúc JVM
Hiểu cách Java biến thành mã máy và cách JVM quản lý class.
- `#1`: Khởi tạo Project & JIT Compilation.
- `#2`: Custom ClassLoader & Bytecode Analysis.

### Phase 2: Quản lý bộ nhớ (JMM & GC)
Hiểu sâu về Stack, Heap và cơ chế Garbage Collection.
- `#3`: Mô phỏng Memory Leak & OutOfMemoryError.
- `#4`: Cấu hình GC & Phân tích GC Log.

### Phase 3: Đồng bộ hóa & Đa luồng (Concurrency)
Xử lý tác vụ song song, tránh Race Condition và tối ưu Performance.
- `#5`: Race Condition & Atomic Synchronization.
- `#6`: Xây dựng Custom ThreadPool.
- `#7`: Virtual Threads (Java 21+).

### Phase 4: Giám sát & Bắt lỗi (Profiling)
Kỹ năng xử lý khi ứng dụng gặp sự cố.
- `#8`: Deadlock Analysis & Thread Dump.
- `#9`: Performance Bottleneck with JFR & JMeter.

## 🛠️ Công cụ & Công nghệ
- Core Java (JDK 21+ tối ưu cho Virtual Threads)
- Git & GitHub Issues
- `javap`, `jstack`, `jcmd`, VisualVM
- Apache JMeter

## 📁 Cấu trúc thư mục (Đề xuất)
- `/src/main/java/com/nexus/phase1`
- `/src/main/java/com/nexus/phase2`
- `/src/main/java/com/nexus/phase3`
- `/src/main/java/com/nexus/phase4`
- `/resources/external-classes` (Dành cho Phase 1)
- `/docs` (Lưu log, báo cáo phân tích)
