package br.edu.pucminas.biblioteca;

import br.edu.pucminas.biblioteca.modelo.Aluno;
import br.edu.pucminas.biblioteca.modelo.Bibliotecario;
import br.edu.pucminas.biblioteca.modelo.Catalogo;
import br.edu.pucminas.biblioteca.modelo.Categoria;
import br.edu.pucminas.biblioteca.modelo.EBook;
import br.edu.pucminas.biblioteca.modelo.EquipeBiblioteca;
import br.edu.pucminas.biblioteca.modelo.EstantePessoal;
import br.edu.pucminas.biblioteca.modelo.FormatoArquivo;
import br.edu.pucminas.biblioteca.modelo.ItemEstante;
import br.edu.pucminas.biblioteca.modelo.Licenca;
import br.edu.pucminas.biblioteca.modelo.SistemaEstatisticas;
import br.edu.pucminas.biblioteca.modelo.TipoLeitura;
import br.edu.pucminas.biblioteca.modelo.Usuario;
import br.edu.pucminas.biblioteca.persistencia.AlunoRepositorioArquivo;
import br.edu.pucminas.biblioteca.persistencia.BibliotecarioRepositorioArquivo;
import br.edu.pucminas.biblioteca.persistencia.EBookRepositorioArquivo;
import br.edu.pucminas.biblioteca.persistencia.EstanteRepositorioArquivo;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Interface de linha de comando do Sistema de Gestão de eBooks (Passos 7, 8 e
 * 10). Faz login, oferece um menu sensível ao tipo de usuário e persiste os
 * dados em arquivo ao sair. O tratamento de erros é amigável: entradas
 * inválidas e regras de negócio violadas não derrubam o programa.
 */
public class MenuPrincipal {

    private static final String SEMESTRE = "2026/2";
    private static final int MAX_LICENCAS = 60;

    private final Scanner leitor = new Scanner(System.in);
    private final SistemaEstatisticas estatisticas = new SistemaEstatisticas();

    private final EBookRepositorioArquivo ebookRepo = new EBookRepositorioArquivo();
    private final AlunoRepositorioArquivo alunoRepo = new AlunoRepositorioArquivo();
    private final BibliotecarioRepositorioArquivo bibRepo = new BibliotecarioRepositorioArquivo();
    private final EstanteRepositorioArquivo estanteRepo = new EstanteRepositorioArquivo();

    private EquipeBiblioteca equipe;

    public static void main(String[] args) {
        new MenuPrincipal().executar();
    }

    private void executar() {
        try {
            carregarDados();
        } catch (IOException e) {
            System.out.println("Falha ao carregar os dados: " + e.getMessage());
            equipe = new EquipeBiblioteca(new Catalogo(SEMESTRE));
        }

        Usuario usuario = autenticarUsuario();
        if (usuario == null) {
            System.out.println("Numero de tentativas excedido. Encerrando.");
            return;
        }

        System.out.println("\nBem-vindo(a), " + usuario.getNome() + "!");
        lacoDeMenu(usuario);

        try {
            salvarDados();
            System.out.println("Dados salvos. Ate logo!");
        } catch (IOException e) {
            System.out.println("Falha ao salvar os dados: " + e.getMessage());
        }
        leitor.close();
    }

    // ------------------------------------------------------------------
    // Carga / gravação
    // ------------------------------------------------------------------

    private void carregarDados() throws IOException {
        Catalogo catalogo = new Catalogo(SEMESTRE);
        List<EBook> ebooks = ebookRepo.carregar();
        for (EBook ebook : ebooks) {
            if (ebook.getLicenca() == null) {
                ebook.setLicenca(new Licenca(MAX_LICENCAS, SEMESTRE));
            }
            catalogo.adicionarEBook(ebook);
        }

        equipe = new EquipeBiblioteca(catalogo);
        for (Aluno aluno : alunoRepo.carregar()) {
            equipe.cadastrarAluno(aluno);
        }
        for (Bibliotecario bib : bibRepo.carregar()) {
            equipe.cadastrarBibliotecario(bib);
        }

        // Reconecta as estantes com os alunos e eBooks já carregados.
        estanteRepo.carregar(equipe.getAlunos(), catalogo.listarEBooks());

        semearDadosIniciais();
    }

