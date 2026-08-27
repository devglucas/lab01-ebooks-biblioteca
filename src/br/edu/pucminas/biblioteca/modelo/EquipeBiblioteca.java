package br.edu.pucminas.biblioteca.modelo;

import java.util.ArrayList;
import java.util.List;

public class EquipeBiblioteca {

    private Catalogo catalogo;
    private List<Aluno> alunos;
    private List<Bibliotecario> bibliotecarios;

    public EquipeBiblioteca(Catalogo catalogo) {
        this.catalogo = catalogo;
        this.alunos = new ArrayList<>();
        this.bibliotecarios = new ArrayList<>();
    }

    public void cadastrarEBook(EBook ebook) {
        if (ebook != null && catalogo != null) {
            catalogo.adicionarEBook(ebook);
        }
    }

    public void cadastrarAluno(Aluno aluno) {
        if (aluno != null && !alunos.contains(aluno)) {
            alunos.add(aluno);
        }
    }

    public void cadastrarBibliotecario(Bibliotecario bib) {
        if (bib != null && !bibliotecarios.contains(bib)) {
            bibliotecarios.add(bib);
        }
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public List<Aluno> getAlunos() {
        return new ArrayList<>(alunos);
    }

    public List<Bibliotecario> getBibliotecarios() {
        return new ArrayList<>(bibliotecarios);
    }
}
