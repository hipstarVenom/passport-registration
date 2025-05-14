import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PrintDatabase {

    public static void printAllPassports() {
        try (Connection conn = DatabaseHelper.getConnection()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM passport");

                System.out.println("----- Passport Records -----");
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Name: " + rs.getString("full_name"));
                    System.out.println("DOB: " + rs.getString("dob"));
                    System.out.println("Gender: " + rs.getString("gender"));
                    System.out.println("Address: " + rs.getString("address"));
                    System.out.println("Email: " + rs.getString("email"));
                    System.out.println("Phone: " + rs.getString("phone"));
                    System.out.println("Aadhar: " + rs.getString("aadhar_number"));
                    System.out.println("Nationality: " + rs.getString("nationality"));
                    System.out.println("Passport Type: " + rs.getString("passport_type"));
                    System.out.println("Application Date: " + rs.getString("application_date"));
                    System.out.println("-----------------------------");
                }
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching data: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        DatabaseHelper.initializeDatabase(); // Optional, ensures DB is ready
        printAllPassports();
    }
}
