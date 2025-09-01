#!/bin/bash

echo "🚀 Starting BookByte Online Bookstore..."

echo "📚 Building Backend..."
cd backend
mvn clean install -DskipTests

echo "🌐 Starting Backend Server..."
mvn spring-boot:run &
BACKEND_PID=$!

echo "⏳ Waiting for backend to start..."
sleep 30

echo "📱 Installing Frontend Dependencies..."
cd ../frontend
npm install

echo "🌐 Starting Frontend Server..."
ng serve &
FRONTEND_PID=$!

echo "✅ BookByte is starting up!"
echo "📚 Backend: http://localhost:8080"
echo "🌐 Frontend: http://localhost:4200"
echo "🔑 Admin Login: admin@bookbyte.com / admin123"
echo ""
echo "Press Ctrl+C to stop both servers"

# Wait for user to stop
wait

# Cleanup
echo "🛑 Stopping servers..."
kill $BACKEND_PID $FRONTEND_PID 2>/dev/null
echo "✅ Servers stopped"