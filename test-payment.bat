@echo off
echo Testing Payment API...
echo.

echo Test 1: Valid payment request
curl -i -X POST -H "Content-Type: application/json" -H "Authorization: Bearer valid-token" -d "{\"paymentId\":\"PAY123\",\"amount\":100.50,\"currency\":\"USD\",\"customerId\":\"CUST001\",\"paymentMethod\":\"CREDIT_CARD\",\"description\":\"Test payment\"}" http://localhost:8080/api/payments
echo.
echo.

echo Test 2: Invalid payment request (missing required fields)
curl -i -X POST -H "Content-Type: application/json" -H "Authorization: Bearer valid-token" -d "{\"description\":\"Test payment\"}" http://localhost:8080/api/payments
echo.
echo.

echo Test 3: Payment with null values
curl -i -X POST -H "Content-Type: application/json" -H "Authorization: Bearer valid-token" -d "{\"paymentId\":null,\"amount\":null,\"currency\":null,\"customerId\":null}" http://localhost:8080/api/payments
echo.