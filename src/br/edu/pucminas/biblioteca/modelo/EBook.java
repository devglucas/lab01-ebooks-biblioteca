package br.edu.pucminas.biblioteca.modelo;

public class EBook {

    private String titulo;
    private String editora;
    private FormatoArquivo formato;
    private Categoria categoria;
    private Licenca licenca;

    public EBook(String titulo, String editora, FormatoArquivo formato, Categoria categoria) {
        this.titulo = titulo;
        this.editora = editora;
        this.formato = formato;
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public FormatoArquivo getFormato() {
        return formato;
    }

    public void setFormato(FormatoArquivo formato) {
        this.formato = formato;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Licenca getLicenca() {
        return licenca;
    }

    public void setLicenca(Licenca licenca) {
        this.licenca = licenca;
    }
}
