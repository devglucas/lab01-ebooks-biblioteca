package br.edu.pucminas.biblioteca.modelo;

import java.util.ArrayList;
import java.util.List;

public class EquipeBiblioteca {

    private Catalogo catalogo;
    private List<Aluno> alunos;
    private List<Bibliotecario> bibliotecarios;

    public EquipeBiblioteca(Catalogo catalogo) {
        this.catalogo = catalogo;
        this.alunos = new ArrayList<>();
        this.bibliotecarios = new ArrayList<>();
    }

    public void cadastrarEBook(EBook ebook) {
        if (ebook != null && catalogo != null) {
            catalogo.adicionarEBook(ebook);
        }
    }

    public void cadastrarAluno(Aluno aluno) {
        if (aluno != null && !alunos.contains(aluno)) {
            alunos.add(aluno);
        }
    }

    public void cadastrarBibliotecario(Bibliotecario bib) {
        if (bib != null && !bibliotecarios.contains(bib)) {
            bibliotecarios.add(bib);
        }
    }

    // --- HU06/07/08: remoção (o "alterar" é feito localizando a entidade com
    // os finders abaixo e usando os setters já existentes) ---

    public boolean removerEBook(EBook ebook) {
        return catalogo != null && catalogo.removerEBook(ebook);
    }

    public boolean removerAluno(Aluno aluno) {
        return aluno != null && alunos.remove(aluno);
    }

    public boolean removerBibliotecario(Bibliotecario bib) {
        return bib != null && bibliotecarios.remove(bib);
    }

    // --- Finders ---

    public Aluno buscarAlunoPorMatricula(String matricula) {
        if (matricula == null) {
            return null;
        }
        for (Aluno aluno : alunos) {
            if (matricula.equals(aluno.getMatricula())) {
                return aluno;
            }
        }
        return null;
    }

    public Aluno buscarAlunoPorEmail(String email) {
        if (email == null) {
            return null;
        }
        for (Aluno aluno : alunos) {
            if (email.equalsIgnoreCase(aluno.getEmail())) {
                return aluno;
            }
        }
        return null;
    }

    public Bibliotecario buscarBibliotecarioPorRegistro(String registro) {
        if (registro == null) {
            return null;
        }
        for (Bibliotecario bib : bibliotecarios) {
            if (registro.equals(bib.getRegistro())) {
                return bib;
            }
        }
        return null;
    }

    /**
     * Autentica um usuário (HU01 / Passo 8). Procura entre alunos e
     * bibliotecários um cujo e-mail corresponda e cuja senha seja válida.
     * Retorna o {@link Usuario} autenticado ou {@code null} se as credenciais
     * não conferirem.
     */
    public Usuario autenticar(String email, String senha) {
        if (email == null || senha == null) {
            return null;
        }
        for (Aluno aluno : alunos) {
            if (email.equalsIgnoreCase(aluno.getEmail()) && aluno.validarSenha(senha)) {
                return aluno;
            }
        }
        for (Bibliotecario bib : bibliotecarios) {
            if (email.equalsIgnoreCase(bib.getEmail()) && bib.validarSenha(senha)) {
                return bib;
            }
        }
        return null;
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public List<Aluno> getAlunos() {
        return new ArrayList<>(alunos);
    }

    public List<Bibliotecario> getBibliotecarios() {
        return new ArrayList<>(bibliotecarios);
    }
}
