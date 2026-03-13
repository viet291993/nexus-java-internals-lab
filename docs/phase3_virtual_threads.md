# Báo cáo Phân tích: Virtual Threads & Scalability (Issue #7)

## 1. Khái niệm Virtual Threads (Java 21+)
Virtual Threads (luồng ảo) là các luồng có trọng lượng cực nhẹ, được quản lý bởi Java Runtime thay vì hệ điều hành (OS). 

### Carrier Threads vs Virtual Threads
- **Carrier Threads (Luồng nền tảng)**: Là các Platform Threads thực thụ của OS. JVM quản lý một pool các carrier threads này (thường bằng số nhân CPU).
- **Virtual Threads (Luồng ảo)**: "Chạy" bên trên carrier threads. Khi một luồng ảo gặp các thao tác chặn (Blocking I/O, sleep), JVM sẽ "treo" luồng ảo đó và giải phóng carrier thread để chạy luồng ảo khác.

## 2. So sánh Kiến trúc (Architecture Comparison)

### A. Mô hình Truyền thống (Platform Threads - 1:1)
Mỗi luồng Java tương ứng trực tiếp với một luồng của Hệ điều hành (OS Thread).

```mermaid
graph TD
    subgraph "Java Application"
        T1[Java Thread 1]
        T2[Java Thread 2]
        T3[Java Thread 3]
    end

    subgraph "OS / Kernel"
        OT1[OS Thread 1]
        OT2[OS Thread 2]
        OT3[OS Thread 3]
    end

    T1 --- OT1
    T2 --- OT2
    T3 --- OT3
    
    note2[Hạn chế: Tốn RAM ~1MB/thread <br/> Chi phí Context Switch cao]
```

### B. Mô hình Hiện đại (Virtual Threads - M:N)
Nhiều luồng ảo (M) chạy trên một số ít luồng nền tảng (N).

```mermaid
graph TD
    subgraph "Java Virtual Threads (Hàng triệu luồng)"
        VT1(Virtual Thread 1)
        VT2(Virtual Thread 2)
        VT3(Virtual Thread 3)
        VT4(Virtual Thread 4)
        VT5(Virtual Thread 5)
    end

    subgraph "Carrier Threads (Luồng nền tảng - Cố định)"
        PT1[Carrier Thread A]
        PT2[Carrier Thread B]
    end

    VT1 -.->|Mounted| PT1
    VT2 -.->|Mounted| PT1
    VT3 -.->|Mounted| PT2
    VT5 -.->|Mounted| PT2
    
    VT4 -.->|Waiting/Blocked| Heap[(Java Heap)]

    style VT4 fill:#f9f,stroke:#333,stroke-dasharray: 5 5
    note1[Ưu điểm: Siêu nhẹ, tự động giải phóng <br/> Luồng nền tảng khi chờ I/O]
```

## 2. Kết quả Thí nghiệm (10,000 Tasks)
Qua thử nghiệm thực tế trên hệ thống với 10,000 tasks (mỗi task sleep 1s):

| Đặc điểm | Platform Threads (Pool 1,000) | Virtual Threads (Per-task) |
| :--- | :--- | :--- |
| **Thời gian hoàn thành** | **10,222 ms** (~10.2 giây) | **1,289 ms** (~1.3 giây) |
| **Chi phí luồng** | Phải chia thành 10 đợt xử lý | Xử lý gần như song song hoàn toàn |
| **Khả năng mở rộng** | Bị giới hạn bởi pool size | Hàng vạn luồng ảo cùng chạy |
| **Hiệu quả (Throughput)** | Thấp (do chờ đợi đồng bộ) | Rất cao (khai thác tối đa thời gian chờ) |

## 3. Tại sao Virtual Threads scale nhanh hơn?
1. **Trọng lượng nhẹ**: Stack của Virtual Thread được lưu trữ trong Heap và có thể thay đổi kích thước linh hoạt, thay vì cấp phát cố định 1MB như Platform Thread.
2. **Non-blocking at OS level**: Khi `Thread.sleep()` được gọi trong Virtual Thread, nó không làm treo luồng OS. JVM chỉ đơn giản là đổi context cho luồng ảo khác làm việc.
3. **Chi phí Context Switch thấp**: Việc đổi context giữa các luồng ảo diễn ra trong không gian người dùng (User space), nhanh hơn nhiều so với việc kernel thực hiện context switch cho Platform Threads.

## 4. Kết luận
Virtual Threads không sinh ra để chạy nhanh hơn (về tốc độ CPU), mà để mang lại **độ lợi (throughput)** cao hơn cho ứng dụng vốn dành phần lớn thời gian chờ đợi I/O (như Web Server).
