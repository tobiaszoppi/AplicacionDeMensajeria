import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static Database instance;
    private final Connection connection;

    private static String url = "jdbc:mysql://127.0.0.1:3306/mensajeriaapp";
    private static String user = "root";
    private static String pass = "643851";

    public Database() throws SQLException {
        connection = DriverManager.getConnection(url, user, pass);
        System.out.println("Conexión a la base de datos exitosa");
    }

    public static Database getInstance() throws SQLException {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // Accede a los nombres de los users y los devuelve en una lista
    protected List<String> listaNombres() throws SQLException {
        List<String> names = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT username FROM users")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    names.add(resultSet.getString("username"));
                }
            }
        }
        return names;
    }
    // Accede a los nombres de los users activos y los devuelve en una lista
    protected List<String> listaNombresActivos() throws SQLException {
        List<String> names = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT username FROM users WHERE isActive = 1")) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    names.add(resultSet.getString("username"));
                }
            }
        }
        return names;
    }

    // Verificar si existe el usuario
    protected boolean userExist(String username) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // Insertar usuario a la db
    protected boolean createUser(String username, String password) throws SQLException {
        PassEnc p = new PassEnc();

        List<String> passData = p.createPassword(password);
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO users (username, securePassword, saltValue) VALUES" +
                " (?, ?, ?)")) {
            statement.setString(1, username);
            statement.setString(2, passData.get(0));
            statement.setString(3, passData.get(1));
            return (statement.executeUpdate() == 1 &&
                    logAction(username, "Creacion de usuario", "El usuario " + username + " ha sido creado"));
        }
    }

    // Eliminar usuario
    protected boolean deleteUser(String username) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE username = ?")) {
            statement.setString(1, username);

            return (statement.executeUpdate() == 1 &&
                    logAction(username, "Alteracion de Estado", "El usuario " + username + " ha sido eliminado"));
        }
    }

    // Comparar securePasswords.
    protected boolean checkPassword(String username, String password) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT securePassword, saltValue FROM users WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedSecurePassword = resultSet.getString("securePassword");
                    String storedSaltValue = resultSet.getString("saltValue");
                    return PassBasedEnc.verifyUserPassword(password, storedSecurePassword, storedSaltValue);
                }
            }
        }
        return false;
    }

    // Preguntar si el usuario está logeado:
    protected boolean isActive(String username) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT isActive FROM users WHERE username = ?")) {
            statement.setString(1, username);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    if (resultSet.getInt("isActive") == 1) {
                        return true;
                    } else return false;
                }
            }
        }
        return false;
    }

    // Setteear si el usuario está logeado:
    protected boolean setIsActive(String username, boolean isActive) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE users SET isActive = ? WHERE username = ?")) {
            statement.setInt(1, isActive ? 1 : 0);
            statement.setString(2, username);
            return statement.executeUpdate() == 1;
        }
    }

    // Auditor:
    public boolean logAction(String username, String action, String detail) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO auditor (timestamp, username, action, detail) VALUES (?, ?, ?, ?)")) {
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            statement.setString(2, username);
            statement.setString(3, action);
            statement.setString(4, detail);
            return statement.executeUpdate() == 1;
        }
    }

}
