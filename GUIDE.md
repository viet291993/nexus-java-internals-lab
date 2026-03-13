# Hướng dẫn Thực hành (Practice Guide)

Tài liệu này ghi lại các nguyên tắc và quy trình (Workflow) để bạn thực hiện dự án **Nexus: Java Internals Lab** một cách hiệu quả nhất.

## 🔄 Quy trình làm việc với Git (Workflow)

Để rèn luyện kỹ năng như một Senior Developer, bạn nên tuân thủ quy trình sau cho mỗi Issue:

1.  **Tạo nhánh theo Giai đoạn (Phase Branch):**
    *   Mỗi Phase lớn (ví dụ Phase 3) sẽ được thực hiện trên một nhánh chính để quản lý tập trung các Issue liên quan:
    ```bash
    git checkout -b phase/3-concurrency
    ```

2.  **Quản lý nhiều Issue trong một Phase:**
    *   Bạn thực hiện lần lượt các Issue (#6, #7, #8...) trên cùng nhánh Phase này.
    *   Mỗi khi xong một Issue, hãy commit với chuẩn Conventional Commits kèm số Issue tương ứng.

3.  **Commit Code chuẩn (Conventional Commits):**
    *   Sử dụng tiền tố `feat:`, `fix:`, `docs:`, hoặc `chore:` kèm theo số Issue.
    ```bash
    git add .
    git commit -m "feat: implement prime calculation for JIT analysis #1"
    ```

4.  **Tài liệu phân tích (Documentation):**
    *   Tất cả báo cáo phân tích trong thư mục `docs/` **bắt buộc phải có sơ đồ minh họa** (sử dụng Mermaid hoặc hình ảnh) để trực quan hóa kiến thức.

5.  **Đẩy lên GitHub (Push) và Tạo Pull Request (PR):**
    *   Đẩy nhánh Phase lên GitHub: `git push origin phase/3-concurrency`.
    *   Tạo PR để merge vào `main`. 
    *   **Mẹo:** Viết mô tả PR về những kiến thức bạn vừa khám phá được (ví dụ: giải thích tại sao một hàm lại được JIT compile).

## 🛠️ Câu lệnh "Cheat Sheet" cho Internals

Lưu lại các lệnh quan trọng bạn sẽ dùng xuyên suốt dự án:

### Phase 1: JVM & Bytecode
*   **Xem JIT hoạt động:** `java -XX:+PrintCompilation -cp target/classes com.nexus.Main`
*   **Đọc Bytecode:** `javap -c -p -v MyClass.class`

### Phase 2: Memory & GC
*   **Bật log GC:** `java -Xlog:gc*:file=logs/gc.log -Xms64m -Xmx64m MyClass`
*   **Heap Dump (khi có OOM):** `-XX:+HeapDumpOnOutOfMemoryError`

### Phase 4: Profiling
*   **Trích xuất Thread Dump:** `jstack <PID>` hoặc `jcmd <PID> Thread.print`
*   **Java Flight Recorder:** `java -XX:StartFlightRecording=filename=recording.jfr MyClass`

## 📁 Cấu trúc thư mục Khuyên dùng

```text
nexus-java-internals-lab/
├── src/
│   └── main/
│       └── java/
│           └── com/nexus/
│               ├── phase1/      # JVM, ClassLoader, JIT
│               ├── phase2/      # Memory Leak, GC Logs
│               ├── phase3/      # Thread Pool, Virtual Threads
│               ├── phase4/      # Deadlock, Profiling
│               ├── phase5/      # NIO, Zero-copy
│               ├── phase6/      # CPU Optimization, JNI, JIT Inlining
│               └── phase7/      # Off-heap Memory Management
├── docs/                        # Lưu các file báo cáo, log phân tích
└── resources/                   # Lưu các file .class ngoại vi cho CustomClassLoader
```

---

## 🛠️ Câu lệnh "Cheat Sheet" cho Internals (Nâng cao)

### Phase 5: High-Performance I/O
*   **Buffer Direct Allocation**: `ByteBuffer.allocateDirect(size)`
*   **Zero-copy Transfer**: `channel.transferTo(position, count, targetChannel)`

### Phase 6: Low-Level Optimization
*   **Xem JIT Inlining**: `-XX:+UnlockDiagnosticVMOptions -XX:+PrintInlining`
*   **Trình biên dịch C (Windows)**: `gcc -shared -o mylib.dll mylib.c` (Cần MinGW)

### Phase 7: Memory Manager
*   **Direct Buffer Monitoring**: `jcmd <PID> VM.native_memory summary` (Cần `-XX:NativeMemoryTracking=summary`)
*   **Check GC Pause (Off-heap focus)**: So sánh log GC khi dùng Heap vs Off-heap.

## 🧠 Deep Dive Notes (Lưu ý chuyên sâu cho Phase 5-7)

### Phase 5: High-Performance I/O
- **Selector Management**: Cần đặc biệt chú ý xử lý `SelectionKey`. Việc quên `iterator.remove()` sau khi xử lý sự kiện là nguyên nhân phổ biến gây ra rò rỉ bộ nhớ hoặc lặp vô hạn.
- **Zero-copy & mmap**: Khi thực hiện Issue #11, hãy tìm hiểu về `MappedByteBuffer`. Đây là cầu nối giữa FileChannel và bộ nhớ, cho phép truyền dữ liệu trực tiếp từ Disk sang Network Card (kernel-to-kernel) mà không qua User Space.

### Phase 6: Low-Level Optimization
- **JMH (Java Microbenchmark Harness)**: Bắt buộc sử dụng JMH cho Issue #12. Việc đo thời gian bằng `System.nanoTime()` thường bị sai lệch do JIT Compiler thực hiện Dead Code Elimination hoặc Loop Unrolling.
- **Cache Line Awareness**: Hiểu rằng CPU nạp dữ liệu theo khối 64-byte. Nếu hai biến nằm sát nhau, chúng sẽ tranh chấp cache (False Sharing) làm giảm hiệu năng đa luồng trầm trọng.

### Phase 7: Off-heap Memory Manager
- **Manual Control**: Đây là vùng đất "không bảo hiểm". Bạn phải tự gọi `free` hoặc quản lý vòng đời của `DirectBuffer`. 
- **The Trade-off**: Đánh đổi sự an toàn của GC lấy độ trễ (Latency) cực thấp. Phù hợp cho các hệ thống Big Data Cache hàng Terabyte mà vẫn muốn giữ Pause Time của GC ở mức mili giây.

---
*Chúc bạn có những giờ phút "vọc vạch" JVM thật thú vị!*
