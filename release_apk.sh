#!/bin/bash

echo "=== ChatMe Release APK Builder ==="
echo "Date: $(date)"
echo ""

# Restore original build.gradle with all features
echo "1. Restoring full build configuration..."
cp app/build.gradle.original app/build.gradle

# Build release APK
echo ""
echo "2. Building release APK..."
echo "This may take several minutes..."
./gradlew assembleRelease --no-daemon --stacktrace

# Check if release APK was created
echo ""
echo "3. Checking release build results..."
if [ -f "app/build/outputs/apk/release/app-release-unsigned.apk" ]; then
    echo "✅ Release APK successfully created!"
    echo "📱 Location: app/build/outputs/apk/release/app-release-unsigned.apk"
    echo "📊 Size: $(du -h app/build/outputs/apk/release/app-release-unsigned.apk | cut -f1)"
    echo ""
    echo "⚠️  Note: This is an unsigned APK. For production, you need to sign it."
    echo "🔐 To sign: Use Android Studio or jarsigner tool"
else
    echo "❌ Release APK build failed. Check the logs above for errors."
fi

echo ""
echo "=== Release Build Complete ==="