    /**
     * Na primeira execução (sem usuários persistidos) cria um bibliotecário e
     * um aluno padrão, para que seja possível fazer login e testar o sistema.
     */
    private void semearDadosIniciais() {
        if (!equipe.getAlunos().isEmpty() || !equipe.getBibliotecarios().isEmpty()) {
            return;
        }
        equipe.cadastrarBibliotecario(
                new Bibliotecario("Bibliotecario Padrao", "admin@biblioteca.edu", "admin", "B001"));
        equipe.cadastrarAluno(
                new Aluno("Aluno Exemplo", "aluno@biblioteca.edu", "aluno", "A001"));
        System.out.println("Primeira execucao: usuarios padrao criados.");
        System.out.println("  Bibliotecario -> email: admin@biblioteca.edu | senha: admin");
        System.out.println("  Aluno         -> email: aluno@biblioteca.edu | senha: aluno");
    }

    private void salvarDados() throws IOException {
        ebookRepo.salvar(equipe.getCatalogo().listarEBooks());
        alunoRepo.salvar(equipe.getAlunos());
        bibRepo.salvar(equipe.getBibliotecarios());
        estanteRepo.salvar(equipe.getAlunos());
    }

    // ------------------------------------------------------------------
    // Autenticação (HU01 / Passo 8)
    // ------------------------------------------------------------------

    private Usuario autenticarUsuario() {
        System.out.println("=== Sistema de Gestao de eBooks ===");
        for (int tentativas = 3; tentativas > 0; tentativas--) {
            String email = lerLinha("E-mail: ");
            String senha = lerLinha("Senha: ");
            Usuario usuario = equipe.autenticar(email, senha);
            if (usuario != null) {
                return usuario;
            }
            System.out.println("Credenciais invalidas. Tentativas restantes: " + (tentativas - 1));
        }
        return null;
    }

    // ------------------------------------------------------------------
    // Laço de menu
    // ------------------------------------------------------------------

    private void lacoDeMenu(Usuario usuario) {
        boolean continuar = true;
        while (continuar) {
            exibirMenu(usuario);
            int opcao;
            try {
                opcao = Integer.parseInt(lerLinha("Escolha uma opcao: "));
            } catch (NumberFormatException e) {
                System.out.println("Digite um numero valido.");
                continue;
            }

            try {
                continuar = processarOpcao(usuario, opcao);
            } catch (Exception e) {
                // Rede de seguranca: nenhuma acao deve derrubar o programa.
                System.out.println("Nao foi possivel concluir a acao: " + e.getMessage());
            }
        }
    }

    private void exibirMenu(Usuario usuario) {
        System.out.println("\n----- MENU -----");
        if (usuario instanceof Aluno) {
            System.out.println("1. Adicionar eBook a estante");
            System.out.println("2. Remover eBook da estante");
            System.out.println("3. Consultar minha estante");
        } else if (usuario instanceof Bibliotecario) {
            System.out.println("4. Consultar alunos com um eBook");
            System.out.println("5. Cadastrar eBook");
            System.out.println("6. Alterar eBook");
            System.out.println("7. Remover eBook");
            System.out.println("8. Gerenciar alunos");
            System.out.println("9. Gerenciar bibliotecarios");
        }
        System.out.println("10. Listar catalogo");
        System.out.println("0. Salvar e sair");
    }

