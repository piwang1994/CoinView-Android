#!/bin/bash

echo "=========================================="
echo "  🚀 CoinView Google 登录完整测试"
echo "=========================================="
echo ""

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}📦 步骤 1: 清理项目...${NC}"
./gradlew clean > /dev/null 2>&1
rm -rf app/build build .gradle
echo -e "${GREEN}✅ 清理完成${NC}"
echo ""

echo -e "${BLUE}🔨 步骤 2: 构建应用...${NC}"
./gradlew installDebug

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 应用构建并安装成功${NC}"
else
    echo -e "${RED}❌ 应用构建失败${NC}"
    exit 1
fi
echo ""

echo -e "${BLUE}🔍 步骤 3: 检查设备状态...${NC}"
echo ""

echo -e "${YELLOW}--- SHA-1 指纹 ---${NC}"
./gradlew signingReport 2>/dev/null | grep SHA1 | head -1

echo ""
echo -e "${YELLOW}--- Google 账号 ---${NC}"
ACCOUNTS=$(adb shell dumpsys account | grep "Account {name=" | head -3)
if [ -z "$ACCOUNTS" ]; then
    echo -e "${RED}⚠️  设备上没有 Google 账号!${NC}"
    echo -e "${YELLOW}请在设备上添加 Google 账号:${NC}"
    echo "   设置 → 账号 → 添加账号 → Google"
else
    echo "$ACCOUNTS"
fi

echo ""
echo -e "${YELLOW}--- Google Play Services ---${NC}"
adb shell dumpsys package com.google.android.gms | grep versionName | head -1

echo ""
echo -e "${YELLOW}--- 设备信息 ---${NC}"
API_LEVEL=$(adb shell getprop ro.build.version.sdk)
DEVICE=$(adb shell getprop ro.product.model)
echo "设备: $DEVICE"
echo "API Level: $API_LEVEL"

echo ""
echo "=========================================="
echo -e "${GREEN}  ✅ 准备完成!${NC}"
echo "=========================================="
echo ""
echo -e "${YELLOW}📱 请在设备上执行以下操作:${NC}"
echo "   1. 打开 CoinView 应用"
echo "   2. 点击 'Sign in with Google' 按钮"
echo "   3. 选择 Google 账号"
echo ""
echo -e "${BLUE}🔍 开始监控日志...${NC}"
echo "=========================================="
echo ""

# 清空日志
adb logcat -c

# 监控日志
adb logcat | grep --line-buffered -E "LoginActivity|FirebaseAuthManager|GoogleSignIn|ApiException" | while IFS= read -r line; do
    if echo "$line" | grep -q "✅"; then
        echo -e "${GREEN}$line${NC}"
    elif echo "$line" | grep -q "❌"; then
        echo -e "${RED}$line${NC}"
    elif echo "$line" | grep -q "⚠️"; then
        echo -e "${YELLOW}$line${NC}"
    elif echo "$line" | grep -q "🔵"; then
        echo -e "${BLUE}$line${NC}"
    else
        echo "$line"
    fi
done

