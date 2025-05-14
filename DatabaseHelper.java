import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHelper {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ SQLite JDBC Driver not found: " + e.getMessage());
        }
    }

    public static final String DB_URL = "jdbc:sqlite:passport.db";

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                String sql = """
                    CREATE TABLE IF NOT EXISTS passport (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        full_name TEXT NOT NULL,
                        dob TEXT NOT NULL,
                        gender TEXT NOT NULL,
                        address TEXT NOT NULL,
                        email TEXT,
                        phone TEXT,
                        aadhar_number TEXT NOT NULL,
                        nationality TEXT DEFAULT 'Indian',
                        passport_type TEXT NOT NULL,
                        application_date TEXT NOT NULL
                    );
                    """;
                stmt.execute(sql);
                System.out.println("✅ Passport table created or already exists.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error initializing DB: " + e.getMessage());
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (SQLException e) {
            System.out.println("❌ Connection failed: " + e.getMessage());
            return null;
        }
    }
}
