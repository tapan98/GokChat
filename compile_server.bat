@echo off

mkdir dist
cd classfiles
mkdir Client
cd ..

echo Compiling files...
javac -d classfiles\Server src/com/gok/chat/server/GokServer.java src/com/gok/chat/server/Server.java src/com/gok/chat/server/ClientListener.java src/com/gok/chat/server/ClientHandler.java

cd classfiles\Server
echo Creating GokServer.jar file in dist directory...
jar -cfm ..\..\dist\GokServer.jar ..\server-manifest-addition.mf .
cd ..\..
