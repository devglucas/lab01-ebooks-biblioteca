package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.Aluno;
import br.edu.pucminas.biblioteca.modelo.EBook;
import br.edu.pucminas.biblioteca.modelo.ItemEstante;
import br.edu.pucminas.biblioteca.modelo.TipoLeitura;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Persiste o conteúdo das estantes pessoais dos alunos, uma referência por
 * linha, campos separados por ponto e vírgula:
 * {@code matricula;tituloEbook;tipoLeitura}.
 *
 * <p>Como um {@link ItemEstante} referencia um {@link Aluno} e um {@link EBook}
 * (objetos que já existem em memória), a carga reconecta as referências a
 * partir das listas de alunos e eBooks previamente carregadas.</p>
 */
public class EstanteRepositorioArquivo {

    private static final String ARQUIVO = "dados/estantes.txt";

    /**
     * Grava, para cada aluno, uma linha por eBook presente em sua estante.
     */
    public void salvar(List<Aluno> alunos) throws IOException {
        File arquivo = new File(ARQUIVO);
        File pasta = arquivo.getParentFile();
        if (pasta != null) {
            pasta.mkdirs();
        }
        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (Aluno aluno : alunos) {
                if (aluno.getEstante() == null) {
                    continue;
                }
                for (ItemEstante item : aluno.getEstante().consultarItens()) {
                    if (item.getEbook() == null) {
                        continue;
                    }
                    escritor.println(aluno.getMatricula() + ";"
                            + item.getEbook().getTitulo() + ";"
                            + item.getTipoLeitura());
                }
            }
        }
    }

    /**
     * Lê o arquivo e recria os itens nas estantes dos alunos informados,
     * reconectando cada item ao aluno (por matrícula) e ao eBook (por título).
     * Adicionar via {@code adicionarItem} também reocupa a licença, mantendo a
     * contagem de acessos simultâneos consistente após reabrir o programa.
     */
    public void carregar(List<Aluno> alunos, List<EBook> ebooks) throws IOException {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return;
        }

        Map<String, Aluno> alunosPorMatricula = new HashMap<>();
        for (Aluno aluno : alunos) {
            alunosPorMatricula.put(aluno.getMatricula(), aluno);
        }
        Map<String, EBook> ebooksPorTitulo = new HashMap<>();
        for (EBook ebook : ebooks) {
            ebooksPorTitulo.put(ebook.getTitulo(), ebook);
        }

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }
                String[] campos = linha.split(";");
                if (campos.length < 3) {
                    continue;
                }
                Aluno aluno = alunosPorMatricula.get(campos[0]);
                EBook ebook = ebooksPorTitulo.get(campos[1]);
                if (aluno == null || ebook == null) {
                    continue;
                }
                try {
                    TipoLeitura tipo = TipoLeitura.valueOf(campos[2].trim());
                    aluno.getEstante().adicionarItem(new ItemEstante(tipo, ebook));
                } catch (IllegalArgumentException e) {
                    // Tipo de leitura inválido no arquivo: ignora a linha.
                }
            }
        }
    }
}