    /** @return {@code false} quando o usuário pede para sair. */
    private boolean processarOpcao(Usuario usuario, int opcao) {
        boolean aluno = usuario instanceof Aluno;
        boolean bib = usuario instanceof Bibliotecario;
        switch (opcao) {
            case 1:
                if (aluno) adicionarEBookEstante((Aluno) usuario); else opcaoNaoPermitida();
                break;
            case 2:
                if (aluno) removerEBookEstante((Aluno) usuario); else opcaoNaoPermitida();
                break;
            case 3:
                if (aluno) consultarEstante((Aluno) usuario); else opcaoNaoPermitida();
                break;
            case 4:
                if (bib) consultarAlunosComEBook((Bibliotecario) usuario); else opcaoNaoPermitida();
                break;
            case 5:
                if (bib) cadastrarEBook(); else opcaoNaoPermitida();
                break;
            case 6:
                if (bib) alterarEBook(); else opcaoNaoPermitida();
                break;
            case 7:
                if (bib) removerEBook(); else opcaoNaoPermitida();
                break;
            case 8:
                if (bib) gerenciarAlunos(); else opcaoNaoPermitida();
                break;
            case 9:
                if (bib) gerenciarBibliotecarios(); else opcaoNaoPermitida();
                break;
            case 10:
                listarCatalogo();
                break;
            case 0:
                return false;
            default:
                System.out.println("Opcao invalida, tente novamente.");
        }
        return true;
    }

    private void opcaoNaoPermitida() {
        System.out.println("Opcao nao disponivel para o seu perfil.");
    }

    // ------------------------------------------------------------------
    // Ações do aluno (HU02, HU03, HU04, HU10)
    // ------------------------------------------------------------------

    private void adicionarEBookEstante(Aluno aluno) {
        EBook ebook = selecionarEBookDoCatalogo();
        if (ebook == null) {
            return;
        }
        TipoLeitura tipo = lerTipoLeitura();
        if (tipo == null) {
            return;
        }
        EstantePessoal estante = aluno.getEstante();
        boolean ok = aluno.adicionarEBook(new ItemEstante(tipo, ebook), estatisticas);
        if (ok) {
            System.out.println("eBook adicionado a estante.");
        } else {
            System.out.println("Nao foi possivel adicionar: " + motivoRecusa(estante, tipo, ebook));
        }
    }

    private String motivoRecusa(EstantePessoal estante, TipoLeitura tipo, EBook ebook) {
        if (tipo == TipoLeitura.OBRIGATORIA
                && estante.contarItensPorTipo(TipoLeitura.OBRIGATORIA) >= estante.getMaxObrigatorios()) {
            return "limite de " + estante.getMaxObrigatorios() + " eBooks obrigatorios atingido.";
        }
        if (tipo == TipoLeitura.LIVRE
                && estante.contarItensPorTipo(TipoLeitura.LIVRE) >= estante.getMaxLivres()) {
            return "limite de " + estante.getMaxLivres() + " eBooks livres atingido.";
        }
        if (ebook.getLicenca() != null && !ebook.getLicenca().verificarDisponibilidade()) {
            return "limite de " + MAX_LICENCAS + " acessos simultaneos da licenca atingido.";
        }
        return "regra de negocio nao atendida.";
    }

    private void removerEBookEstante(Aluno aluno) {
        List<ItemEstante> itens = aluno.getEstante().consultarItens();
        if (itens.isEmpty()) {
            System.out.println("Sua estante esta vazia.");
            return;
        }
        for (int i = 0; i < itens.size(); i++) {
            EBook e = itens.get(i).getEbook();
            System.out.println((i + 1) + ". " + e.getTitulo() + " (" + itens.get(i).getTipoLeitura() + ")");
        }
        int idx = lerInteiro("Numero do eBook a remover (0 para cancelar): ");
        if (idx <= 0 || idx > itens.size()) {
            System.out.println("Operacao cancelada.");
            return;
        }
        aluno.removerEBook(itens.get(idx - 1));
        System.out.println("eBook removido da estante.");
    }

    private void consultarEstante(Aluno aluno) {
        List<ItemEstante> itens = aluno.getEstante().consultarItens();
        if (itens.isEmpty()) {
            System.out.println("Sua estante esta vazia.");
            return;
        }
        System.out.println("--- Sua estante ---");
        for (ItemEstante item : itens) {
            EBook e = item.getEbook();
            System.out.println("- " + e.getTitulo() + " | " + item.getTipoLeitura()
                    + " | " + e.getFormato() + " | adicionado em " + item.getDataAdicao());
        }
    }

    // ------------------------------------------------------------------
    // Ações do bibliotecário (HU05, HU06, HU07, HU08)
    // ------------------------------------------------------------------

