import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class Helpers {
    private static final Scanner stdin = new Scanner(System.in);

    public static String getInputStr(String message) {
        System.out.println(message);
        return stdin.nextLine().trim();
    }

    public static int getInputInt(String message) throws NumberFormatException {
        System.out.println(message);
        return Integer.parseInt(stdin.nextLine().trim());
    }

    public static void easyExecuteUpdate(PreparedStatement statement, boolean isDeletion) {
        int result = -1;
        try {
            result = statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("An error occurred. Try again with proper value/s");
            System.out.println(e.getMessage());
            Helpers.PRESS_ENTER_TO_CONTINUE();
            return;
        }

        if (result == 0) {
            System.out.println("An error occurred. Try again with proper value/s");
            Helpers.PRESS_ENTER_TO_CONTINUE();
            return;
        }

        if (! isDeletion) System.out.printf("%d record/s committed successfully.", result);
        else System.out.printf("%d record/s deleted successfully.", result);
        Helpers.PRESS_ENTER_TO_CONTINUE();
    }

    public static void printResultSet(ResultSet set) throws SQLException {
        LinkedHashMap<Integer, ArrayList<String>> TABLE = new LinkedHashMap<>();
        LinkedHashMap<Integer, Integer> TABLE_COLUMN_MAX_LENGTH = new LinkedHashMap<>();
        StringBuilder prettyTableHeader = new StringBuilder();
        StringBuilder prettyTableBody = new StringBuilder();

        ResultSetMetaData setMeta = set.getMetaData();
        int numberOfColumns = setMeta.getColumnCount();
        int numberOfRows = 0;

        for (int index = 1; index <= numberOfColumns; ++index) {
            String columnName = setMeta.getColumnName(index);
            TABLE.put(index, new ArrayList<>());
            TABLE.get(index).add(columnName);
            TABLE_COLUMN_MAX_LENGTH.put(index, columnName.length());
        }

        while (set.next()) {
            for (int index = 1; index <= numberOfColumns; ++index) {
                String cellData = set.getString(index);
                
                if (cellData == null) {
                	cellData = "  -  ";
                }

                TABLE.get(index).add(cellData);

                if (TABLE_COLUMN_MAX_LENGTH.get(index) < cellData.length()) {
                    TABLE_COLUMN_MAX_LENGTH.put(index, cellData.length());
                }
            }
            ++numberOfRows;
        }

        int totalHeaderLength = 0;
        prettyTableHeader.append("| ");
        // Pretty-print table
        for (int i = 1; i <= numberOfRows; ++i) {
            prettyTableBody.append("| ");
            for (int j = 1; j <= numberOfColumns; ++j) {
                String cellData = TABLE.get(j).get(i);                
                
                if (cellData == null) {
                	cellData = "  -  ";
                }
                
                if (cellData.equals("t")) cellData = "True";
                else if (cellData.equals("f")) cellData = "False";
                
                prettyTableBody.append(cellData);

                if (i == 1) {
                    String oneHeader = TABLE.get(j).get(0);
                    prettyTableHeader.append(oneHeader);
                    for (int k = 0; k < TABLE_COLUMN_MAX_LENGTH.get(j) - (oneHeader.length() + 1); ++k) {
                        prettyTableHeader.append(' ');
                        totalHeaderLength += TABLE_COLUMN_MAX_LENGTH.get(j);
                    }
                    prettyTableHeader.append(" |");
                }

                for (int k = 0; k < TABLE_COLUMN_MAX_LENGTH.get(j) - cellData.length(); ++k) {
                    prettyTableBody.append(' ');
                }
                
                prettyTableBody.append("| ");
            }
            prettyTableBody.append('\n');
        }

        prettyTableHeader.append("\n| ");
        
        totalHeaderLength = 0;
        for (int i = 1; i < TABLE_COLUMN_MAX_LENGTH.size() + 1; ++i)
        	totalHeaderLength += TABLE_COLUMN_MAX_LENGTH.get(i);
        
        for (int i = 0; i < (totalHeaderLength + 2*(numberOfColumns-1) - 1); ++i)
            prettyTableHeader.append('=');
        
        prettyTableHeader.append(" |");

        System.out.println(prettyTableHeader.toString());
        System.out.println(prettyTableBody.toString());
    }

    public static void PRESS_ENTER_TO_CONTINUE() {
        System.out.println("\nPress 'Enter' to continue...");
        stdin.nextLine();
    }

    public static void close_scanner() {
        stdin.close();
    }
}
