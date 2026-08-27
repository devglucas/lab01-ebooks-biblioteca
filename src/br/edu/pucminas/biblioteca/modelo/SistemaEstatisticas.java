package br.edu.pucminas.biblioteca.modelo;

public class SistemaEstatisticas {

    public void receberNotificacao(Aluno aluno, EBook ebook) {
        if (aluno == null || ebook == null) {
            return;
        }
        System.out.println("[Estatisticas] O aluno " + aluno.getNome()
                + " adicionou o eBook \"" + ebook.getTitulo()
                + "\" a sua estante.");
    }
}
