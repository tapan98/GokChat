# GokChat

![Clients Chat Screen](https://i.imgur.com/ijP6z1n.png)

GokChat is a network chat program which allows multiple users to join and communicate with each other.

The Client app uses Swing API for GUI based user interaction which was built with the help of Apache NetBeans GUI designer.

Originally, I decided to make this program to have a better understanding in Java Socket programming and Swing API without creating an overly compilcated program. 

___

## More info

* Uses TCP protocol for the communication between client and server.
* Graphical User Interface (GUI) for the client.
* Users can send commands to the server.
* Informs all users whenever a user joins or leaves the server.
* Server uses ping system to determine if the connection has been lost with the user.
* The Client Window displays a list of online users.

___

## Requirements

* [JDK 11](https://www.oracle.com/in/java/technologies/javase-jdk11-downloads.html)

___

## Compile and run

* ### on Windows platform:
    1. Run `compile_client.bat` and `compile_server.bat` files.
    2. To run the server: navigate to dist directory and enter `java -jar GokServer.jar [PORT]` where [PORT] is the port number to run the server at.
    3. To run the client: double click GokChat.jar in the dist directory.
* ### on other platforms:
    1. Compile the client files using `javac` command located in: `src/com/gok/chat/` (note that the client also uses the images directory)
    2. Compile the server files using `javac` command located in: `src/com/gok/chat/server/`
        * The main class of the server is `com.gok.chat.server.GokServer`
        * The main class of the client is `com.gok.chat.GokChat`
    3. Either run the program directly or create .jar files.

