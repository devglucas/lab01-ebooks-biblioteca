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
        // TODO: implementar na Sprint 3
        return false;
    }

    public void removerItem(ItemEstante item) {
        // TODO: implementar na Sprint 3
    }

    public List<ItemEstante> consultarItens() {
        // TODO: implementar na Sprint 3
        return itens;
    }

    public int contarItensPorTipo(TipoLeitura tipo) {
        // TODO: implementar na Sprint 3
        return 0;
    }

    public int getMaxObrigatorios() {
        return maxObrigatorios;
    }

    public int getMaxLivres() {
        return maxLivres;
    }
}
