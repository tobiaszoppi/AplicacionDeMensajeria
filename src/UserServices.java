import java.sql.SQLException;

public class UserServices {
    private final DatabaseServices db;

    public UserServices() {
        db = new DatabaseServices();
    }

    public boolean handleRegistration(String username, String password) throws SQLException {

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

}
