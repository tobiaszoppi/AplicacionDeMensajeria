package user;

import server.DatabaseServices;

import java.sql.SQLException;

public class UserServices {
    private final DatabaseServices db;

    public UserServices() {
        db = new DatabaseServices();
    }

    public boolean handleAccess(String username, String password) throws SQLException {

        if (username.isBlank() || password.isBlank()) {
           throw new RuntimeException("Campos vacios");
        }

        if (db.userExist(username)) {
            if (db.checkPassword(username, password)) {
                System.out.println("Contraseña correcta");
                return true;
            } else return false;
        }

        System.out.println("Registro exitoso " + username + " registrado correctamente");
        return db.createUser(username, password);
    }

    protected boolean isAdmin(String username) throws SQLException {
        return db.isAdmin(username);
    }

    protected boolean setAdmin(String username) throws SQLException {
        return db.setAdmin(username);
    }

    protected boolean isActive(String username) throws SQLException {
        return db.isActive(username);
    }

    protected boolean setIsActive(String username, boolean variant) throws SQLException {
        return db.setIsActive(username,variant);
    }

    protected boolean kick(String username) throws SQLException {
        if (db.isActive(username)) {
            return db.setIsActive(username, false);
        }
        return false;
    }
}
