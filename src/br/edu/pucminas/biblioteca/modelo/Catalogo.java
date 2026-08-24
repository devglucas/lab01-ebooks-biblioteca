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

    public List<EBook> listarEBooks() {
        // TODO: implementar na Sprint 3
        return ebooks;
    }

    public void renovarCatalogo() {
        // TODO: implementar na Sprint 3
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
}
