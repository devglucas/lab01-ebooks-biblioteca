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
        // TODO: implementar na Sprint 3
        return false;
    }

    public void ocuparLicenca() {
        // TODO: implementar na Sprint 3
    }

    public void liberarLicenca() {
        // TODO: implementar na Sprint 3
    }

    public boolean avaliarRenovacao(int qtdAlunos) {
        // TODO: implementar na Sprint 3
        return false;
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
