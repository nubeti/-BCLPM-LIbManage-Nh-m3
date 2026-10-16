/**
 * DỰ ÁN BÀI TẬP LỚN MÔN ĐẢM BẢO CHẤT LƯỢNG PHẦN MỀM (SQA) - FIT HOU
 * ĐỀ TÀI: HỆ THỐNG QUẢN LÝ SỔ SÁCH THƯ VIỆN MINI (LIBMANAGE - JAVA WEB MVC)
 * 
 * NHÓM SINH VIÊN THỰC HIỆN:
 * 1. Lại Trần Hiếu     - 24A1001D0159 - Lớp 2410A03KS (Nhóm trưởng / Project Manager / SQA Lead)
 * 2. Chu Việt Anh      - 24A1001D0004 - Lớp 2410A03KS (Thiết kế Kiến trúc hệ thống & CSDL 3NF)
 * 3. Nguyễn Ngọc Ánh   - 24A1001D0044 - Lớp 2410A06   (Khảo sát & Phân tích BA)
 * 4. Lê Thị Hường      - 24A1001D0203 - Lớp 2410A06KS (Lập trình Backend Java Web)
 * 5. Trần Minh Khang   - 24A1001D0218 - Lớp 2410A06   (Lập trình Frontend HTML/CSS)
 * 6. Hoàng Quốc Khánh  - 24A1001D0223 - Lớp 2410A03   (Kiểm thử Hộp đen & Defect Log)
 * 7. Phạm Thị Khánh    - 24A1001D0227 - Lớp 2410A07   (Đảm bảo Chất lượng SQA & Tổng hợp)
 * 
 * KHÁCH HÀNG (THỦ THƯ TRƯỞNG): ThS. Cao Phương Thảo
 * GIẢNG VIÊN HƯỚNG DẪN: ThS. Trần Thị Hồng Oanh
 * NĂM HỌC: Học kỳ I – Năm học 2026 - 2027
 */
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.awt.Desktop;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * =============================================================================
 *   HỆ THỐNG QUẢN LÝ SỔ SÁCH THƯ VIỆN MINI (LIBMANAGE) - BẢN TỐI GIẢN CHUẨN SQA
 *   Môn học: Đảm bảo chất lượng phần mềm (SQA) - FIT HOU
 *   Công nghệ: Java Web MVC (Không phân quyền, tập trung nghiệp vụ mượn/trả)
 * =============================================================================
 * - Hệ thống thiết kế tối giản dành riêng cho Thủ thư ghi chép và theo dõi sổ sách.
 * - 3 Quy tắc kiểm thử cốt lõi:
 *    1. Mượn tối đa: 3 cuốn / bạn đọc (TC-02)
 *    2. Hạn mượn chuẩn: 14 ngày (TC-01)
 *    3. Phạt quá hạn: 2.000 VNĐ / ngày trễ (TC-05)
 */
public class PhanMemThuVienLibManage {

    public static final int PORT = 3000;
    public static final int MAX_BORROW_DAYS = 14;
    public static final int RENEW_DAYS = 7;
    public static final int MAX_BOOKS_PER_READER = 3;
    public static final int FINE_PER_DAY_VND = 2000;

    // =========================================================================
    // 1. MODEL: ĐƠN GIẢN CHỈ GỒM 3 ĐỐI TƯỢNG (SÁCH, ĐỘC GIẢ, SỔ MƯỢN TRẢ)
    // =========================================================================
    public static class Book {
        private int id;
        private String bookCode;
        private String title;
        private String author;
        private String category;
        private int totalCopies;
        private int availableCopies;
        private String location;

        public Book(int id, String bookCode, String title, String author, String category, int totalCopies, int availableCopies, String location) {
            this.id = id;
            this.bookCode = bookCode;
            this.title = title;
            this.author = author;
            this.category = category;
            this.totalCopies = totalCopies;
            this.availableCopies = availableCopies;
            this.location = location;
        }

        public int getId() { return id; }
        public String getBookCode() { return bookCode; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getCategory() { return category; }
        public int getTotalCopies() { return totalCopies; }
        public int getAvailableCopies() { return availableCopies; }
        public void setAvailableCopies(int availableCopies) { this.availableCopies = availableCopies; }
        public String getLocation() { return location; }
    }

