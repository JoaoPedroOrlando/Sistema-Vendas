/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistema.dao;

import br.com.sistema.jdbc.ConnectionFactory;
import br.com.sistema.model.Clientes;
import br.com.sistema.model.Vendas;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Tampelini
 */
public class VendasDAO {

    private Connection con;

    public VendasDAO() {
        this.con = new ConnectionFactory().getConnection();
    }

    //Cadastrar Venda
    public void cadastrarVenda(Vendas obj) {
        try {

            String sql = "insert into tb_vendas (cliente_id, data_venda,total_venda,observacoes) values (?,?,?,?)";
            //2 passo - conectar o banco de dados e organizar o comando sql
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, obj.getCliente().getId());
            stmt.setString(2, obj.getData_venda());
            stmt.setDouble(3, obj.getTotal_venda());
            stmt.setString(4, obj.getObs());

            stmt.execute();
            stmt.close();

        } catch (Exception erro) {

            JOptionPane.showMessageDialog(null, "Erro : " + erro);

        }

    }

    //Retorna a ultima venda
    public int retornaUltimaVenda() {
        try {
            int idvenda = 0;

            String sql = "select max(id) id from tb_vendas";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Vendas p = new Vendas();

                p.setId(rs.getInt("id"));
                idvenda = p.getId();
            }

            return idvenda;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    //Metodo que filtra Vendas por Datas
    public List<Vendas> listarVendasPorPeriodo(LocalDate data_inicio, LocalDate data_fim) {
        try {

            //1 passo criar a lista
            List<Vendas> lista = new ArrayList<>();

            //2 passo - criar o sql , organizar e executar.
            String sql = "select v.id ,  date_format(v.data_venda,'%d/%m/%Y') as data_formatada , c.nome, v.total_venda, v.observacoes  from tb_vendas as v  "
                    + " inner join tb_clientes as c on(v.cliente_id = c.id) where v.data_venda BETWEEN ? AND ?";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, data_inicio.toString());
            stmt.setString(2, data_fim.toString());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Vendas obj = new Vendas();
                Clientes c = new Clientes();

                obj.setId(rs.getInt("v.id"));
                obj.setData_venda(rs.getString("data_formatada"));
                c.setNome(rs.getString("c.nome"));
                obj.setTotal_venda(rs.getDouble("v.total_venda"));
                obj.setObs(rs.getString("v.observacoes"));

                obj.setCliente(c);

                lista.add(obj);
            }

            return lista;

        } catch (SQLException erro) {

            JOptionPane.showMessageDialog(null, "Erro :" + erro);
            return null;
        }

    }

    //Metodo que calcula total da venda por data
    public double retornaTotalVendaPorData(LocalDate data_venda) {
        try {

            double totalvenda = 0;

            String sql = "select sum(total_venda) as total from tb_vendas where data_venda = ?";
            
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, data_venda.toString());

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalvenda = rs.getDouble("total");
            }

            return totalvenda;
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    //verifica se há estoque
    public int verificaEstoque(int idProduto){
        
        try{
            //1 abre a conexao com o banco
            Connection con = new ConnectionFactory().getConnection();
            //2 cria o comando sql
             String sql = "select qtd_estoque from tb_produtos where id =?;";
            //3 preparando o comando sql
            PreparedStatement stmt = con.prepareStatement(sql);
            //4 colocando os valores no comando sql
            stmt.setString(1,Integer.toString(idProduto));
            //5 o select retorna uma lista 
            ResultSet rs = stmt.executeQuery();
            //6 fecha a conexão com  o banco
            //con.close();
            //7 envia a quantidade do item
            if (rs.next()){
                // nome da coluna
                return  rs.getInt("qtd_estoque");
            } else {
                return 0;
            }
        } 
        catch (SQLException erro) {
            JOptionPane.showMessageDialog(null, "Erro: " + erro);
            return 0;
        }
    }
    
    public void atualizaEstoque(int qtd, int idProduto){
        try{
            //1 abre a conexao com o banco
            Connection con = new ConnectionFactory().getConnection();
            //2 cria o comando sql
            String sql = "update tb_produtos set  qtd_estoque=? where id =?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1,Integer.toString(qtd));
            stmt.setString(2,Integer.toString(idProduto));
             //3 passo - executar o comando sql
            stmt.execute();
            stmt.close();
            
        }catch( SQLException erro ){
            JOptionPane.showMessageDialog(null, "Erro: " + erro);
        }
    }   
}
