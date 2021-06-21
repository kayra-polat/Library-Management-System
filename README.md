# Library-Management-System
A library management system made using Postgresql. This project was designed during the course "Relational Databases CMPE232".

## Entity Relationship Diagram and Relational Schema
 The **ER Diagram** file can be created with using a useful tool which is [Visual Paradigm](https://www.visual-paradigm.com/features/database-design-with-erd-tools/)
![Library Management System ER Model](https://user-images.githubusercontent.com/75734949/122827883-a3ac6b00-d2ed-11eb-9f9e-c91ff89422a1.jpg)
![Relational Schema_Final](https://user-images.githubusercontent.com/75734949/122827903-a909b580-d2ed-11eb-9c0e-6de5b224d676.png)

## Java Application Program
=> In the Java command line interface part, we implemented an API to provide some functionalities of our database to end user. At the very first part, the program must make a connection through the database with authorization credentials. So, we created a “config.txt” file under the “/project_root_directory/res/” folder to store user secrets properly. The file includes both local and remote configurations as key-value pairs for ease of use (you can edit local values to test it out on your machine). After we passed the credentials into the Java program, the program asks for connection type (local/remote). Then it connects to the database with the help of Postgres JDBC driver (included inside the “/project_root_directory/lib/” folder).

![image](https://user-images.githubusercontent.com/75734949/122827990-c474c080-d2ed-11eb-9373-b64d3023453f.png)

=> Later on, the program displays 5 options to retrieve data, to commit a query or to quit program. First option is an insertion statement. The program wants necessary attributes to create a tuple (book) in the database. It gives an error if you leave a “NOT NULL field” empty. Otherwise, you can directly tap to “enter” to skip for nullable attributes. Second option is an update statement. The program wants an existing publisher’s ID and updates its city according to the user input as an example case. And third option is a deletion statement. The program waits for user id and room id. Then it deletes the reservations related to both the user and the room. Finally, the fourth option provides a listing operation. It is possible to view almost all tables in the database with this command (including the tables which was used in first 3 options). The program prints the output of a chosen table to console. All of the features above are controlled by “while-loop” and “switch-case” control statements depending on the user input.

![image](https://user-images.githubusercontent.com/75734949/122828075-da828100-d2ed-11eb-8240-93b7268b0a0f.png) (Main Menu)

![image](https://user-images.githubusercontent.com/75734949/122828106-e53d1600-d2ed-11eb-8c5f-1467cae1bfc5.png) (Result Table)

=> Additionally, the program’s source file contains helper functionalities which was defined by us in the Helper class. For example, “PRESS_ENTER_TO_CONTINUE” method waits until be sure about that user’s get acknowledged or “printResultSet” method shows retrieved table (result set) in a prettier state. These methods were provided to enhance user experience and to make code more modular. Also, the functions such as “getInputStr” and “getInputInt” offers required exception handling system and saves us from writing duplicate lines of code to pass the user input into statements.
