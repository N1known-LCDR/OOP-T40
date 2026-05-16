How to start the PacMan game
The game was developed with Maven and Java 21.

Requirements
Before running the project, make sure you have the following installed:
Java JDK 21,
Maven

You can verify the installation with:
java -version,
mvn -version,

Project Setup:
Extract the Project

Unzip the project folder:
OOP-T40-main example.zip
Navigate to the Pacman Folder
cd OOP-T40-main/template/pacman

Running the Project

Run the following Maven command:
mvn clean javafx:run

This will:

Download all required dependencies
Compile the project
Launch the Pacman game
Open a window with all the needed instructions to play the game

You are now ready to enjoy the game. Good luck!

Technologies Used
Java 21
JavaFX 21
Maven

Project Structure
pacman/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── dk/sdu/imada/oop26/
                ├── Main.java
                ├── Player.java
                ├── Ghost.java
                ├── Map.java
                ├── GameManager.java
                └── ...

Troubleshooting
Java Version Issues
If Maven fails due to Java version mismatch, ensure Java 21 is active:
java -version

JavaFX Not Starting
Try cleaning and rebuilding:
mvn clean install
mvn javafx:run


