package br.edu.pucminas.biblioteca.persistencia;

import br.edu.pucminas.biblioteca.modelo.Categoria;
import br.edu.pucminas.biblioteca.modelo.EBook;
import br.edu.pucminas.biblioteca.modelo.FormatoArquivo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Persiste eBooks em um arquivo de texto, um eBook por linha, com os campos
 * separados por ponto e vírgula: {@code titulo;editora;formato;categoria}.
 *
 * <p>A licença não é gravada aqui (é recriada em tempo de execução pela camada
 * de aplicação), mantendo o formato do arquivo simples.</p>
 */
public class EBookRepositorioArquivo {

    private static final String ARQUIVO = "dados/ebooks.txt";

    public void salvar(List<EBook> ebooks) throws IOException {
        File arquivo = new File(ARQUIVO);
        File pasta = arquivo.getParentFile();
        if (pasta != null) {
            pasta.mkdirs();
        }
        try (PrintWriter escritor = new PrintWriter(new FileWriter(arquivo))) {
            for (EBook ebook : ebooks) {
                escritor.println(ebook.getTitulo() + ";" + ebook.getEditora()
                        + ";" + ebook.getFormato() + ";" + ebook.getCategoria());
            }
        }
    }

    public List<EBook> carregar() throws IOException {
        List<EBook> ebooks = new ArrayList<>();
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return ebooks;
        }
        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                if (linha.trim().isEmpty()) {
                    continue;
                }
                String[] campos = linha.split(";");
                if (campos.length < 4) {
                    // Linha malformada: ignora em vez de derrubar a leitura.
                    continue;
                }
                try {
                    FormatoArquivo formato = FormatoArquivo.valueOf(campos[2].trim());
                    Categoria categoria = Categoria.valueOf(campos[3].trim());
                    ebooks.add(new EBook(campos[0], campos[1], formato, categoria));
                } catch (IllegalArgumentException e) {
                    // Enum inválido no arquivo: ignora a linha corrompida.
                }
            }
        }
        return ebooks;
    }
}
