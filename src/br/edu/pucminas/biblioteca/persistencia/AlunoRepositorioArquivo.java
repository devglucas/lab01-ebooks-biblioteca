package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.Aluno;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Persiste alunos em arquivo de texto, um por linha, campos separados por
 * ponto e vírgula: {@code nome;email;senha;matricula}.
 */
public class AlunoRepositorioArquivo {

    private static final String ARQUIVO = "dados/alunos.txt";

    public void salvar(List<Aluno> alunos) throws IOException {
        File arquivo = new File(ARQUIVO);
        File pasta = arquivo.getParentFile();
        if (pasta != null) {
            pasta.mkdirs();
        }
        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (Aluno aluno : alunos) {
                escritor.println(aluno.getNome() + ";" + aluno.getEmail()
                        + ";" + aluno.getSenha() + ";" + aluno.getMatricula());
            }
        }
    }

    public List<Aluno> carregar() throws IOException {
        List<Aluno> alunos = new ArrayList<>();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return alunos;
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
                alunos.add(new Aluno(campos[0], campos[1], campos[2], campos[3]));
            }
        }
        return alunos;
    }
}
