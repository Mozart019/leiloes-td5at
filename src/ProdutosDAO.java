import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutosDAO {
    // Removido: ArrayList<ProdutosDTO> listagem = new ArrayList<>();

    public void cadastrarProduto(ProdutosDTO produto) {
        Connection conn = null;
        PreparedStatement prep = null;
        try {
            conn = new conectaDAO().connectDB();
            if (conn == null) {
                throw new RuntimeException("Falha ao conectar ao banco: conexão é null");
            }
            
            String sql = "INSERT INTO produtos (nome, valor, status) VALUES (?, ?, ?)";
            prep = conn.prepareStatement(sql);
            prep.setString(1, produto.getNome());
            prep.setInt(2, produto.getValor());
            prep.setString(3, "À Venda");
            prep.executeUpdate();
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao cadastrar produto: " + erro.getMessage());
        } finally {
            try {
                if (prep != null) prep.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public ArrayList<ProdutosDTO> listarProdutos() {
        ArrayList<ProdutosDTO> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement prep = null;
        ResultSet rs = null;
        try {
            conn = new conectaDAO().connectDB();
            if (conn == null) {
                throw new RuntimeException("Falha ao conectar ao banco: conexão é null");
            }

            String sql = "SELECT * FROM produtos";
            prep = conn.prepareStatement(sql);
            rs = prep.executeQuery();

            while (rs.next()) {
                ProdutosDTO produto = new ProdutosDTO();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setValor(rs.getInt("valor"));
                produto.setStatus(rs.getString("status"));
                lista.add(produto);
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao listar produtos: " + erro.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (prep != null) prep.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar conexão: " + e.getMessage());
            }
        }
        return lista;
    }
}