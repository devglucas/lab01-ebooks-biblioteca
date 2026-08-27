package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.Bibliotecario;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Persiste bibliotecários em arquivo de texto, um por linha, campos separados
 * por ponto e vírgula: {@code nome;email;senha;registro}.
 */
public class BibliotecarioRepositorioArquivo {

    private static final String ARQUIVO = "dados/bibliotecarios.txt";

    public void salvar(List<Bibliotecario> bibliotecarios) throws IOException {
        File arquivo = new File(ARQUIVO);
        File pasta = arquivo.getParentFile();
        if (pasta != null) {
            pasta.mkdirs();
        }
        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (Bibliotecario bib : bibliotecarios) {
                escritor.println(bib.getNome() + ";" + bib.getEmail()
                        + ";" + bib.getSenha() + ";" + bib.getRegistro());
            }
        }
    }

    public List<Bibliotecario> carregar() throws IOException {
        List<Bibliotecario> bibliotecarios = new ArrayList<>();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return bibliotecarios;
        }
        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }
                String[] campos = linha.split(";");
                if (campos.length < 4) {
                    continue;
                }
                bibliotecarios.add(new Bibliotecario(campos[0], campos[1], campos[2], campos[3]));
            }
        }
        return bibliotecarios;
    }
}
