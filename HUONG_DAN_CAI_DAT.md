# HƯỚNG DẪN CÀI ĐẶT VÀ CHẠY DỰ ÁN MEDICAL APPOINTMENT SYSTEM

Tài liệu này hướng dẫn chi tiết cách cài đặt môi trường, cấu hình cơ sở dữ liệu và chạy ứng dụng quản lý phòng khám (ClinicManagement).

## 1. YÊU CẦU HỆ THỐNG (PREREQUISITES)

Trước khi bắt đầu, hãy đảm bảo máy tính của bạn đã cài đặt các công cụ sau:

*   **Java Development Kit (JDK) 17**: Ứng dụng yêu cầu Java 17.
    *   Kiểm tra bằng lệnh: `java -version`
*   **Apache Maven**: Công cụ quản lý dự án và build. (Cần cài đặt global vì dự án không có sẵn wrapper `mvnw`).
    *   Kiểm tra bằng lệnh: `mvn -version`
*   **MySQL Server**: Cơ sở dữ liệu (Khuyên dùng MySQL 8.0 trở lên).
    *   Cổng mặc định: `3306`

## 2. CÀI ĐẶT CƠ SỞ DỮ LIỆU

1.  Mở công cụ quản lý MySQL (như MySQL Workbench, phpMyAdmin, hoặc DBeaver).
2.  Tạo một cơ sở dữ liệu mới (Schema) với tên chính xác là `clinic_db`.
    *   **Charset**: `utf8mb4` (để hỗ trợ tiếng Việt đầy đủ).
    *   **Collation**: `utf8mb4_unicode_ci`.

```sql
CREATE DATABASE clinic_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

*Lưu ý: Bạn không cần chạy script tạo bảng. Ứng dụng được cấu hình `ddl-auto: create`, nghĩa là nó sẽ tự động tạo bảng khi chạy lần đầu.*

## 3. CẤU HÌNH ỨNG DỤNG

File cấu hình nằm tại:
`ClinicManagement/backend/clinic/src/main/resources/application.yml`

Mở file này và kiểm tra thông tin kết nối database:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/clinic_db...
    username: root          # Tên đăng nhập mặc định
    password: "123456"      # MẬT KHẨU MẶC ĐỊNH
```

⚠️ **Quan trọng**: Nếu mật khẩu MySQL của bạn khác `123456`, hãy sửa lại dòng `password` trong file này.

## 4. CÁCH CHẠY ỨNG DỤNG

1.  Mở Command Prompt (CMD) hoặc Main Terminal.
2.  Di chuyển vào thư mục chứa code backend:
    ```cmd
    cd "Đường_dẫn_đến_thư_mục\ClinicManagement\backend\clinic"
    ```
3.  Chạy lệnh khởi động Spring Boot:
    ```cmd
    mvn spring-boot:run
    ```

Chờ cho đến khi thấy dòng log `Started ClinicApplication in ... seconds`.

## 5. SỬ DỤNG

Sau khi ứng dụng chạy thành công, truy cập trình duyệt:

*   **Trang chủ**: [http://localhost:8080](http://localhost:8080)

### Tài khoản Mặc định (Tự động tạo)

Hệ thống sẽ tự động tạo các tài khoản mẫu khi chạy lần đầu:

| Vai trò | Email (Tên đăng nhập) | Mật khẩu | Ghi chú |
| :--- | :--- | :--- | :--- |
| **Admin** | `admin@clinic.com` | `admin123` | Quản trị viên hệ thống |
| **Bác sĩ** | `doctor@clinic.com` | `123456` | Bác sĩ mẫu |
| **Bệnh nhân**| `patient@email.com` | *(Đăng ký mới)*| Bệnh nhân mẫu |

### Các Lỗi Thường Gặp

1.  **Lỗi kết nối Database**:
    *   Kiểm tra lại xem MySQL service đã chạy chưa.
    *   Kiểm tra lại username/password trong `application.yml`.
    *   Đảm bảo database `clinic_db` đã được tạo.

2.  **Lỗi Port 8080 used**:
    *   Tắt ứng dụng khác đang chiếm cổng 8080 hoặc đổi port trong `application.yml` (`server.port: 8081`).
