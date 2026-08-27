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

    /**
     * Adiciona um eBook à estante do aluno e, em caso de sucesso, notifica o
     * sistema de estatísticas de uso (HU10). Retorna {@code false} quando a
     * adição é recusada por alguma regra de negócio (limite de eBooks ou de
     * licenças simultâneas).
     */
    public boolean adicionarEBook(ItemEstante item, SistemaEstatisticas estatisticas) {
        boolean adicionado = estante.adicionarItem(item);
        if (adicionado && estatisticas != null) {
            estatisticas.receberNotificacao(this, item.getEbook());
        }
        return adicionado;
    }

    /**
     * Remove um eBook da estante do aluno (HU03), liberando a licença ocupada.
     */
    public void removerEBook(ItemEstante item) {
        estante.removerItem(item);
    }
}
