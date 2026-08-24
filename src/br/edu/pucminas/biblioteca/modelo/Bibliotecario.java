package br.edu.pucminas.biblioteca.modelo;

public class Bibliotecario extends Usuario {

    private String registro;

    public Bibliotecario(String nome, String email, String senha, String registro) {
        super(nome, email, senha);
        this.registro = registro;
    }

    public String getRegistro() {
        return registro;
    }

    public void setRegistro(String registro) {
        this.registro = registro;
    }
}
