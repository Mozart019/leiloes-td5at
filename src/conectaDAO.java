import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conectaDAO {
    
    public Connection connectDB() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uc11?user=root&password=bidu&useSSL=false&allowPublicKeyRetrieval=true");
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ConectaDAO: " + erro.getMessage());
        }
        return conn;
    }
}