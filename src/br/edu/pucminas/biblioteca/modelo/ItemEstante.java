package br.edu.pucminas.biblioteca.modelo;

import java.util.Date;

public class ItemEstante {

    private TipoLeitura tipoLeitura;
    private Date dataAdicao;
    private EBook ebook;

    public ItemEstante(TipoLeitura tipoLeitura, EBook ebook) {
        this.tipoLeitura = tipoLeitura;
        this.dataAdicao = new Date();
        this.ebook = ebook;
    }

    public TipoLeitura getTipoLeitura() {
        return tipoLeitura;
    }

    public void setTipoLeitura(TipoLeitura tipoLeitura) {
        this.tipoLeitura = tipoLeitura;
    }

    public Date getDataAdicao() {
        return dataAdicao;
    }

    public void setDataAdicao(Date dataAdicao) {
        this.dataAdicao = dataAdicao;
    }

    public EBook getEbook() {
        return ebook;
    }

    public void setEbook(EBook ebook) {
        this.ebook = ebook;
    }
}
