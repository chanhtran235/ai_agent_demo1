# Student Management System

A full-stack administration application for managing student records. The backend exposes a validated REST API and the React frontend provides listing, search, pagination, and student create, edit, and delete workflows.

## Architecture

```
frontend/  React + Vite + Axios
    |
    v
backend/   Spring Boot REST API
    |
    v
MySQL      Docker Compose development database
```

The backend follows `controller`, `service`, `repository`, `entity`, `dto`, `exception`, and `config` packages. Tests use an isolated H2 database configuration and do not require MySQL.

## Requirements

- Java 17
- Node.js 20+ and npm
- Docker Desktop (for the development MySQL database)

## Installation and database setup

1. Copy `.env.example` to `.env` and replace the password placeholder values.
2. Start MySQL:

   ```powershell
   docker compose --env-file .env up -d
   ```

3. Export the backend environment variables from `.env` in your shell, including `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, and `FRONTEND_URL`.

No real `.env` file is committed. Docker Compose requires `MYSQL_PASSWORD` and `MYSQL_ROOT_PASSWORD` to be supplied.

## Backend startup

From `backend/`:

```powershell
./gradlew.bat bootRun
```

The server listens on `http://localhost:8080`. `FRONTEND_URL` is required and is used as the only allowed CORS origin.

## Frontend startup

From `frontend/`:

```powershell
npm install
npm run dev
```

Set `VITE_API_BASE_URL` if the API is not at `http://localhost:8080/api`.

## API endpoints

| Method | Endpoint | Description |
| --- | --- | --- |
| GET | `/api/students?page=0&size=10&sort=fullName,asc` | List students with pagination |
| GET | `/api/students/{id}` | Get a student |
| GET | `/api/students/search?name=...&page=0&size=10` | Search by full name |
| POST | `/api/students` | Create a student |
| PUT | `/api/students/{id}` | Update a student |
| DELETE | `/api/students/{id}` | Delete a student |

Create and update request body:

```json
{
  "studentCode": "STU-001",
  "fullName": "Ada Lovelace",
  "email": "ada@example.com",
  "phone": "+1 555 0100",
  "dateOfBirth": "2000-12-10",
  "address": "123 Example Street"
}
```

## Testing

```powershell
cd backend
./gradlew.bat test
./gradlew.bat build

cd ../frontend
npm test
npm run build
```

## Environment variables

| Variable | Purpose |
| --- | --- |
| `DB_HOST`, `DB_PORT`, `DB_NAME` | Backend MySQL connection location |
| `DB_USERNAME`, `DB_PASSWORD` | Backend MySQL credentials |
| `FRONTEND_URL` | Required allowed CORS origin |
| `MYSQL_DATABASE`, `MYSQL_USER`, `MYSQL_PASSWORD`, `MYSQL_ROOT_PASSWORD` | Docker Compose MySQL initialization |
| `VITE_API_BASE_URL` | Frontend REST API base URL |
