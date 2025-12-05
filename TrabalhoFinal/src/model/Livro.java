package model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  **Representa uma Mídia do tipo Livro.**
 *
 * <p>Esta classe estende a classe abstrata {@link Midia}, utilizando o campo
 * {@code duracao} (da classe pai) para armazenar a **contagem de páginas** do livro.</p>
 *
 * <p>Adiciona o atributo específico {@code autores} (armazenado como uma {@code String}
 * separada por vírgulas).</p>
 *
 * @see Midia
 */
public class Livro extends Midia{
	private String autores;

    /**
     *  Construtor completo para criar um novo objeto Livro (sem ID, que será gerado pelo DAO).
     *
     * @param local O caminho físico onde o arquivo (e.g., PDF, ePub) está armazenado.
     * @param tamanhoBytes O tamanho do arquivo em bytes.
     * @param titulo O título do livro.
     * @param paginas O número total de páginas do livro (armazenado em {@code duracao}).
     * @param categoria A categoria ou gênero do livro (ex: Ficção, Técnico).
     * @param autores A lista de autores como uma única {@code String}, tipicamente separada por vírgulas.
     */
    public Livro(String local, long tamanhoBytes, String titulo, long paginas, String categoria, String autores) {
        super(local, tamanhoBytes, titulo, paginas, categoria);
        this.autores = autores;
    }

    /**
     *  Construtor completo para recriar um objeto Livro a partir do armazenamento persistente (com ID já definido).
     *
     * @param id O ID único do livro.
     * @param local O caminho físico onde o arquivo está armazenado.
     * @param tamanhoBytes O tamanho do arquivo em bytes.
     * @param titulo O título do livro.
     * @param paginas O número total de páginas do livro (armazenado em {@code duracao}).
     * @param categoria A categoria ou gênero do livro.
     * @param autores A lista de autores como uma única {@code String}, tipicamente separada por vírgulas.
     */
    public Livro(int id, String local, long tamanhoBytes, String titulo, long paginas, String categoria, String autores) {
        super(id, local, tamanhoBytes, titulo, paginas, categoria);
        this.autores = autores;
    }

    /**
     *  Retorna a lista de autores como uma {@code List<String>}.
     * <p>A string {@code autores} é dividida por vírgulas (",") e os espaços em branco
     * (whitespace) são removidos de cada nome antes de formar a lista.</p>
     *
     * @return Uma lista de strings contendo cada autor, ou uma lista vazia se a string {@code autores} estiver vazia ou nula.
     */
    public List<String> getAutoresList() {
        if (autores == null || autores.trim().isEmpty()) return Arrays.asList();
        return Arrays.stream(autores.split(",")).map(String::trim).collect(Collectors.toList());
    }

    /**
     *  Obtém a string original dos autores.
     *
     * @return A string contendo todos os autores (ex: "Autor 1, Autor 2").
     */
    public String getAutores() { return autores; }

    /**
     * Define a string dos autores.
     *
     * @param autores A nova string de autores.
     */
    public void setAutores(String autores) {
    	this.autores = autores;
    }

    /**
     *  Retorna o tipo específico desta mídia.
     *
     * @return A string literal "LIVRO".
     */
    @Override
    public String getTipo() {
    	return "LIVRO";
    }

    /**
     * Retorna uma string formatada com os atributos específicos da classe {@code Livro}.
     *
     * <p>Exibe os autores e o número de páginas (que é armazenado no atributo {@code duracao}).</p>
     *
     * @return Uma string contendo os autores e a contagem de páginas.
     */
    @Override
    public String exibirAtributosEspecificos() {
        return "Autores: " + autores + " | Páginas: " + getDuracao();
    }
}