import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Properties;
import java.util.Scanner;

public class Driver {
    public static HashMap<String, String> local_config;
    public static HashMap<String, String> remote_config;

    public static void main(String[] args) throws Exception {
        local_config = new HashMap<>();
        remote_config = new HashMap<>();
        Scanner config = new Scanner(new File("res/config.txt"));

        while (config.hasNextLine()) {
            String[] elements = config.nextLine().split(";");
            String key = elements[0];
            String value = elements[1];

            if (key.startsWith("LOCAL")) local_config.put(key.substring(6), value);
            else if (key.startsWith("REMOTE")) remote_config.put(key.substring(7), value);
            else throw new Exception("ERROR :: IMPROPER CONFIGURATION FILE");
        }
        
        config.close();

        int connectionType = 0;
        while (!(connectionType == 1 || connectionType == 2)) {
            try {
                connectionType = Helpers.getInputInt("* Type 1 for LOCAL connection.\n* Type 2 for REMOTE connection.\nYour Choice: ");
            } catch (NumberFormatException e) {
                System.out.println("ERROR :: WRONG INPUT! TRY AGAIN...\n");
            }
        }

        Connection connection = null;
        String URI = null;
        Properties properties = new Properties();

        if (connectionType == 1) {
            URI = local_config.get("URI") + "/" + local_config.get("DBNAME");
            properties.setProperty("user", local_config.get("UNAME"));
            properties.setProperty("password", local_config.get("PW"));
        }

        if (connectionType == 2) {
            URI = remote_config.get("URI") + "/" + remote_config.get("DBNAME");
            properties.setProperty("user", remote_config.get("UNAME"));
            properties.setProperty("password", remote_config.get("PW"));
        }

        try {
            System.out.println("Connecting...");
            connection = DriverManager.getConnection(URI, properties);
        } catch (Exception e) {
            System.out.println("ERROR :: DATABASE CONNECTION FAILED\n" + e.getMessage());
        }

        if (connection == null) {
            System.out.println("ERROR :: DATABASE CONNECTION FAILED");
            System.exit(1);
        }

        System.out.println("SUCCESS :: DB CONNECTION ESTABLISHED...");
        Helpers.PRESS_ENTER_TO_CONTINUE();

        // Event loop
        while (true) {
            int CHOSEN_OPTION = -1;
            try {
                CHOSEN_OPTION = Helpers.getInputInt(
                        "\nType the number of option you want to choose:\n" +
                                "1) Add a new book to the library.(Insert)\n" +
                                "2) Change a publisher's city.(Update)\n" +
                                "3) Cancel a room reservation.(Delete)\n" +
                                "4) Choose to view all information of ..?(List)\n\n" +
                                "0) QUIT PROGRAM");
            } catch (NumberFormatException e) {
                System.out.println("Type only an integer value..!\n");
                Helpers.PRESS_ENTER_TO_CONTINUE();
                continue;
            }
            switch (CHOSEN_OPTION) {
                case 0:
                	Helpers.close_scanner();
                    System.out.println("Closing connection...");
                    connection.close();
                    System.out.println("EXITTED.");
                    System.exit(0);
                    break;
                case 1:
                    final String columns =
                            "book_id, book_name, book_language, book_edition, book_genre, author_id, publisher_id, isbn, copies, page_count";

                    PreparedStatement insertionStatement = null;
                	try {
                		insertionStatement = 
                				connection.prepareStatement(String.format("INSERT INTO books(%s) " + "VALUES(?,?,?,?,?,?,?,?,?,?)", columns));
					} catch (Exception e) {
						System.out.println(e.getMessage());
						continue;
					}
                	
                    insertionStatement.setString(1, Helpers.getInputStr("Type book id: "));
                    insertionStatement.setString(2, Helpers.getInputStr("Type book name: "));
                    insertionStatement.setString(3, Helpers.getInputStr("Type book language: "));
                    insertionStatement.setString(4, Helpers.getInputStr("Type book edition: "));
                    insertionStatement.setString(5, Helpers.getInputStr("Type book genre: "));
                    insertionStatement.setString(6, Helpers.getInputStr("Type author id: "));
                    insertionStatement.setString(7, Helpers.getInputStr("Type publisher id: "));
                    insertionStatement.setString(8, Helpers.getInputStr("Type isbn: "));
                  
                    try {
                        insertionStatement.setInt(9, Helpers.getInputInt("Type number of copies: "));
                        insertionStatement.setInt(10, Helpers.getInputInt("Type page count: "));
                    } catch (NumberFormatException e) {
                        System.out.println("WARNING :: You need to enter an integer value for \"number of copies or page count\" attribute" +
                                "\nThe statement could not have been inserted.");
                        Helpers.PRESS_ENTER_TO_CONTINUE();
                        continue;
                    }

                    Helpers.easyExecuteUpdate(insertionStatement, false);
                    break;
                case 2:
                	PreparedStatement updateStatement = null;
                	try {
                		updateStatement = connection.prepareStatement("UPDATE publishers SET city = ? WHERE publisher_id = ?");
					} catch (Exception e) {
						System.out.println(e.getMessage());
						continue;
					}

                    updateStatement.setString(2,
                            Helpers.getInputStr("Type id of the publisher that you want to update: "));

                    updateStatement.setString(1,
                            Helpers.getInputStr("Type a new city name for the publisher: "));

                    Helpers.easyExecuteUpdate(updateStatement, false);
                    break;
                case 3:
                	PreparedStatement deletionStatement = null;
                    try {
                    	deletionStatement = connection.prepareStatement(
                                "DELETE FROM room_reservation WHERE user_id = ? AND room_id = ?");
					} catch (Exception e) {
						System.out.println(e.getMessage());
						continue;
					}
                    
                    deletionStatement.setString(1,
                            Helpers.getInputStr("Type your user id: "));

                    deletionStatement.setString(2,
                            Helpers.getInputStr("Type id of the room you reserved before: "));

                    Helpers.easyExecuteUpdate(deletionStatement, true);
                    break;
                case 4:
                    try {
                        CHOSEN_OPTION = Helpers.getInputInt(
                                "\nType the number of option you want to choose:\n" +
                                        "1) Show all authors.\n" +
                                        "2) Show all books in the library system.\n" +
                                        "3) Show all departments in the library.\n" +
                                        "4) Show all publishers.\n" +
                                        "5) Show all room reservations.\n" +
                                        "6) Show all staff.\n" +
                                        "7) Show all borrowing procedures.\n" +
                                        "8) Show all study rooms available.\n" +
                                        "9) Show all users which was registered.\n\n" +
                                        "0) BACK TO MENU");
                    } catch (NumberFormatException e) {
                        System.out.println("Type only an integer value..!\n");
                        Helpers.PRESS_ENTER_TO_CONTINUE();
                        continue;
                    }
                    
                    Statement selectionStatement = null;

                    try {
                    	selectionStatement = connection.createStatement();
					} catch (Exception e) {
						System.out.println(e.getMessage());
						continue;
					}

                    StringBuilder QUERY = new StringBuilder();
                    QUERY.append("SELECT * FROM ");

                    switch (CHOSEN_OPTION) {
                        case 0:
                            // BACK_TO_MENU
                            continue;
                        case 1:
                            QUERY.append("authors");
                            break;
                        case 2:
                            QUERY.append("books");
                            break;
                        case 3:
                            QUERY.append("department");
                            break;
                        case 4:
                            QUERY.append("publishers");
                            break;
                        case 5:
                            QUERY.append("room_reservation");
                            break;
                        case 6:
                            QUERY.append("staff");
                            break;
                        case 7:
                            QUERY.append("borrow");
                            break;
                        case 8:
                            QUERY.append("study_rooms");
                            break;
                        case 9:
                            QUERY.append("users");
                            break;
                        default:
                            System.out.printf("There is no option with number \"%d\"", CHOSEN_OPTION);
                            Helpers.PRESS_ENTER_TO_CONTINUE();
                    }
                    try {
                        Helpers.printResultSet(selectionStatement.executeQuery(QUERY.toString()));
                    } catch (Exception e) {
                    	System.out.println("Unexpected error occurred. Try again...");
                    }
                    Helpers.PRESS_ENTER_TO_CONTINUE();
                    break;
                default:
                    System.out.printf("There is no option with number \"%d\"", CHOSEN_OPTION);
                    Helpers.PRESS_ENTER_TO_CONTINUE();
            }
        }
    }
}
