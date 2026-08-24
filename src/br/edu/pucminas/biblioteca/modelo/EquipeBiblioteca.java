package br.edu.pucminas.biblioteca.modelo;

public class EquipeBiblioteca {

    private Catalogo catalogo;

    public EquipeBiblioteca(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public void cadastrarEBook(EBook ebook) {
        // TODO: implementar na Sprint 3
    }

    public void cadastrarAluno(Aluno aluno) {
        // TODO: implementar na Sprint 3
    }

    public void cadastrarBibliotecario(Bibliotecario bib) {
        // TODO: implementar na Sprint 3
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }
}
