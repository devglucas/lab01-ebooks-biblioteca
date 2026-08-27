package br.edu.pucminas.biblioteca.modelo;

public class Licenca {

    private int maxAcessosSimultaneos;
    private int acessosAtuais;
    private String semestre;

    public Licenca(int maxAcessosSimultaneos, String semestre) {
        if (maxAcessosSimultaneos > 60) {
            maxAcessosSimultaneos = 60;
        }
        this.maxAcessosSimultaneos = maxAcessosSimultaneos;
        this.acessosAtuais = 0;
        this.semestre = semestre;
    }

    public boolean verificarDisponibilidade() {
        return acessosAtuais < maxAcessosSimultaneos;
    }

    public void ocuparLicenca() {
        if (verificarDisponibilidade()) {
            acessosAtuais++;
        }
    }

    public void liberarLicenca() {
        if (acessosAtuais > 0) {
            acessosAtuais--;
        }
    }

    public boolean avaliarRenovacao(int qtdAlunos) {
        return qtdAlunos >= 3;
    }

    public int getMaxAcessosSimultaneos() {
        return maxAcessosSimultaneos;
    }

    public void setMaxAcessosSimultaneos(int maxAcessosSimultaneos) {
        this.maxAcessosSimultaneos = maxAcessosSimultaneos;
    }

    public int getAcessosAtuais() {
        return acessosAtuais;
    }

    public void setAcessosAtuais(int acessosAtuais) {
        this.acessosAtuais = acessosAtuais;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }
}
