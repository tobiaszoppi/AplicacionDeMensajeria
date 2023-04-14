package server;

import java.sql.SQLException;
import java.util.List;

public class DatabaseServices {
    private final Database db;

    public DatabaseServices() {
        try {
            db = Database.getInstance();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> listaNombres() throws SQLException {
        return db.listaNombres();
    }
    public List<String> listaNombresActivos() throws SQLException {
        return db.listaNombresActivos();
    }
    public boolean createUser(String username, String password) throws SQLException {
        return db.createUser(username, password);
    }
    public boolean deleteUser(String username) throws SQLException {
        return db.deleteUser(username);
    }
    public boolean userExist(String username) throws SQLException {
        return db.userExist(username);
    }
    public boolean checkPassword(String username, String password) throws SQLException {
        return db.checkPassword(username, password);
    }
    public boolean isActive(String username) throws SQLException {
        return db.isActive(username);
    }
    public boolean setIsActive(String username, boolean isActive) throws SQLException {
        return db.setIsActive(username,isActive);
    }

    public boolean isAdmin(String username) throws SQLException {
        return db.isAdmin(username);
    }

    public boolean setAdmin(String username) throws SQLException {
        return db.setAdmin(username);
    }
}
