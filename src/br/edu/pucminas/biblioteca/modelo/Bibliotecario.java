package br.edu.pucminas.biblioteca.modelo;

import java.util.ArrayList;
import java.util.List;

public class Bibliotecario extends Usuario {

    private String registro;

    public Bibliotecario(String nome, String email, String senha, String registro) {
        super(nome, email, senha);
        this.registro = registro;
    }

    public List<Aluno> consultarAlunosComEBook(List<Aluno> alunos, EBook ebook) {
        List<Aluno> resultado = new ArrayList<>();
        if (alunos == null || ebook == null) {
            return resultado;
        }
        for (Aluno aluno : alunos) {
            if (aluno == null || aluno.getEstante() == null) {
                continue;
            }
            for (ItemEstante item : aluno.getEstante().consultarItens()) {
                if (item.getEbook() == ebook) {
                    resultado.add(aluno);
                    break;
                }
            }
        }
        return resultado;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }
}
