# TRƯỜNG ĐẠI HỌC MỞ HÀ NỘI - KHOA CÔNG NGHỆ THÔNG TIN
## BÀI TẬP LỚN MÔN: ĐẢM BẢO CHẤT LƯỢNG PHẦN MỀM (SQA)
### ĐỀ TÀI: PHẦN MỀM QUẢN LÝ SỔ SÁCH THƯ VIỆN MINI (HỆ THỐNG LIBMANAGE)

---

### THÔNG TIN DỰ ÁN & BÊN NGHIỆM THU
* **Học phần:** Đảm bảo chất lượng phần mềm (Software Quality Assurance)
* **Giảng viên hướng dẫn môn học:** ThS. Trần Thị Hồng Oanh
* **Khách hàng nghiệm thu (Thủ thư trưởng):** **ThS. Cao Phương Thảo** - Cán bộ quản lý thư viện mini Khoa CNTT
* **Học kỳ thực hiện:** Học kỳ I – Năm học 2026 - 2027 (Tháng 10/2026)
* **Mô hình kiến trúc:** Java Web MVC (Model - View - Controller) nguyên khối

---

### DANH SÁCH NHÓM SINH VIÊN THỰC HIỆN (07 THÀNH VIÊN)

| Họ và tên | Lớp | Mã sinh viên | Vai trò dự án |
| :--- | :---: | :---: | :--- |
| **Lại Trần Hiếu** | 2410A03KS | **24A1001D0159** | **Trưởng nhóm / Trưởng ban SQA** |
| Chu Việt Anh | 2410A03KS | 24A1001D0004 | Thành viên nhóm phát triển & SQA |
| Nguyễn Ngọc Ánh | 2410A06 | 24A1001D0044 | Thành viên nhóm phát triển & SQA |
| Lê Thị Hường | 2410A06KS | 24A1001D0203 | Thành viên nhóm phát triển & SQA |
| Trần Minh Khang | 2410A06 | 24A1001D0218 | Thành viên nhóm phát triển & SQA |
| Hoàng Quốc Khánh | 2410A03 | 24A1001D0223 | Thành viên nhóm phát triển & SQA |
| Phạm Thị Khánh | 2410A07 | 24A1001D0227 | Thành viên nhóm phát triển & SQA |

---

### DANH MỤC CÁC TỆP NGUỒN CỐT LÕI BÀN GIAO

1. **`PhanMemThuVienLibManage.java`**
   - Máy chủ Java Web MVC độc lập (tích hợp sẵn `HttpServer` chạy cổng `3000`).
   - Xử lý toàn bộ logic nghiệp vụ bàn trực: kiểm tra hạn mức mượn tối đa 3 cuốn sách, tính hạn trả 14 ngày, tự động tính tiền phạt quá hạn 2.000 đồng/ngày.
   - Cơ sở dữ liệu quan hệ chuẩn hóa 3NF (3 bảng: `DocGia`, `Sach`, `MuonTra`).
   - Tốc độ phản hồi dưới 1 giây (dưới 0.1s trong bộ nhớ), phòng chống 100% tiêm mã SQL qua PreparedStatement.

2. **`Ung_Dung_Thu_Vien_LibManage_Chay_Truc_Tiep.html`**
   - Giao diện bảng điều khiển Dashboard hiện đại, thiết kế màu sắc chuyên nghiệp.
   - Tuân thủ nghiêm ngặt quy tắc không quá 3 lần bấm chuột cho mỗi nghiệp vụ mượn/trả.
   - Có thể nhấp đúp mở chạy trực tiếp trên bất kỳ trình duyệt nào mà không cần cài đặt phức tạp.

3. **`Chay_Phan_Mem.bat`**
   - Tệp kịch bản tự động hóa 1-click cho người chấm bài: Tự động biên dịch `javac`, khởi động máy chủ `java` và mở trình duyệt web đến cổng `http://localhost:3000`.

---

### HƯỚNG DẪN KHỞI CHẠY PHẦN MỀM

#### Cách 1: Khởi chạy 1 nhấp đúp (Dễ nhất cho Giảng viên & Bạn đọc)
- Nhấp đúp vào tệp **`Chay_Phan_Mem.bat`**.
- Hệ thống sẽ tự động biên dịch, bật máy chủ và mở ứng dụng trên trình duyệt web.

#### Cách 2: Khởi chạy thủ công từ dòng lệnh
```bash
# 1. Biên dịch mã nguồn Java
javac -encoding UTF-8 PhanMemThuVienLibManage.java

# 2. Khởi động máy chủ dịch vụ
java -Dfile.encoding=UTF-8 PhanMemThuVienLibManage

# 3. Mở trình duyệt web truy cập địa chỉ:
http://localhost:3000
```

---
*Bản quyền mã nguồn thuộc về Nhóm SQA FIT HOU - Trường Đại học Mở Hà Nội (2026 - 2027)*