    private void consultarAlunosComEBook(Bibliotecario bib) {
        EBook ebook = selecionarEBookDoCatalogo();
        if (ebook == null) {
            return;
        }
        List<Aluno> alunos = bib.consultarAlunosComEBook(equipe.getAlunos(), ebook);
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno possui esse eBook na estante.");
            return;
        }
        System.out.println("Alunos com \"" + ebook.getTitulo() + "\":");
        for (Aluno a : alunos) {
            System.out.println("- " + a.getNome() + " (matricula " + a.getMatricula() + ")");
        }
    }

    private void cadastrarEBook() {
        String titulo = lerLinha("Titulo: ");
        String editora = lerLinha("Editora: ");
        FormatoArquivo formato = lerFormato();
        if (formato == null) {
            return;
        }
        Categoria categoria = lerCategoria();
        if (categoria == null) {
            return;
        }
        EBook ebook = new EBook(titulo, editora, formato, categoria);
        ebook.setLicenca(new Licenca(MAX_LICENCAS, SEMESTRE));
        equipe.cadastrarEBook(ebook);
        System.out.println("eBook cadastrado.");
    }

    private void alterarEBook() {
        EBook ebook = selecionarEBookDoCatalogo();
        if (ebook == null) {
            return;
        }
        System.out.println("Deixe em branco para manter o valor atual.");
        String titulo = lerLinha("Novo titulo (" + ebook.getTitulo() + "): ");
        if (!titulo.isEmpty()) {
            ebook.setTitulo(titulo);
        }
        String editora = lerLinha("Nova editora (" + ebook.getEditora() + "): ");
        if (!editora.isEmpty()) {
            ebook.setEditora(editora);
        }
        System.out.println("eBook atualizado.");
    }

    private void removerEBook() {
        EBook ebook = selecionarEBookDoCatalogo();
        if (ebook == null) {
            return;
        }
        if (equipe.removerEBook(ebook)) {
            System.out.println("eBook removido do catalogo.");
        } else {
            System.out.println("Nao foi possivel remover o eBook.");
        }
    }

    private void gerenciarAlunos() {
        System.out.println("1. Cadastrar  2. Alterar  3. Remover");
        int opcao = lerInteiro("Opcao: ");
        switch (opcao) {
            case 1: {
                String nome = lerLinha("Nome: ");
                String email = lerLinha("E-mail: ");
                String senha = lerLinha("Senha: ");
                String matricula = lerLinha("Matricula: ");
                equipe.cadastrarAluno(new Aluno(nome, email, senha, matricula));
                System.out.println("Aluno cadastrado.");
                break;
            }
            case 2: {
                Aluno aluno = equipe.buscarAlunoPorMatricula(lerLinha("Matricula do aluno: "));
                if (aluno == null) {
                    System.out.println("Aluno nao encontrado.");
                    break;
                }
                System.out.println("Deixe em branco para manter o valor atual.");
                String nome = lerLinha("Novo nome (" + aluno.getNome() + "): ");
                if (!nome.isEmpty()) aluno.setNome(nome);
                String email = lerLinha("Novo e-mail (" + aluno.getEmail() + "): ");
                if (!email.isEmpty()) aluno.setEmail(email);
                System.out.println("Aluno atualizado.");
                break;
            }
            case 3: {
                Aluno aluno = equipe.buscarAlunoPorMatricula(lerLinha("Matricula do aluno: "));
                if (aluno != null && equipe.removerAluno(aluno)) {
                    System.out.println("Aluno removido.");
                } else {
                    System.out.println("Aluno nao encontrado.");
                }
                break;
            }
            default:
                System.out.println("Opcao invalida.");
        }
    }

    private void gerenciarBibliotecarios() {
        System.out.println("1. Cadastrar  2. Alterar  3. Remover");
        int opcao = lerInteiro("Opcao: ");
        switch (opcao) {
            case 1: {
                String nome = lerLinha("Nome: ");
                String email = lerLinha("E-mail: ");
                String senha = lerLinha("Senha: ");
                String registro = lerLinha("Registro: ");
                equipe.cadastrarBibliotecario(new Bibliotecario(nome, email, senha, registro));
                System.out.println("Bibliotecario cadastrado.");
                break;
            }
            case 2: {
                Bibliotecario bib = equipe.buscarBibliotecarioPorRegistro(lerLinha("Registro: "));
                if (bib == null) {
                    System.out.println("Bibliotecario nao encontrado.");
                    break;
                }
                System.out.println("Deixe em branco para manter o valor atual.");
                String nome = lerLinha("Novo nome (" + bib.getNome() + "): ");
                if (!nome.isEmpty()) bib.setNome(nome);
                String email = lerLinha("Novo e-mail (" + bib.getEmail() + "): ");
                if (!email.isEmpty()) bib.setEmail(email);
                System.out.println("Bibliotecario atualizado.");
                break;
            }
            case 3: {
                Bibliotecario bib = equipe.buscarBibliotecarioPorRegistro(lerLinha("Registro: "));
                if (bib != null && equipe.removerBibliotecario(bib)) {
                    System.out.println("Bibliotecario removido.");
                } else {
                    System.out.println("Bibliotecario nao encontrado.");
                }
                break;
            }
            default:
                System.out.println("Opcao invalida.");
        }
    }

    private void listarCatalogo() {
        List<EBook> ebooks = equipe.getCatalogo().listarEBooks();
        if (ebooks.isEmpty()) {
            System.out.println("Catalogo vazio.");
            return;
        }
        System.out.println("--- Catalogo (" + equipe.getCatalogo().getSemestre() + ") ---");
        for (int i = 0; i < ebooks.size(); i++) {
            EBook e = ebooks.get(i);
            String acessos = e.getLicenca() != null
                    ? e.getLicenca().getAcessosAtuais() + "/" + e.getLicenca().getMaxAcessosSimultaneos()
                    : "sem licenca";
            System.out.println((i + 1) + ". " + e.getTitulo() + " - " + e.getEditora()
                    + " [" + e.getFormato() + ", " + e.getCategoria() + "] acessos: " + acessos);
        }
    }

    // ------------------------------------------------------------------
    // Utilitários de entrada
    // ------------------------------------------------------------------

    private EBook selecionarEBookDoCatalogo() {
        List<EBook> ebooks = equipe.getCatalogo().listarEBooks();
        if (ebooks.isEmpty()) {
            System.out.println("Catalogo vazio.");
            return null;
        }
        for (int i = 0; i < ebooks.size(); i++) {
            System.out.println((i + 1) + ". " + ebooks.get(i).getTitulo());
        }
        int idx = lerInteiro("Numero do eBook (0 para cancelar): ");
        if (idx <= 0 || idx > ebooks.size()) {
            System.out.println("Operacao cancelada.");
            return null;
        }
        return ebooks.get(idx - 1);
    }

    private TipoLeitura lerTipoLeitura() {
        System.out.println("Tipo de leitura: 1. OBRIGATORIA  2. LIVRE");
        int opcao = lerInteiro("Opcao: ");
        if (opcao == 1) return TipoLeitura.OBRIGATORIA;
        if (opcao == 2) return TipoLeitura.LIVRE;
        System.out.println("Tipo invalido.");
        return null;
    }

    private FormatoArquivo lerFormato() {
        System.out.println("Formato: 1. PDF  2. EPUB");
        int opcao = lerInteiro("Opcao: ");
        if (opcao == 1) return FormatoArquivo.PDF;
        if (opcao == 2) return FormatoArquivo.EPUB;
        System.out.println("Formato invalido.");
        return null;
    }

    private Categoria lerCategoria() {
        System.out.println("Categoria: 1. LITERATURA  2. TECNICO  3. PERIODICO");
        int opcao = lerInteiro("Opcao: ");
        if (opcao == 1) return Categoria.LITERATURA;
        if (opcao == 2) return Categoria.TECNICO;
        if (opcao == 3) return Categoria.PERIODICO;
        System.out.println("Categoria invalida.");
        return null;
    }

    private String lerLinha(String prompt) {
        System.out.print(prompt);
        return leitor.hasNextLine() ? leitor.nextLine().trim() : "";
    }

    private int lerInteiro(String prompt) {
        try {
            return Integer.parseInt(lerLinha(prompt));
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