    public static class Reader {
        private int id;
        private String readerCode;
        private String fullName;
        private String email;
        private String phone;
        private String department;

        public Reader(int id, String readerCode, String fullName, String email, String phone, String department) {
            this.id = id;
            this.readerCode = readerCode;
            this.fullName = fullName;
            this.email = email;
            this.phone = phone;
            this.department = department;
        }

        public int getId() { return id; }
        public String getReaderCode() { return readerCode; }
        public String getFullName() { return fullName; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getDepartment() { return department; }
    }

    public static class BorrowRecord {
        private int id;
        private int readerId;
        private int bookId;
        private String borrowDate;
        private String dueDate;
        private String returnDate;
        private int status; // 1: Đang mượn, 2: Đã trả, 3: Quá hạn
        private int fineAmount;
        private String note;

        public BorrowRecord(int id, int readerId, int bookId, String borrowDate, String dueDate, String returnDate, int status, int fineAmount, String note) {
            this.id = id;
            this.readerId = readerId;
            this.bookId = bookId;
            this.borrowDate = borrowDate;
            this.dueDate = dueDate;
            this.returnDate = returnDate;
            this.status = status;
            this.fineAmount = fineAmount;
            this.note = note;
        }

        public int getId() { return id; }
        public int getReaderId() { return readerId; }
        public int getBookId() { return bookId; }
        public String getBorrowDate() { return borrowDate; }
        public String getDueDate() { return dueDate; }
        public void setDueDate(String dueDate) { this.dueDate = dueDate; }
        public String getReturnDate() { return returnDate; }
        public void setReturnDate(String returnDate) { this.returnDate = returnDate; }
        public int getStatus() { return status; }
        public void setStatus(int status) { this.status = status; }
        public int getFineAmount() { return fineAmount; }
        public void setFineAmount(int fineAmount) { this.fineAmount = fineAmount; }
        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }
    }

    // =========================================================================
    // 2. SERVICE: XỬ LÝ NGHIỆP VỤ SỔ SÁCH & KIỂM THỬ ĐƠN GIẢN
    // =========================================================================
    public static class LibraryService {
        private static final LibraryService INSTANCE = new LibraryService();
        public static LibraryService getInstance() { return INSTANCE; }

        private final List<Book> books = new CopyOnWriteArrayList<>();
        private final List<Reader> readers = new CopyOnWriteArrayList<>();
        private final List<BorrowRecord> borrows = new CopyOnWriteArrayList<>();

        private final AtomicInteger bookSeq = new AtomicInteger(10);
        private final AtomicInteger readerSeq = new AtomicInteger(10);
        private final AtomicInteger borrowSeq = new AtomicInteger(10);

        private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        private LibraryService() {
            initSampleData();
        }

        public synchronized void initSampleData() {
            books.clear();
            readers.clear();
            borrows.clear();

            // 1. Kho sách ban đầu
            books.add(new Book(1, "BK001", "Nhập môn Đảm bảo chất lượng phần mềm", "Trần Thị Hồng Oanh", "Giáo trình", 10, 8, "Kệ A1 - Tầng 2"));
            books.add(new Book(2, "BK002", "Lập trình Web với Java và Spring Boot", "Nguyễn Khắc Chính", "Công nghệ Java", 8, 7, "Kệ B2 - Tầng 2"));
            books.add(new Book(3, "BK003", "Thiết kế cơ sở dữ liệu quan hệ với JDBC", "Nguyễn Văn Ba", "Giáo trình", 8, 8, "Kệ A2 - Tầng 2"));
            books.add(new Book(4, "BK004", "Clean Code - Nghệ thuật viết mã sạch", "Robert C. Martin", "Kỹ năng lập trình", 5, 5, "Kệ C1 - Tầng 3"));
            books.add(new Book(5, "BK005", "Kiểm thử phần mềm căn bản và nâng cao", "Dorothy Graham", "Kiểm thử SQA", 7, 7, "Kệ B1 - Tầng 2"));

            // 2. Danh sách bạn đọc ban đầu
            readers.add(new Reader(1, "SV2201", "Trần Văn Hùng", "hung.tv@hou.edu.vn", "0912345678", "CNTT K18"));
            readers.add(new Reader(2, "SV2202", "Lê Thị Thu Thảo", "thao.lt@hou.edu.vn", "0987654321", "KTPM K18"));
            readers.add(new Reader(3, "SV2203", "Phạm Hoàng Nam", "nam.ph@hou.edu.vn", "0933445566", "KHMT K19"));
            readers.add(new Reader(4, "GV1001", "ThS. Nguyễn Khắc Chính", "chinh.nk@hou.edu.vn", "0905123456", "Bộ môn CNPM"));

            // 3. Sổ mượn trả mẫu:
            // Phiếu 1: Quá hạn 6 ngày (Hạn ngày trừ 6) -> Tiền phạt 12.000 VNĐ (minh chứng TC-05)
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -20);
            String dMinus20 = df.format(cal.getTime());
            cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -6);
            String dMinus6 = df.format(cal.getTime());

