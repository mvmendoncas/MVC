/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.List;
import model.ClienteDAO;
import model.ModelCliente;

/**
 * @author thiag
 */
public class ControllerCliente {

  private ClienteDAO dao = new ClienteDAO();

  
    public boolean cadastrarCliente(String nome, String cpf, int idade) {
     
        if (nome == null || nome.trim().isEmpty() || cpf == null || cpf.trim().isEmpty()) {
            System.err.println("Erro: Nome e CPF não podem ser vazios.");
            return false;
        }
        
        // cria o objeto ModelCliente
        ModelCliente novoCliente = new ModelCliente(nome, cpf, idade);
        
        // delega a persistência ao DAO
        dao.salvar(novoCliente);
        return true;
    }
    
 
    public ModelCliente buscarClientePorCpf(String cpf) {
        List<ModelCliente> clientes = dao.listarTodos();
        
        // busca o cliente na lista
        for (ModelCliente cliente : clientes) {
            if (cliente.getCpf().equals(cpf)) {
                return cliente; // Cliente encontrado
            }
        }
        return null; // cliente nao encontrado
    }
 
    public boolean atualizarCliente(String nome, String cpf, int novaIdade) {
        // cpf é a chave
        ModelCliente clienteAtualizado = new ModelCliente(nome, cpf, novaIdade);
        
        // delega a persistência ao DAO
        return dao.atualizar(clienteAtualizado);
    }

   
    public boolean deletarCliente(String cpf) {
        // delega a deleção ao DAO
        return dao.deletar(cpf);
    }
}
