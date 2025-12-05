/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ClienteDAO {
    // Arquivo onde os dados serao salvos
    private static final String NOME_ARQUIVO = "clientes_db.txt"; // SUBSTITUIR AQUI O NOME DO ARQUIVO QUE VC VAI CRIAR
    
    // Metodo auxiliar para converter uma linha de texto em um objeto ModelCliente
    private ModelCliente parseCliente(String linha) {
        String[] partes = linha.split(";");
        if (partes.length == 3) {
            try {
                String nome = partes[0];
                String cpf = partes[1];
                int idade = Integer.parseInt(partes[2]);
                return new ModelCliente(nome, cpf, idade);
            } catch (NumberFormatException e) {
                // Ignora linhas com idade inválida, mas avisa no console
                System.err.println("Linha ignorada por formato de idade inválido: " + linha);
            }
        }
        return null;
    }

   
    // pega todoa os clientes do arquivo e retorna uma lista de ModelCliente
    public List<ModelCliente> listarTodos() {
        List<ModelCliente> clientes = new ArrayList<>();
        File arquivo = new File(NOME_ARQUIVO);
        
        if (!arquivo.exists() || arquivo.length() == 0) {
            return clientes; // retorna lista vazia se o arquivo nao existe ou ta vazio
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                
                ModelCliente cliente = parseCliente(linha);
                if (cliente != null) {
                    clientes.add(cliente);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo de clientes: " + e.getMessage());
        }
        return clientes;
    }

    /**
     * Salva um novo cliente, coloca mais um cliente na abaixo da ultima linha escrita do arquivo
     */
    public void salvar(ModelCliente cliente) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO, true))) {
            writer.write(cliente.toFileString());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Erro ao salvar cliente no arquivo: " + e.getMessage());
        }
    }
    
    /**
     * Atualiza um cliente existente (busca pelo CPF e reescreve o arquivo)
     */
    // busca pelo cpf e se achar chama o metodo que reescreve a linha no arquivo
    public boolean atualizar(ModelCliente clienteAtualizado) {
        // le todos os clientes
        List<ModelCliente> clientes = listarTodos();
        boolean encontrado = false;
        
        // procura e atualiza na linha da memoria
        for (int i = 0; i < clientes.size(); i++) {
            if (clientes.get(i).getCpf().equals(clienteAtualizado.getCpf())) {
                clientes.set(i, clienteAtualizado); // quem vai substituir o cliente naquela linha
                encontrado = true;
                break;
            }
        }

        if (encontrado) {
            // reescreve TODO o arquivo com a nova lista com o cliente atualizado
            reescreverArquivo(clientes);
            return true;
        }
        return false;
    }

    // busca pelo cpf e se o cliente existir ele apaga aquela linha e deleta ele do banco de daods
    public boolean deletar(String cpf) {
        // le todos os clientes
        List<ModelCliente> clientes = listarTodos();
        
        // cria uma nova lista sem o cliente
        List<ModelCliente> clientesRestantes = clientes.stream()
            .filter(c -> !c.getCpf().equals(cpf))
            .collect(Collectors.toList());
        
        // ver se o cliente foi alterado ou removido
        if (clientesRestantes.size() < clientes.size()) {
            // reescreve todo arquivo sem o cliente que foi deletado
            reescreverArquivo(clientesRestantes);
            return true;
        }
        return false;
    }
    
    // metodo pra auxiliar a reescrever o arquivo
    private void reescreverArquivo(List<ModelCliente> clientes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(NOME_ARQUIVO, false))) {
            // usa "false" para sobreescrever o metodo existente
            for (ModelCliente cliente : clientes) {
                writer.write(cliente.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao reescrever arquivo: " + e.getMessage());
        }
    }
}