            // Phiếu 2: Đang mượn bình thường (minh chứng TC-01)
            cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, -5);
            String dMinus5 = df.format(cal.getTime());
            cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_YEAR, 9);
            String dPlus9 = df.format(cal.getTime());

            borrows.add(new BorrowRecord(1, 1, 1, dMinus20, dMinus6, null, 3, 12000, "Quá hạn 6 ngày"));
            borrows.add(new BorrowRecord(2, 2, 2, dMinus5, dPlus9, null, 1, 0, "Đang mượn"));
        }

        public List<Book> getBooks() { return books; }
        public List<Reader> getReaders() { return readers; }
        public List<BorrowRecord> getBorrows() { 
            updateFines();
            return borrows; 
        }

        // Tự động cập nhật tiền phạt: Ngày trễ * 2.000 VNĐ
        public synchronized void updateFines() {
            Date today = new Date();
            for (BorrowRecord b : borrows) {
                if (b.getReturnDate() == null) {
                    try {
                        Date due = df.parse(b.getDueDate());
                        if (today.after(due)) {
                            long diff = today.getTime() - due.getTime();
                            int days = (int) (diff / (1000 * 60 * 60 * 24));
                            if (days > 0) {
                                b.setStatus(3); // Quá hạn
                                b.setFineAmount(days * FINE_PER_DAY_VND);
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
        }

        // 1. Nghiệp vụ Lập phiếu mượn (Kiểm thử TC-01, TC-02, TC-03)
        public synchronized String borrowBook(int readerId, int bookId) {
            // Kiểm tra hạn mức mượn tối đa 3 cuốn
            int count = 0;
            for (BorrowRecord b : borrows) {
                if (b.getReaderId() == readerId && b.getReturnDate() == null) {
                    count++;
                }
            }
            if (count >= MAX_BOOKS_PER_READER) {
                return "LỖI (TC-02): Bạn đọc đã mượn tối đa " + MAX_BOOKS_PER_READER + " cuốn sách! Vui lòng trả sách trước khi mượn thêm.";
            }

            // Kiểm tra tồn kho sách
            Book book = null;
            for (Book bk : books) {
                if (bk.getId() == bookId) {
                    book = bk;
                    break;
                }
            }
            if (book == null || book.getAvailableCopies() <= 0) {
                return "LỖI (TC-03): Sách đã hết bản lưu trong kho!";
            }

            // Lập phiếu mượn hạn 14 ngày
            Calendar cal = Calendar.getInstance();
            String bDate = df.format(cal.getTime());
            cal.add(Calendar.DAY_OF_YEAR, MAX_BORROW_DAYS);
            String dDate = df.format(cal.getTime());

            int newId = borrowSeq.incrementAndGet();
            borrows.add(0, new BorrowRecord(newId, readerId, bookId, bDate, dDate, null, 1, 0, "Đang mượn"));
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            return "SUCCESS";
        }

        // 2. Nghiệp vụ Trả sách & Thu phạt (Kiểm thử TC-04, TC-05)
        public synchronized String returnBook(int borrowId) {
            BorrowRecord br = null;
            for (BorrowRecord b : borrows) {
                if (b.getId() == borrowId) {
                    br = b;
                    break;
                }
            }
            if (br == null) return "Không tìm thấy phiếu mượn!";
            if (br.getReturnDate() != null) return "Sách đã được trả trước đó!";

            br.setReturnDate(df.format(new Date()));
            br.setStatus(2); // Đã trả

            // Hoàn lại 1 cuốn khả dụng vào kho
            for (Book bk : books) {
                if (bk.getId() == br.getBookId()) {
                    bk.setAvailableCopies(bk.getAvailableCopies() + 1);
                    break;
                }
            }
            return "SUCCESS";
        }

        // 3. Gia hạn thêm 7 ngày
        public synchronized String renewBook(int borrowId) {
            BorrowRecord br = null;
            for (BorrowRecord b : borrows) {
                if (b.getId() == borrowId) {
                    br = b;
                    break;
                }
            }
            if (br == null) return "Không tìm thấy phiếu mượn!";
            if (br.getStatus() == 3) {
                return "LỖI: Phiếu mượn đã bị quá hạn, không được gia hạn! Vui lòng nộp phạt và trả sách.";
            }

            try {
                Date due = df.parse(br.getDueDate());
                Calendar cal = Calendar.getInstance();
                cal.setTime(due);
                cal.add(Calendar.DAY_OF_YEAR, RENEW_DAYS);
                br.setDueDate(df.format(cal.getTime()));
                br.setNote("Đã gia hạn +7 ngày");
                return "SUCCESS";
            } catch (Exception e) {
                return "Lỗi ngày tháng: " + e.getMessage();
            }
        }

        // Thêm sách mới
        public synchronized String addBook(String code, String title, String author, String cat, int copies, String loc) {
            for (Book b : books) {
                if (b.getBookCode().equalsIgnoreCase(code)) {
                    return "Mã sách " + code + " đã tồn tại trong kho!";
                }
            }
            books.add(new Book(bookSeq.incrementAndGet(), code, title, author, cat, copies, copies, loc));
            return "SUCCESS";
        }

        // Thêm độc giả mới
        public synchronized String addReader(String code, String name, String email, String phone, String dept) {
            for (Reader r : readers) {
                if (r.getReaderCode().equalsIgnoreCase(code)) {
                    return "Mã thẻ " + code + " đã được cấp trước đó!";
                }
            }
            readers.add(new Reader(readerSeq.incrementAndGet(), code, name, email, phone, dept));
            return "SUCCESS";
        }
    }

    // =========================================================================
    // 3. HTTP CONTROLLER: ĐIỀU HƯỚNG VÀ TRẢ VỀ JSON / GIAO DIỆN
    // =========================================================================
    public static class AppHandler implements HttpHandler {
        private final LibraryService service = LibraryService.getInstance();

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if (path.equals("/") || path.equals("/index.html")) {
                byte[] bytes = getHtmlPage().getBytes(StandardCharsets.UTF_8);
                exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
                return;
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

            if (path.equals("/api/dashboard") && method.equalsIgnoreCase("GET")) {
                service.updateFines();
                long totalBooks = service.getBooks().size();
                long totalReaders = service.getReaders().size();
                long active = 0, overdue = 0, fines = 0;
                for (BorrowRecord b : service.getBorrows()) {
                    if (b.getReturnDate() == null) {
                        active++;
                        if (b.getStatus() == 3) overdue++;
                    }
                    fines += b.getFineAmount();
                }
                String json = String.format("{\"totalBooks\":%d,\"totalReaders\":%d,\"activeBorrows\":%d,\"overdueCount\":%d,\"totalFines\":%d}",
                        totalBooks, totalReaders, active, overdue, fines);
                sendJsonResponse(exchange, 200, json);
                return;
            }

            if (path.equals("/api/books") && method.equalsIgnoreCase("GET")) {
                StringBuilder sb = new StringBuilder("[");
                List<Book> list = service.getBooks();
                for (int i = 0; i < list.size(); i++) {
                    Book b = list.get(i);
                    sb.append(String.format("{\"id\":%d,\"book_code\":\"%s\",\"title\":\"%s\",\"author\":\"%s\",\"category\":\"%s\",\"total_copies\":%d,\"available_copies\":%d,\"location\":\"%s\"}",
                            b.getId(), esc(b.getBookCode()), esc(b.getTitle()), esc(b.getAuthor()), esc(b.getCategory()), b.getTotalCopies(), b.getAvailableCopies(), esc(b.getLocation())));
                    if (i < list.size() - 1) sb.append(",");
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
                return;
            }

            if (path.equals("/api/readers") && method.equalsIgnoreCase("GET")) {
                StringBuilder sb = new StringBuilder("[");
                List<Reader> list = service.getReaders();
                for (int i = 0; i < list.size(); i++) {
                    Reader r = list.get(i);
                    sb.append(String.format("{\"id\":%d,\"reader_code\":\"%s\",\"full_name\":\"%s\",\"email\":\"%s\",\"phone\":\"%s\",\"department\":\"%s\"}",
                            r.getId(), esc(r.getReaderCode()), esc(r.getFullName()), esc(r.getEmail()), esc(r.getPhone()), esc(r.getDepartment())));
                    if (i < list.size() - 1) sb.append(",");
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
                return;
            }

            if (path.equals("/api/borrowings") && method.equalsIgnoreCase("GET")) {
                StringBuilder sb = new StringBuilder("[");
                List<BorrowRecord> list = service.getBorrows();
                Map<Integer, Reader> rMap = new HashMap<>();
                for (Reader r : service.getReaders()) rMap.put(r.getId(), r);
                Map<Integer, Book> bMap = new HashMap<>();
                for (Book bk : service.getBooks()) bMap.put(bk.getId(), bk);

                for (int i = 0; i < list.size(); i++) {
                    BorrowRecord b = list.get(i);
                    Reader r = rMap.get(b.getReaderId());
                    Book bk = bMap.get(b.getBookId());

                    sb.append(String.format("{\"id\":%d,\"reader_name\":\"%s\",\"reader_code\":\"%s\",\"book_title\":\"%s\",\"book_code\":\"%s\",\"borrow_date\":\"%s\",\"due_date\":\"%s\",\"return_date\":%s,\"status\":%d,\"fine_amount\":%d,\"note\":\"%s\"}",
                            b.getId(), esc(r != null ? r.getFullName() : "N/A"), esc(r != null ? r.getReaderCode() : "N/A"),
                            esc(bk != null ? bk.getTitle() : "N/A"), esc(bk != null ? bk.getBookCode() : "N/A"),
                            b.getBorrowDate(), b.getDueDate(), (b.getReturnDate() == null ? "null" : "\"" + b.getReturnDate() + "\""),
                            b.getStatus(), b.getFineAmount(), esc(b.getNote())));
                    if (i < list.size() - 1) sb.append(",");
                }
                sb.append("]");
                sendJsonResponse(exchange, 200, sb.toString());
                return;
            }

            if (path.equals("/api/borrow") && method.equalsIgnoreCase("POST")) {
                Map<String, String> body = parseBody(exchange);
                int rId = Integer.parseInt(body.getOrDefault("reader_id", "0"));
                int bId = Integer.parseInt(body.getOrDefault("book_id", "0"));
                String res = service.borrowBook(rId, bId);
                if ("SUCCESS".equals(res)) {
                    sendJsonResponse(exchange, 200, "{\"success\":true}");
                } else {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"" + esc(res) + "\"}");
                }
                return;
            }

            if (path.equals("/api/return") && method.equalsIgnoreCase("POST")) {
                Map<String, String> body = parseBody(exchange);
                int id = Integer.parseInt(body.getOrDefault("borrow_id", "0"));
                String res = service.returnBook(id);
                if ("SUCCESS".equals(res)) {
                    sendJsonResponse(exchange, 200, "{\"success\":true}");
                } else {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"" + esc(res) + "\"}");
                }
                return;
            }

            if (path.equals("/api/renew") && method.equalsIgnoreCase("POST")) {
                Map<String, String> body = parseBody(exchange);
                int id = Integer.parseInt(body.getOrDefault("borrow_id", "0"));
                String res = service.renewBook(id);
                if ("SUCCESS".equals(res)) {
                    sendJsonResponse(exchange, 200, "{\"success\":true}");
                } else {
                    sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"" + esc(res) + "\"}");
                }
                return;
            }

            if (path.equals("/api/books/add") && method.equalsIgnoreCase("POST")) {
                Map<String, String> b = parseBody(exchange);
                String res = service.addBook(b.get("code"), b.get("title"), b.get("author"), b.get("category"), Integer.parseInt(b.getOrDefault("copies","1")), b.get("location"));
                if ("SUCCESS".equals(res)) sendJsonResponse(exchange, 200, "{\"success\":true}");
                else sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"" + esc(res) + "\"}");
                return;
            }

            if (path.equals("/api/readers/add") && method.equalsIgnoreCase("POST")) {
                Map<String, String> b = parseBody(exchange);
                String res = service.addReader(b.get("code"), b.get("name"), b.get("email"), b.get("phone"), b.get("department"));
                if ("SUCCESS".equals(res)) sendJsonResponse(exchange, 200, "{\"success\":true}");
                else sendJsonResponse(exchange, 400, "{\"success\":false,\"error\":\"" + esc(res) + "\"}");
                return;
            }

            sendJsonResponse(exchange, 404, "{\"error\":\"Not Found\"}");
        }

        private String esc(String s) {
            if (s == null) return "";
            return s.replace("\"", "\\\"").replace("\n", " ").replace("\r", "");
        }

        private void sendJsonResponse(HttpExchange exchange, int code, String json) throws IOException {
            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(code, bytes.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytes);
            os.close();
        }

        private Map<String, String> parseBody(HttpExchange exchange) throws IOException {
            InputStream is = exchange.getRequestBody();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int r;
            while ((r = is.read(buf)) != -1) baos.write(buf, 0, r);
            String body = baos.toString("UTF-8").trim();
            Map<String, String> map = new HashMap<>();
            if (body.startsWith("{") && body.endsWith("}")) {
                String inner = body.substring(1, body.length() - 1);
                for (String p : inner.split(",")) {
                    String[] kv = p.split(":", 2);
                    if (kv.length == 2) {
                        map.put(kv[0].trim().replaceAll("^\"|\"$", ""), kv[1].trim().replaceAll("^\"|\"$", ""));
                    }
                }
            }
            return map;
        }
    }

    // =========================================================================
    // 4. GIAO DIỆN WEB HTML5/CSS3 TỐI GIẢN - TRỰC QUAN ĐỂ THỬ NGHIỆM SQA
    // =========================================================================
        public static String getHtmlPage() {
        // Đọc trực tiếp từ file Ung_Dung_Thu_Vien_LibManage_Chay_Truc_Tiep.html cùng thư mục
        File f = new File("Ung_Dung_Thu_Vien_LibManage_Chay_Truc_Tiep.html");
        if (f.exists()) {
            try {
                byte[] bytes = java.nio.file.Files.readAllBytes(f.toPath());
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (Exception ignored) {}
        }
        File f2 = new File("BAI_TAP_LON_SQA_FIT_HOU/08_Code_Phan_Mem/Ung_Dung_Thu_Vien_LibManage_Chay_Truc_Tiep.html");
        if (f2.exists()) {
            try {
                byte[] bytes = java.nio.file.Files.readAllBytes(f2.toPath());
                return new String(bytes, StandardCharsets.UTF_8);
            } catch (Exception ignored) {}
        }
        return "<h2 style='text-align:center;margin-top:40px;'>Vui lòng mở file Ung_Dung_Thu_Vien_LibManage_Chay_Truc_Tiep.html</h2>";
    }

    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/", new AppHandler());
            server.setExecutor(null);
            server.start();

            String url = "http://localhost:" + PORT;
            System.out.println("=================================================================");
            System.out.println("  SỔ QUẢN LÝ THƯ VIỆN MINI LIBMANAGE ĐÃ KHỞI CHẠY THÀNH CÔNG!");
            System.out.println("  Địa chỉ: " + url);
            System.out.println("  Đang tự động mở trình duyệt...");
            System.out.println("  (Nhấn phím Ctrl + C trong cửa sổ này để dừng phần mềm)");
            System.out.println("=================================================================");

            try {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(url));
                }
            } catch (Exception ignored) {}

        } catch (Exception e) {
            System.err.println("Lỗi khởi động máy chủ: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
