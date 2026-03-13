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
│               └── phase4/      # Deadlock, Profiling
├── docs/                        # Lưu các file báo cáo, log phân tích
└── resources/                   # Lưu các file .class ngoại vi cho CustomClassLoader
```

---
*Chúc bạn có những giờ phút "vọc vạch" JVM thật thú vị!*
