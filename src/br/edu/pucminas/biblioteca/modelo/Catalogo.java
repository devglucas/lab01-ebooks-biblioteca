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

    /**
     * Remove um eBook do catálogo (HU06). Retorna {@code true} se o eBook
     * estava presente e foi removido.
     */
    public boolean removerEBook(EBook ebook) {
        return ebook != null && ebooks.remove(ebook);
    }

    /**
     * Localiza um eBook pelo título (HU06 - apoio ao "alterar"/"remover").
     * A comparação ignora diferenças de caixa. Retorna {@code null} se nenhum
     * eBook corresponder.
     */
    public EBook buscarPorTitulo(String titulo) {
        if (titulo == null) {
            return null;
        }
        for (EBook ebook : ebooks) {
            if (titulo.equalsIgnoreCase(ebook.getTitulo())) {
                return ebook;
            }
        }
        return null;
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
