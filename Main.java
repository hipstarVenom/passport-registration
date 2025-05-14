import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseHelper.initializeDatabase();

        // Form container
        GridPane formGrid = new GridPane();
        formGrid.setPadding(new Insets(30));
        formGrid.setVgap(15);
        formGrid.setHgap(20);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.setStyle("""
            -fx-border-color: #3498db;
            -fx-border-width: 2px;
            -fx-border-radius: 15px;
            -fx-background-radius: 15px;
            -fx-background-color: #ecf5ff;
        """);

        Label titleLabel = new Label("🛂 Passport Registration");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Fields
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");
        nameField.setTooltip(new Tooltip("Full name (e.g., John Doe)"));

        DatePicker dobPicker = new DatePicker();
        dobPicker.setPromptText("YYYY-MM-DD");
        dobPicker.setEditable(true);
        dobPicker.setTooltip(new Tooltip("Select your date of birth"));

        ComboBox<String> genderBox = new ComboBox<>();
        genderBox.getItems().addAll("Male", "Female", "Other");
        genderBox.setTooltip(new Tooltip("Select your gender"));

        TextArea addressArea = new TextArea();
        addressArea.setPrefRowCount(3);
        addressArea.setPromptText("Enter your address");
        addressArea.setTooltip(new Tooltip("Provide your residential address"));

        TextField emailField = new TextField();
        emailField.setPromptText("Enter valid email address");
        emailField.setTooltip(new Tooltip("Example: user@example.com"));

        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter 10-digit phone number");
        phoneField.setTooltip(new Tooltip("Phone number should be 10 digits"));

        TextField aadharField = new TextField();
        aadharField.setPromptText("Enter 12-digit Aadhar number");
        aadharField.setTooltip(new Tooltip("Aadhar number should be exactly 12 digits"));

        TextField nationalityField = new TextField("Indian");
        nationalityField.setPromptText("Nationality");
        nationalityField.setTooltip(new Tooltip("Enter your nationality"));

        ComboBox<String> passportBox = new ComboBox<>();
        passportBox.getItems().addAll("Ordinary", "Diplomatic", "Official");
        passportBox.setTooltip(new Tooltip("Select passport type"));

        // Wrap validation
        HBox nameWrap = withValidation(nameField, "[A-Za-z ]{2,}", "-fx-border-color: red;");
        HBox dobWrap = new HBox(dobPicker);
        HBox emailWrap = withValidation(emailField, "^[\\w.-]+@[\\w.-]+\\.\\w+$", "-fx-border-color: red;");
        HBox phoneWrap = withValidation(phoneField, "\\d{10}", "-fx-border-color: red;");
        HBox aadharWrap = withValidation(aadharField, "\\d{12}", "-fx-border-color: red;");

        Button submitBtn = new Button("Submit");
        submitBtn.setStyle("""
            -fx-background-color: #3498db;
            -fx-text-fill: white;
            -fx-font-weight: bold;
            -fx-background-radius: 5px;
            -fx-padding: 10px 20px;
        """);

        Label statusLabel = new Label();
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Add to grid
        formGrid.add(new Label("Full Name:"), 0, 0);
        formGrid.add(nameWrap, 1, 0);
        formGrid.add(new Label("Date of Birth:"), 0, 1);
        formGrid.add(dobWrap, 1, 1);
        formGrid.add(new Label("Gender:"), 0, 2);
        formGrid.add(genderBox, 1, 2);
        formGrid.add(new Label("Address:"), 0, 3);
        formGrid.add(addressArea, 1, 3);
        formGrid.add(new Label("Email:"), 0, 4);
        formGrid.add(emailWrap, 1, 4);
        formGrid.add(new Label("Phone:"), 0, 5);
        formGrid.add(phoneWrap, 1, 5);
        formGrid.add(new Label("Aadhar Number:"), 0, 6);
        formGrid.add(aadharWrap, 1, 6);
        formGrid.add(new Label("Nationality:"), 0, 7);
        formGrid.add(nationalityField, 1, 7);
        formGrid.add(new Label("Passport Type:"), 0, 8);
        formGrid.add(passportBox, 1, 8);
        formGrid.add(submitBtn, 1, 9);

        VBox root = new VBox(20, titleLabel, formGrid, statusLabel);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #ffffff, #d6eaff);");

        submitBtn.setOnAction(e -> {
            if (validateAll(nameField, dobPicker, emailField, phoneField, aadharField, passportBox)) {
                try (Connection conn = DatabaseHelper.getConnection()) {
                    String sql = """
                        INSERT INTO passport (
                          full_name, dob, gender, address, email,
                          phone, aadhar_number, nationality, passport_type, application_date
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """;
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, nameField.getText());
                    stmt.setString(2, dobPicker.getValue().toString());
                    stmt.setString(3, genderBox.getValue());
                    stmt.setString(4, addressArea.getText());
                    stmt.setString(5, emailField.getText());
                    stmt.setString(6, phoneField.getText());
                    stmt.setString(7, aadharField.getText());
                    stmt.setString(8, nationalityField.getText());
                    stmt.setString(9, passportBox.getValue());
                    stmt.setString(10, LocalDate.now().toString());
                    stmt.executeUpdate();

                    statusLabel.setStyle("-fx-text-fill: green;");
                    statusLabel.setText("✅ Registration Successful!");

                    // clear
                    nameField.clear();
                    dobPicker.setValue(null);
                    genderBox.setValue(null);
                    addressArea.clear();
                    emailField.clear();
                    phoneField.clear();
                    aadharField.clear();
                    nationalityField.setText("Indian");
                    passportBox.setValue(null);

                    clearValidation(nameField, emailField, phoneField, aadharField);

                } catch (SQLException ex) {
                    statusLabel.setStyle("-fx-text-fill: red;");
                    statusLabel.setText("❌ Error: " + ex.getMessage());
                }
            } else {
                statusLabel.setStyle("-fx-text-fill: red;");
                statusLabel.setText("❌ Please correct invalid fields.");
            }
        });

        primaryStage.setScene(new Scene(root, 900, 700));
        primaryStage.setTitle("Passport Registration Form");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private HBox withValidation(TextField field, String regex, String errorStyle) {
        ImageView ok = new ImageView(new Image("https://img.icons8.com/color/48/ok.png", 20, 20, true, true));
        ImageView fail = new ImageView(new Image("https://img.icons8.com/color/48/cancel.png", 20, 20, true, true));
        Label icon = new Label();
        field.textProperty().addListener((o, old, nv) -> {
            boolean pass = nv.matches(regex);
            field.setStyle(pass ? "" : errorStyle);
            icon.setGraphic(pass ? ok : fail);
        });
        return new HBox(5, field, icon);
    }

    private boolean validateAll(TextField name, DatePicker dob, TextField email, TextField phone,
                                TextField aadhar, ComboBox<String> passportBox) {
        return name.getText().matches("[A-Za-z ]{2,}")
                && dob.getValue() != null
                && email.getText().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")
                && phone.getText().matches("\\d{10}")
                && aadhar.getText().matches("\\d{12}")
                && passportBox.getValue() != null;
    }

    private void clearValidation(TextField... fields) {
        for (TextField f : fields) {
            f.setStyle("");
            HBox h = (HBox) f.getParent();
            ((Label) h.getChildren().get(1)).setGraphic(null);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
