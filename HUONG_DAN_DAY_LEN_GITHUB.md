# HƯỚNG DẪN ĐẨY MÃ NGUỒN PHẦN MỀM LÊN GITHUB
### BÀI TẬP LỚN MÔN: ĐẢM BẢO CHẤT LƯỢNG PHẦN MỀM (SQA - FIT HOU)
**Dự án:** Hệ thống Quản lý Sổ sách Thư viện Mini (LibManage)

---

Tài liệu này hướng dẫn bạn 3 cách đẩy mã nguồn lên GitHub để lấy đường link nộp bài và đưa vào Báo cáo Bài Tập Lớn:

---

## CÁCH 1: DÙNG CÔNG CỤ TỰ ĐỘNG 1-CLICK (KHUYÊN DÙNG NHẤT)

Nhóm đã tạo sẵn tệp kịch bản tự động `setup_git_and_push.bat` ngay trong thư mục này.

### Bước 1: Tạo Repository mới trên GitHub
1. Mở trình duyệt web và truy cập: [https://github.com/new](https://github.com/new).
2. Đăng nhập tài khoản GitHub của bạn.
3. Tại ô **Repository name**, nhập:
   ```text
   BTL_SQA_LibManage_FIT_HOU
   ```
4. Chọn chế độ: **Public** (Công khai, để thầy cô chấm bài có thể truy cập xem mã nguồn).
5. **LƯU Ý:** Không tích chọn bất kỳ ô nào trong mục *"Initialize this repository with"* (Không tích README, không tích .gitignore).
6. Nhấp nút xanh **Create repository**.
7. Sao chép lại đường link HTTPS của repo bạn vừa tạo (dạng: `https://github.com/username/BTL_SQA_LibManage_FIT_HOU.git`).

### Bước 2: Chạy kịch bản tự động
1. Nhấp đúp chuột vào tệp: **`setup_git_and_push.bat`**.
2. Kịch bản sẽ tự động:
   * Kiểm tra máy tính (nếu chưa cài Git sẽ tự động tải và cài qua winget).
   * Tự động khởi tạo Git và chia nhỏ mã nguồn thành 8 commits chuẩn Conventional Commits theo từng tính năng nghiệp vụ.
   * Khi hiện câu hỏi: *"Dán đường link Repository GitHub của bạn"*, bạn chỉ cần nhấp chuột phải để dán đường link đã copy ở Bước 1 và nhấn **Enter**.
3. Quá trình tải mã nguồn lên GitHub sẽ hoàn tất trong vài giây!

---

## CÁCH 2: TẢI TRỰC TIẾP QUA TRANG WEB GITHUB (KHÔNG CẦN CÀI ĐẶT GIT)

Nếu bạn không muốn cài đặt công cụ Git trên máy tính, bạn có thể tải mã nguồn lên trực tiếp qua giao diện web của GitHub:

1. Vào [https://github.com/new](https://github.com/new) để tạo repository mới với tên `BTL_SQA_LibManage_FIT_HOU` (chọn Public).
2. Sau khi tạo xong, ở trang hướng dẫn đầu tiên, bạn sẽ thấy dòng chữ:
   > *"...or create a new repository on the command line... or **uploading an existing file**"*
3. Bạn nhấp vào liên kết xanh **uploading an existing file**.
4. Mở thư mục `6. Source code` trên máy tính, kéo thả các file sau vào trình duyệt:
   * `PhanMemThuVienLibManage.java`
   * `Ung_Dung_Thu_Vien_LibManage_Chay_Truc_Tiep.html`
   * `Chay_Phan_Mem.bat`
   * `.gitignore`
   * `HUONG_DAN_DAY_LEN_GITHUB.md`
5. Cuộn xuống dưới, tại ô **Commit changes**, bạn nhập:
   ```text
   release: Hoan thien ma nguon phan mem LibManage FIT HOU
   ```
6. Bấm nút xanh **Commit changes**.
7. Toàn bộ mã nguồn đã có mặt trên GitHub! Sao chép link thanh địa chỉ trình duyệt để lấy đường link nộp bài.

---

## CÁCH 3: DÙNG PHẦN MỀM ĐỒ HỌA GITHUB DESKTOP

1. Tải và cài đặt phần mềm [GitHub Desktop](https://desktop.github.com/).
2. Đăng nhập tài khoản GitHub trên GitHub Desktop.
3. Chọn menu **File** -> **Add Local Repository...** -> Trỏ đến thư mục `6. Source code` (hoặc thư mục `BAI_TAP_LON_SQA_FIT_HOU`).
4. Bấm **Create a repository** nếu được hỏi.
5. Nhập mô tả commit và bấm **Commit to main**.
6. Bấm nút **Publish repository** ở góc trên bên phải để đưa lên tài khoản GitHub của bạn.

---

## SAU KHI ĐẨY CODE LÊN GITHUB: CẬP NHẬT LINK VÀO BÁO CÁO BTL

1. Mở file `1. Báo Cáo BTL/danh_sach_link_google_docs.json`.
2. Thay thế đường link repository của bạn vào 2 dòng:
   * `"SRC-JAVA"`: `https://github.com/username/repo/blob/main/PhanMemThuVienLibManage.java`
   * `"SRC-HTML"`: `https://github.com/username/repo/blob/main/Ung_Dung_Thu_Vien_LibManage_Chay_Truc_Tiep.html`
3. Chạy lệnh: `py cap_nhat_link_google_docs.py` để toàn bộ link trong báo cáo Word và Markdown được đồng bộ ngay lập tức!
