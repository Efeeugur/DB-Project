#!/bin/bash

# Art School Management System - Run Script

echo "🎨 Art School Management System"
echo "================================"
echo ""

# Check if compiled
if [ ! -d "out" ] || [ ! -f "out/Main.class" ]; then
    echo "⚠️  Project not compiled. Compiling..."
    javac -d out $(find src -name "*.java")
    if [ $? -ne 0 ]; then
        echo "❌ Compilation failed!"
        exit 1
    fi
    echo "✅ Compilation successful"
fi

# Copy resources
echo "📁 Copying resources..."
mkdir -p out/resources
cp -f src/resources/application.properties out/resources/ 2>/dev/null

# Check for PostgreSQL driver
if [ ! -f "postgresql.jar" ]; then
    echo "📥 Downloading PostgreSQL JDBC driver..."
    curl -L -o postgresql.jar https://jdbc.postgresql.org/download/postgresql-42.7.1.jar
    echo "✅ Driver downloaded"
fi

# Run application
echo ""
echo "🚀 Starting application..."
echo ""
java -cp out:postgresql.jar Main

echo ""
echo "👋 Application closed"
