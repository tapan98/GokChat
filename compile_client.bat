@echo off

echo Compiling files...
javac -d classfiles\Client src/com/gok/chat/Login.java src/com/gok/chat/Client.java src/com/gok/chat/ClientWindow.java
cd classfiles\Client\com\gok\chat\
echo Copying images...
mkdir images
cd ..\..\..\..\..
copy src\com\gok\chat\images classfiles\Client\com\gok\chat\images\

cd classfiles\Client
echo Creating GokChat.jar file in dist directory...
jar -cfm ..\..\dist\GokChat.jar ..\client-manifest-addition.mf .
cd ..\..
