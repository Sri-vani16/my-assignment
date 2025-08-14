@echo off
echo Testing Security Configuration...
echo.

echo 1. Get token by login:
curl -i -X POST -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"password\"}" http://localhost:8080/auth/login
echo.
echo.

echo 2. Testing without token (should get 401):
curl -i http://localhost:8080/api/users
echo.
echo.

echo 3. Testing with valid token (should work):
curl -i -H "Authorization: Bearer valid-token" http://localhost:8080/api/users
echo.
echo.

echo 4. Testing non-API endpoint (should work without token):
curl -i http://localhost:8080/