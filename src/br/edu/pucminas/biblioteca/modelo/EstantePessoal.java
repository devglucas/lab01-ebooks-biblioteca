package br.edu.pucminas.biblioteca.modelo;

import java.util.ArrayList;
import java.util.List;

public class EstantePessoal {

    private int maxObrigatorios = 4;
    private int maxLivres = 2;
    private List<ItemEstante> itens;

    public EstantePessoal() {
        this.itens = new ArrayList<>();
    }

    public boolean adicionarItem(ItemEstante item) {
        if (item == null) {
            return false;
        }

        TipoLeitura tipo = item.getTipoLeitura();
        if (tipo == TipoLeitura.OBRIGATORIA
                && contarItensPorTipo(TipoLeitura.OBRIGATORIA) >= maxObrigatorios) {
            return false;
        }
        if (tipo == TipoLeitura.LIVRE
                && contarItensPorTipo(TipoLeitura.LIVRE) >= maxLivres) {
            return false;
        }

        EBook ebook = item.getEbook();
        if (ebook != null && ebook.getLicenca() != null) {
            Licenca licenca = ebook.getLicenca();
            if (!licenca.verificarDisponibilidade()) {
                return false;
            }
            licenca.ocuparLicenca();
        }

        itens.add(item);
        return true;
    }

    public void removerItem(ItemEstante item) {
        if (item != null && itens.remove(item)) {
            EBook ebook = item.getEbook();
            if (ebook != null && ebook.getLicenca() != null) {
                ebook.getLicenca().liberarLicenca();
            }
        }
    }

    public List<ItemEstante> consultarItens() {
        return new ArrayList<>(itens);
    }

    public int contarItensPorTipo(TipoLeitura tipo) {
        int total = 0;
        for (ItemEstante item : itens) {
            if (item.getTipoLeitura() == tipo) {
                total++;
            }
        }
        return total;
    }

    public int getMaxObrigatorios() {
        return maxObrigatorios;
    }

    public int getMaxLivres() {
        return maxLivres;
    }
}
