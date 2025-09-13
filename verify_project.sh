#!/bin/bash

echo "=== ChatMe Android Project Verification ==="
echo "Date: $(date)"
echo ""

echo "1. Checking project structure..."
if [ -d "app/src/main/java/tpass/chatme" ]; then
    echo "✓ Main source directory exists"
    echo "  Java files count: $(find app/src/main/java -name "*.java" | wc -l)"
else
    echo "✗ Main source directory missing"
fi

echo ""
echo "2. Checking key files..."
files=("app/build.gradle" "app/src/main/AndroidManifest.xml" "app/google-services.json")
for file in "${files[@]}"; do
    if [ -f "$file" ]; then
        echo "✓ $file exists"
    else
        echo "✗ $file missing"
    fi
done

echo ""
echo "3. Checking SDK versions..."
echo "  compileSdk: $(grep 'compileSdk' app/build.gradle | tr -d '\t')"
echo "  targetSdkVersion: $(grep 'targetSdkVersion' app/build.gradle | tr -d '\t')"
echo "  minSdkVersion: $(grep 'minSdkVersion' app/build.gradle | tr -d '\t')"

echo ""
echo "4. Checking Gradle version..."
echo "  Gradle wrapper: $(grep 'gradle-.*-bin.zip' gradle/wrapper/gradle-wrapper.properties | cut -d'/' -f5)"

echo ""
echo "5. Checking key activities..."
activities=("MainActivity" "ChatActivity" "UsersActivity" "LogActivity")
for activity in "${activities[@]}"; do
    if [ -f "app/src/main/java/tpass/chatme/${activity}.java" ]; then
        echo "✓ ${activity}.java exists"
    else
        echo "✗ ${activity}.java missing"
    fi
done

echo ""
echo "6. Checking resources..."
if [ -d "app/src/main/res" ]; then
    echo "✓ Resources directory exists"
    echo "  Layout files: $(find app/src/main/res/layout -name "*.xml" 2>/dev/null | wc -l)"
    echo "  Drawable files: $(find app/src/main/res/drawable* -name "*" 2>/dev/null | wc -l)"
    echo "  Animation files: $(find app/src/main/res/anim -name "*.xml" 2>/dev/null | wc -l)"
else
    echo "✗ Resources directory missing"
fi

echo ""
echo "7. Checking test structure..."
if [ -d "app/src/test/java" ]; then
    echo "✓ Test directory exists"
    echo "  Test files: $(find app/src/test/java -name "*.java" | wc -l)"
else
    echo "✗ Test directory missing"
fi

echo ""
echo "=== Verification Complete ==="
