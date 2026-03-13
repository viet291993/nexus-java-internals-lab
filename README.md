# Nexus: Java Internals Lab

Dự án nghiên cứu sâu về cơ chế vận hành của JVM, Memory và Concurrency dựa trên dự án thực hành xử lý tác vụ nền bằng Core Java (không dùng framework).

## 🏆 Project Summary Report (Final)

Dự án đã hoàn thành cả 4 giai đoạn nghiên cứu với các kết quả thực nghiệm nổi bật:

### 🔬 Phase 1: JVM Architecture & Bytecode
- **JIT Optimization**: Quan sát thành công quá trình Tiered Compilation (Biên dịch phân tầng) từ Tier 0 đến Tier 4.
- **Dynamic Loading**: Triển khai `CustomClassLoader` hỗ trợ nạp Plugin động mà không cần restart ứng dụng.
- **Bytecode**: Giải mã thành công cơ chế hoạt động của `Operand Stack` thông qua tập lệnh JVM cơ bản.
- 📂 [Báo cáo Bytecode](docs/phase1_bytecode_analysis.md) | [Báo cáo JIT](docs/phase1_jit_analysis.md)

### 🧠 Phase 2: Memory & Garbage Collection
- **Memory Safety**: Mô phỏng thành công lỗi `OutOfMemoryError` và xác định được rò rỉ bộ nhớ do biến `static`.
- **GC Performance**: Thử nghiệm cho thấy **ZGC** vượt trội với độ trễ (Pause Time) cực thấp (~0.04ms) so với G1GC.
- 📂 [Báo cáo GC Comparison](docs/phase2_gc_comparison.md) | [Báo cáo Memory Leak](docs/phase2_memory_leak.md)

### ⚙️ Phase 3: Concurrency & Virtual Threads
- **Race Condition**: Chứng minh sự mất mát dữ liệu khi không đồng bộ và fix lỗi bằng `AtomicInteger` (CAS).
- **Custom Pooling**: Xây dựng thành công hệ thống tái sử dụng luồng (Worker Thread Reuse) giúp tối ưu tài nguyên.
- **Java 21 Scalability**: **Virtual Threads** cho thấy tốc độ xử lý nhanh gấp **8 lần** đối với các tác vụ I/O blocking (1.3s vs 10.2s).
- 📂 [Báo cáo Virtual Threads](docs/phase3_virtual_threads.md) | [Báo cáo ThreadPool](docs/phase3_custom_threadpool.md)

### 🔍 Phase 4: Profiling & Debugging
- **Deadlock Resolution**: Phân tích Thread Dump bằng `jstack` và xác định chuẩn xác vị trí "khóa chết" giữa các luồng.
- **Flight Recorder (JFR)**: Sử dụng JFR và JMC để xác định "Hot Methods" chiếm **80% CPU** và vùng nhớ Heap răng cưa đặc trưng.
- 📂 [Báo cáo Deadlock](docs/phase4_deadlock_analysis.md) | [Báo cáo JFR Profiling](docs/phase4_jfr_analysis.md)

---

## 🚀 Cách chạy các bài Lab

Để chạy bất kỳ bài lab nào, bạn chỉ cần thực thi tệp tin trung tâm ở thư mục gốc:

```cmd
nexus.bat
```
Sau đó, hãy chọn số tương ứng với Issue bạn muốn thực hiện từ Menu hiện ra.

> [!TIP]
> **[GUIDE.md](GUIDE.md)**: Chứa quy trình Git, Cheat Sheet các câu lệnh JVM quan trọng bạn cần dùng.

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
- Java Mission Control (JMC) & Java Flight Recorder (JFR)

## 📁 Cấu trúc thư mục
- `/src/main/java/com/nexus/phase1-4`: Mã nguồn thực hành cho từng giai đoạn.
- `/docs`: Nhật ký, báo cáo phân tích và hình ảnh thực nghiệm.
- `/docs/screenshots`: Ảnh chụp màn hình từ JMC và các công cụ giám sát.
