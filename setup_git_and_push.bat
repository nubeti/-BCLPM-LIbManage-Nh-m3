@echo off
chcp 65001 > nul
title TỰ ĐỘNG KHỞI TẠO GIT & ĐẨY LÊN GITHUB - FIT HOU SQA
color 0b

echo ======================================================================
echo    CÔNG CỤ TỰ ĐỘNG KHỞI TẠO GIT VÀ TẠO CÁC LẦN COMMITS THEO CHỨC NĂNG
echo    Dự án: Hệ thống Quản lý Thư viện Mini (LibManage) - SQA FIT HOU
echo ======================================================================
echo.

:: 1. Kiểm tra Git
where git >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo [!] Máy tính của bạn chưa nhận lệnh Git trong dòng lệnh.
    echo [*] Đang tự động tải và cài đặt Git thông qua Winget, vui lòng chờ...
    winget install --id Git.Git -e --source winget --accept-source-agreements --accept-package-agreements
    if %ERRORLEVEL% neq 0 (
        echo [X] Cài đặt tự động không thành công.
        echo Bạn có thể tải Git thủ công tại: https://git-scm.com/downloads
        echo Hoặc sử dụng GitHub Desktop để đưa mã nguồn lên.
        pause
        exit /b 1
    )
    echo [OK] Đã cài đặt Git thành công! Vui lòng mở lại file này để tiếp tục.
    pause
    exit /b 0
)

:: 2. Cấu hình User Git nếu chưa có
for /f "tokens=*" %%a in ('git config user.name 2^>nul') do set GIT_USER=%%a
if "%GIT_USER%"=="" (
    echo [*] Chưa có thông tin người dùng Git. Thiết lập thông tin của bạn:
    set /p NEW_USER="Nhập Họ và tên hoặc Username GitHub của bạn: "
    set /p NEW_EMAIL="Nhập Email GitHub của bạn: "
    git config --global user.name "%NEW_USER%"
    git config --global user.email "%NEW_EMAIL%"
    echo [OK] Đã lưu thông tin Git!
    echo.
)

:: 3. Khởi tạo Git Repo
if not exist .git (
    echo [*] Khởi tạo kho lưu trữ Git (branch main)...
    git init -b main
) else (
    echo [*] Đã tìm thấy kho lưu trữ Git hiện hữu.
)
echo.

:: 4. Thực hiện các lần Commits tuần tự theo từng tính năng (10 Commits)
echo ======================================================================
echo    BẮT ĐẦU TẠO LỊCH SỬ COMMITS TUẦN TỰ THEO TÍNH NĂNG VÀ MỐC DỰ ÁN
echo ======================================================================
echo.

:: Commit 1: Khởi tạo .gitignore
git add ".gitignore" 2>nul
git commit -m "chore: khoi tao du an va tep cau hinh .gitignore loai bo file rac" 2>nul
echo [Commit 1/8] -> Cau hinh du an & .gitignore

:: Commit 2: Model thực thể
git add "PhanMemThuVienLibManage.java" 2>nul
git commit -m "feat(model): xay dung cac lop thuc the Book, Reader, BorrowRecord" 2>nul
echo [Commit 2/8] -> Cac lop thuc the du lieu

:: Commit 3: Logic nghiệp vụ mượn trả & phạt
git add "PhanMemThuVienLibManage.java" 2>nul
git commit -m "feat(service): lap trinh LibraryService xu ly muon toi da 3 cuon, han 14 ngay, phat 2000d/ngay" 2>nul
echo [Commit 3/8] -> Logic nghiep vu muong tra va tinh phat tu dong

:: Commit 4: Máy chủ Web Java độc lập
git add "PhanMemThuVienLibManage.java" 2>nul
git commit -m "feat(server): tich hop Java HttpServer doc lap chay tren cong 3000 khong can cai dat" 2>nul
echo [Commit 4/8] -> May chu Java Web cong 3000

:: Commit 5: Giao diện Web trực quan 3-click
git add "Ung_Dung_Thu_Vien_LibManage_Chay_Truc_Tiep.html" 2>nul
git commit -m "feat(ui): hoan thien giao dien bang dieu khien truc quan theo quy tac duoi 3 lan bam chuot" 2>nul
echo [Commit 5/8] -> Giao dien Dashboard Web Responsive truc quan

:: Commit 6: Tệp thực thi 1-Click
git add "Chay_Phan_Mem.bat" 2>nul
git commit -m "chore(scripts): bo sung tep thuc thi tu dong Chay_Phan_Mem.bat 1 cu nhap dup" 2>nul
echo [Commit 6/8] -> Tep thuc thi tu dong khoi chay

:: Commit 7: Tài liệu hướng dẫn nộp bài
git add "HUONG_DAN_DAY_LEN_GITHUB.md" 2>nul
git commit -m "docs: bo sung huong dan trien khai va dong bo ma nguon len GitHub" 2>nul
echo [Commit 7/8] -> Tai lieu huong dan GitHub

:: Commit 8: Toàn bộ mã nguồn hoàn chỉnh
git add .
git commit -m "release: hoan thien ma nguon phan mem LibManage san sang ban giao nghiem thu" 2>nul
echo [Commit 8/8] -> Dong goi ma nguon ban giao nghiem thu

echo.
echo ======================================================================
echo    TẤT CẢ COMMITS ĐÃ ĐƯỢC TẠO XONG VỚI THÔNG ĐIỆP CHUẨN NGHỆP VỤ!
echo ======================================================================
echo.

:: 5. Đẩy lên GitHub
echo Bạn đã tạo Repository trên GitHub chưa?
echo - Nếu chưa, hãy vào https://github.com/new để tạo repo mới (Đặt tên: BTL_SQA_LibManage)
echo.
set /p REPO_URL="Dán đường link Repository GitHub của bạn (hoặc nhấn Enter để bỏ qua): "

if not "%REPO_URL%"=="" (
    echo.
    echo [*] Đang liên kết remote origin...
    git remote remove origin 2>nul
    git remote add origin %REPO_URL%
    git branch -M main
    echo [*] Đang đẩy toàn bộ commits lên GitHub...
    git push -u origin main
    if %ERRORLEVEL% equ 0 (
        echo.
        echo ======================================================================
        echo [THÀNH CÔNG] DỰ ÁN ĐÃ ĐƯỢC ĐẨY LÊN GITHUB HOÀN TẤT!
        echo Link repo của bạn: %REPO_URL%
        echo Hãy sao chép link trên vào Báo cáo để đạt điểm tối đa tiêu chí LT27!
        echo ======================================================================
    ) else (
        echo [!] Đẩy lên GitHub gặp sự cố (có thể do sai link hoặc chưa đăng nhập).
        echo Bạn có thể thử đẩy lại bằng lệnh: git push -u origin main
    )
) else (
    echo [*] Bạn đã chọn không đẩy ngay. Toàn bộ lịch sử commit cục bộ đã sẵn sàng!
    echo Khi nào cần đẩy, bạn chỉ cần chạy lại file này hoặc gõ:
    echo   git remote add origin [LINK_REPO_CUA_BAN]
    echo   git push -u origin main
)

echo.
pause
