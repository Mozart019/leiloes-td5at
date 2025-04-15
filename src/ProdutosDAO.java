import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProdutosDAO {

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

    public void venderProduto(int id) {
        Connection conn = null;
        PreparedStatement prepVerifica = null;
        PreparedStatement prepAtualiza = null;
        ResultSet rs = null;
        try {
            // Conectar ao banco de dados
            conn = new conectaDAO().connectDB();
            if (conn == null) {
                throw new RuntimeException("Falha ao conectar ao banco: conexão é null");
            }

            // Verificar se o produto existe e obter o status atual
            String sqlVerifica = "SELECT status FROM produtos WHERE id = ?";
            prepVerifica = conn.prepareStatement(sqlVerifica);
            prepVerifica.setInt(1, id);
            rs = prepVerifica.executeQuery();

            if (!rs.next()) {
                throw new RuntimeException("Produto com ID " + id + " não encontrado!");
            }

            String statusAtual = rs.getString("status");
            if (!statusAtual.equals("À Venda")) {
                throw new RuntimeException("Produto com ID " + id + " não está disponível para venda (status: " + statusAtual + ").");
            }

            // Atualizar o status do produto para "Vendido"
            String sqlAtualiza = "UPDATE produtos SET status = ? WHERE id = ?";
            prepAtualiza = conn.prepareStatement(sqlAtualiza);
            prepAtualiza.setString(1, "Vendido");
            prepAtualiza.setInt(2, id);

            int linhasAfetadas = prepAtualiza.executeUpdate();
            if (linhasAfetadas == 0) {
                throw new RuntimeException("Erro ao atualizar o status do produto com ID " + id + ".");
            }
        } catch (SQLException erro) {
            throw new RuntimeException("Erro ao vender produto: " + erro.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (prepVerifica != null) prepVerifica.close();
                if (prepAtualiza != null) prepAtualiza.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                throw new RuntimeException("Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    public ArrayList<ProdutosDTO> listarProdutosVendidos() {
    ArrayList<ProdutosDTO> listaVendidos = new ArrayList<>();
    Connection conn = null;
    PreparedStatement prep = null;
    ResultSet rs = null;
    try {
        conn = new conectaDAO().connectDB();
        if (conn == null) {
            throw new RuntimeException("Falha ao conectar ao banco: conexão é null");
        }

        String sql = "SELECT * FROM produtos WHERE status = ?";
        prep = conn.prepareStatement(sql);
        prep.setString(1, "Vendido");
        rs = prep.executeQuery();

        while (rs.next()) {
            ProdutosDTO produto = new ProdutosDTO();
            produto.setId(rs.getInt("id"));
            produto.setNome(rs.getString("nome"));
            produto.setValor(rs.getInt("valor"));
            produto.setStatus(rs.getString("status"));
            listaVendidos.add(produto);
        }
    } catch (SQLException erro) {
        throw new RuntimeException("Erro ao listar produtos vendidos: " + erro.getMessage());
    } finally {
        try {
            if (rs != null) rs.close();
            if (prep != null) prep.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar conexão: " + e.getMessage());
        }
    }
    return listaVendidos;
}
}