@echo off
chcp 65001 >nul
title KHỞI CHẠY HỆ THỐNG QUẢN LÝ THƯ VIỆN LIBMANAGE (JAVA WEB - FIT HOU)
color 0B

echo =====================================================================
echo    HỆ THỐNG QUẢN LÝ THƯ VIỆN MINI (LIBMANAGE) - BTL SQA FIT HOU
echo    CÔNG NGHỆ: JAVA WEB (MÔ HÌNH MVC) + HTML5 / CSS3
echo =====================================================================
echo.
echo  Đang kiểm tra môi trường Java trên máy tính...

where javac >nul 2>nul
if %ERRORLEVEL% neq 0 (
    echo [THÔNG BÁO] Không tìm thấy javac trong PATH. Đang kiểm tra Java runtime...
    where java >nul 2>nul
    if %ERRORLEVEL% neq 0 (
        echo [LỖI] Không tìm thấy Java trên máy tính.
        echo Bạn có thể mở trực tiếp file: Ung_Dung_Thu_Vien_LibManage_Chay_Truc_Tiep.html
        echo để sử dụng phần mềm trên trình duyệt mà không cần cài đặt gì thêm.
        echo.
        pause
        exit /b 1
    )
)

echo  Tìm thấy môi trường Java SE!
echo  Đang biên dịch mã nguồn Java: PhanMemThuVienLibManage.java...
javac -encoding UTF-8 "%~dp0PhanMemThuVienLibManage.java"
if %ERRORLEVEL% neq 0 (
    echo.
    echo [LỖI] Quá trình biên dịch Java không thành công!
    pause
    exit /b 1
)

echo  Biên dịch thành công!
echo  Đang khởi động Web Server Java tại cổng 3000 và tự động mở trình duyệt...
echo  (Nhấn phím Ctrl + C trong cửa sổ này để tắt máy chủ khi kết thúc)
echo.
echo  Địa chỉ: http://localhost:3000
echo =====================================================================

java -cp "%~dp0." PhanMemThuVienLibManage

if %ERRORLEVEL% neq 0 (
    echo.
    echo Phần mềm đã dừng hoặc gặp sự cố.
    pause
)
