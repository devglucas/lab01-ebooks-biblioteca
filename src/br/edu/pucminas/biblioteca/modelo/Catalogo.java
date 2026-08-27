package br.edu.pucminas.biblioteca.modelo;

import java.util.ArrayList;
import java.util.List;

public class Catalogo {

    private String semestre;
    private List<EBook> ebooks;

    public Catalogo(String semestre) {
        this.semestre = semestre;
        this.ebooks = new ArrayList<>();
    }

    public void adicionarEBook(EBook ebook) {
        if (ebook != null && !ebooks.contains(ebook)) {
            ebooks.add(ebook);
        }
    }

    public List<EBook> listarEBooks() {
        return new ArrayList<>(ebooks);
    }

    public void renovarCatalogo() {
        // Ao renovar o catálogo para um novo semestre, os acessos de todas as
        // licenças são zerados, liberando novamente os 60 acessos simultâneos.
        for (EBook ebook : ebooks) {
            if (ebook.getLicenca() != null) {
                ebook.getLicenca().setAcessosAtuais(0);
            }
        }
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
}
