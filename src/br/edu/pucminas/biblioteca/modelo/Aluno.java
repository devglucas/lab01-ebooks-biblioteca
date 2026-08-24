package br.edu.pucminas.biblioteca.modelo;

public class Aluno extends Usuario {

    private String matricula;
    private EstantePessoal estante;

    public Aluno(String nome, String email, String senha, String matricula) {
        super(nome, email, senha);
        this.matricula = matricula;
        this.estante = new EstantePessoal();
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public EstantePessoal getEstante() {
        return estante;
    }
}
