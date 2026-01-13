#!/bin/bash

# Compile and prepare the application

echo "🔨 Compiling..."
javac -d out $(find src -name "*.java")

if [ $? -ne 0 ]; then
    echo "❌ Compilation failed!"
    exit 1
fi

echo "✅ Compilation successful"

# Copy dependencies
echo "📦 Copying dependencies..."
cp postgresql.jar out/ 2>/dev/null || true
cp src/resources/application.properties out/ 2>/dev/null || true

echo "✅ Ready to run!"
echo ""
echo "To start the application, use:"
echo "  java -cp out:out/postgresql.jar Main"
