# GokChat

![Clients Chat Screen](https://i.imgur.com/fSh1XoZ.png)

GokChat is a network chat program that allows multiple users to join and communicate with each other.

Along with the ability to handle multiple users, the server-side program also has features like command inputs, server log output and a ping system to determine if users are still connected.

The Client app is built using Swing API for GUI based user interaction with the help of Apache NetBeans GUI designer. 
The Client app has:
* Chat area that displays chat transcripts and some useful information
* Text input that takes the user's text and command inputs
* Online users panel that shows which users are online and their IDs
* Colour selector to change the colour scheme.

Originally, I decided to make this program to have a better understanding of Java Socket programming and Swing API without creating an overly complicated program.

___

## More info

* TCP protocol for the communication between client and server.
* GUI is set to undecorated, meaning that the Client app does not display operating system's native title bar and frame.
* Users can send commands to the server.
* Informs all users whenever a user joins or leaves the server.
* Users can send Personal Messages (using `/pm` command) to another user using their ID.

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
    3. Either run the program from the console or create .jar files.

