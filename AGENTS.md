# Hướng dẫn cho AI agents

## Bối cảnh dự án

Đây là **Student Management System** full-stack, khởi tạo ngày 20-08-2026.

- Backend: Java 17, Spring Boot, Gradle, Spring Web, Spring Data JPA, Bean Validation, MySQL.
- Frontend: React, Vite, JavaScript, Axios.
- Database phát triển: MySQL 8.4 qua Docker Compose.
- Mục tiêu hiện tại đã hoàn thành phần khởi tạo: CRUD sinh viên, tìm kiếm theo tên, phân trang, validation, CORS, exception handling, UI quản trị và test cơ bản.

Đọc `README.md` trước khi thay đổi kiến trúc, cách chạy hoặc cấu hình dự án.

## Trạng thái Git hiện tại

- Branch làm việc: `feature/initial-project`.
- Remote: `origin` = `https://github.com/chanhtran235/ai_agent_demo1.git`.
- Branch đã được push và đang theo dõi `origin/feature/initial-project`.
- Commit nền tảng hiện tại: `314a2be feat: add student management system`.
- Không có Pull Request; không có merge vào `main`.

Luôn kiểm tra `git status --short --branch` trước khi bắt đầu. Không chuyển branch, commit, push, tạo PR hoặc merge nếu người dùng chưa yêu cầu rõ ràng trong phiên hiện tại.

## Cấu trúc chính

```text
backend/
  src/main/java/com/studentmanagement/
    config/ controller/ dto/ entity/ exception/ repository/ service/
  src/test/                         # Service unit tests và MockMvc controller tests
  gradlew, gradlew.bat              # Luôn dùng Gradle Wrapper
frontend/
  src/api/students.js               # Axios API client
  src/components/                   # StudentForm và Pagination
  src/App.jsx                       # UI quản trị chính
docker-compose.yml                  # MySQL development database
.env.example                        # Chỉ placeholder, không chứa secret thật
README.md                           # Tài liệu vận hành và API
```

## Backend

- Base API: `/api/students`.
- Endpoints: list pageable, get by id, `GET /search?name=...`, create, update, delete.
- `Student` gồm: `id`, `studentCode`, `fullName`, `email`, `phone`, `dateOfBirth`, `address`.
- `studentCode` và `email` là unique.
- CORS chỉ cho phép origin từ biến môi trường bắt buộc `FRONTEND_URL`; không hard-code origin frontend.
- `application.yml` dùng các biến `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`.
- `spring-dotenv` đã được thêm để hỗ trợ nạp `.env` cục bộ; vẫn không được tạo/commit `.env` chứa credential.
- Tests dùng H2 qua `backend/src/test/resources/application.yml`, tuyệt đối không yêu cầu MySQL đang chạy.

Chạy backend từ `backend/`:

```powershell
.\gradlew.bat test --no-daemon
.\gradlew.bat build --no-daemon
.\gradlew.bat bootRun
```

Trong môi trường Windows này, Gradle Wrapper được cấu hình để dùng cache cục bộ `backend/.gradle-user-home/`, đã được ignore.

## Frontend

- `VITE_API_BASE_URL` là base URL API; mặc định là API local.
- UI hỗ trợ list, search, pagination, thêm, sửa, xoá sinh viên.

Chạy frontend từ `frontend/`:

```powershell
npm.cmd install
npm.cmd test
npm.cmd run build
npm.cmd run dev
```

Trên PowerShell hiện tại, dùng `npm.cmd` thay vì `npm` vì execution policy có thể chặn `npm.ps1`.

## Database và secrets

- Dùng `docker compose --env-file .env up -d` để chạy MySQL local.
- Copy `.env.example` thành `.env` ở máy local và thay tất cả password placeholder.
- Không hard-code mật khẩu, token, private key hoặc real `.env`.
- `.gitignore` đã ignore `.env`, `node_modules`, Gradle cache và build output. Không thêm các artifact này vào Git.

## Quy tắc làm việc

1. Chỉ thay đổi file bên trong repository này.
2. Trước thay đổi lớn, nói ngắn gọn kế hoạch cho người dùng.
3. Dùng `apply_patch` cho chỉnh sửa file thông thường.
4. Sau thay đổi backend, chạy test/build phù hợp; sau thay đổi frontend, ít nhất chạy test hoặc build phù hợp.
5. Báo trung thực lỗi, warning quan trọng và phần chưa xác minh; không che giấu lỗi.
6. Không dùng MySQL thật để chạy test backend.
7. Kiểm tra secrets trước bất kỳ commit/push nào.
8. Không tạo PR hoặc merge nếu chưa được yêu cầu rõ.

## Ghi chú phiên làm việc trước

- Backend test suite đã pass: 5 tests (service và controller).
- Backend build và frontend test/build đã pass trước commit nền tảng.
- Backend test đã được chạy lại sau khi thêm `spring-dotenv` và pass.
- Cảnh báo LF/CRLF khi Git stage trên Windows là cảnh báo line ending, không phải lỗi build.
