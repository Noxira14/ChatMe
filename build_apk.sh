#!/bin/bash

echo "=== ChatMe APK Builder ==="
echo "Date: $(date)"
echo ""

# Check Java version
echo "1. Checking Java version..."
java -version 2>&1 | head -1

# Check Gradle version
echo ""
echo "2. Checking Gradle version..."
./gradlew --version | grep "Gradle"

# Clean previous builds
echo ""
echo "3. Cleaning previous builds..."
./gradlew clean --no-daemon

# Build debug APK
echo ""
echo "4. Building debug APK..."
echo "This may take several minutes..."
./gradlew assembleDebug --no-daemon --stacktrace

# Check if APK was created
echo ""
echo "5. Checking build results..."
if [ -f "app/build/outputs/apk/debug/app-debug.apk" ]; then
    echo "✅ APK successfully created!"
    echo "📱 Location: app/build/outputs/apk/debug/app-debug.apk"
    echo "📊 Size: $(du -h app/build/outputs/apk/debug/app-debug.apk | cut -f1)"
    echo ""
    echo "🚀 APK ready for installation!"
else
    echo "❌ APK build failed. Check the logs above for errors."
    echo ""
    echo "💡 Common solutions:"
    echo "   - Ensure Java 11+ is installed"
    echo "   - Check internet connection for dependencies"
    echo "   - Verify Android SDK is available"
    echo "   - Try running: ./gradlew clean assembleDebug"
fi

echo ""
echo "=== Build Complete ==="
