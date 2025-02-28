package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/meneger";
    private static final String USER = "postgres";
    private static final String PASSWORD = "26052006";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static boolean validateFields(String name, String phone, String email) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("Ім'я не може бути порожнім!");
            return false;
        }
        if (phone == null || phone.trim().isEmpty()) {
            System.out.println("Телефон не може бути порожнім!");
            return false;
        }
        if (email == null || email.trim().isEmpty()) {
            System.out.println("Email не може бути порожнім!");
            return false;
        }
        return true;
    }


    private static boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            System.out.println("Некоректний формат email!");
            return false;
        }
        return true;
    }


    public static void createContact(String name, String phone, String email) {

        if (!validateFields(name, phone, email) || !validateEmail(email)) {
            return;
        }

        String sql = "INSERT INTO contacts (name, phone, email) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.executeUpdate();
            System.out.println("Контакт додано успішно!");

        } catch (SQLException e) {
            System.out.println("Помилка додавання контакту: " + e.getMessage());
        }
    }
    public static void readContacts() {
        String sql = "SELECT * FROM contacts";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");

                System.out.println("ID: " + id + ", Name: " + name + ", Phone: " + phone + ", Email: " + email);
            }

        } catch (SQLException e) {
            System.out.println("Помилка читання контактів: " + e.getMessage());
        }
    }

    public static void updateContact(int id, String name, String phone, String email) {
        String sql = "UPDATE contacts SET name = ?, phone = ?, email = ? WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, email);
            stmt.setInt(4, id);
            stmt.executeUpdate();
            System.out.println("Контакт оновлено успішно!");

        } catch (SQLException e) {
            System.out.println("Помилка оновлення контакту: " + e.getMessage());
        }
    }

    public static void deleteContact(int id) {
        String sql = "DELETE FROM contacts WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Контакт видалено успішно!");

        } catch (SQLException e) {
            System.out.println("Помилка видалення контакту: " + e.getMessage());
        }
    }

}
