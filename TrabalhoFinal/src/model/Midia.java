package model;

import controle.IdGerador;

/**
 * **Classe Abstrata Base para Representar Mídias.**
 *
 * <p>Esta classe define os **atributos comuns** a todos os tipos de mídia
 * gerenciados pela aplicação (ex: Livro, Filme, Música), como ID, localização
 * do arquivo, título e duração/tamanho.</p>
 *
 * <p>Ela também gerencia a atribuição de IDs únicos para novas mídias
 * utilizando o {@link IdGerador}.</p>
 *
 * @see controle.IdGerador
 */
public abstract class Midia {
	private int id;
    private String local;
    private long tamanhoBytes;
    private String titulo;
    private long duracao; // Usada para minutos (Filme/Música) ou número de páginas (Livro)
    private String categoria;

	// --- CONSTRUTORES ---

    /**
     *  Construtor para **novas mídias** que ainda não foram persistidas.
     * <p>Este construtor chama {@link IdGerador#novoId()} para obter e atribuir um ID único e sequencial.</p>
     *
     * @param local O caminho completo do arquivo físico no sistema.
     * @param tamanhoBytes O tamanho do arquivo em bytes.
     * @param titulo O título da mídia.
     * @param duracao A duração da mídia (em minutos para vídeo/áudio) ou páginas (para livro).
     * @param categoria A categoria ou gênero da mídia.
     */
    public Midia(String local, long tamanhoBytes, String titulo, long duracao, String categoria) {
        this.id = IdGerador.novoId();
        this.local = local;
        this.tamanhoBytes = tamanhoBytes;
        this.titulo = titulo;
        this.duracao = duracao;
        this.categoria = categoria;
    }


    /**
     *  Construtor para **carregar mídias existentes** a partir do disco/persistência.
     * <p>Utilizado pelo DAO, onde o ID já foi previamente gerado e persistido.</p>
     *
     * @param id O ID único da mídia carregada.
     * @param local O caminho completo do arquivo físico no sistema.
     * @param tamanhoBytes O tamanho do arquivo em bytes.
     * @param titulo O título da mídia.
     * @param duracao A duração da mídia (em minutos/páginas).
     * @param categoria A categoria ou gênero da mídia.
     */
    public Midia(int id, String local, long tamanhoBytes, String titulo, long duracao, String categoria) {
        this.id = id;
        this.local = local;
        this.tamanhoBytes = tamanhoBytes;
        this.titulo = titulo;
        this.duracao = duracao;
        this.categoria = categoria;
    }


    // --- GETTERS & SETTERS ---

    /**
     * Obtém o ID único da mídia.
     *
     * @return O ID da mídia (int).
     */
    public int getId() {
    	return id;
    }

    /**
     * Define o ID da mídia.
     * <p>Uso limitado, geralmente usado para inicializar IDs na carga do sistema (DAO).</p>
     *
     * @param id O novo ID.
     */
    public void setId(int id) {
    	this.id = id;
    }

    /**
     * Obtém o caminho físico do arquivo da mídia.
     *
     * @return O caminho do arquivo (String).
     */
    public String getLocal() {
    	return local;
    }

    /**
     * Define o caminho físico do arquivo da mídia.
     *
     * @param local O novo caminho do arquivo.
     */
    public void setLocal(String local) {
    	this.local = local;
    }

    /**
     * Obtém o tamanho do arquivo em bytes.
     *
     * @return O tamanho em bytes (long).
     */
    public long getTamanhoBytes() {
    	return tamanhoBytes;
    }

    /**
     * Define o tamanho do arquivo em bytes.
     *
     * @param tamanhoBytes O novo tamanho em bytes.
     */
    public void setTamanhoBytes(long tamanhoBytes) {
    	this.tamanhoBytes = tamanhoBytes;
    }

    /**
     * Obtém o título da mídia.
     *
     * @return O título (String).
     */
    public String getTitulo() {
    	return titulo;
    }

    /**
     *  Define o título da mídia.
     *
     * @param titulo O novo título.
     */
    public void setTitulo(String titulo) {
    	this.titulo = titulo;
    }

    /**
     *  Obtém a duração (em minutos para áudio/vídeo) ou contagem (em páginas para livro).
     *
     * @return O valor da duração/contagem (long).
     */
    public long getDuracao() {
    	return duracao;
    }

    /**
     *  Define a duração (em minutos/páginas).
     *
     * @param duracao O novo valor de duração/contagem.
     */
    public void setDuracao(long duracao) {
    	this.duracao = duracao;
    }

    /**
     *  Obtém a categoria ou gênero da mídia.
     *
     * @return A categoria (String).
     */
    public String getCategoria() {
    	return categoria;
    }

    /**
     *  Define a categoria ou gênero da mídia.
     *
     * @param categoria A nova categoria.
     */
    public void setCategoria(String categoria) {
    	this.categoria = categoria;
    }

    // --- MÉTODOS ABSTRATOS ---

    /**
     *  **Método Abstrato:** Deve ser implementado pelas subclasses para retornar o tipo específico da mídia.
     * <p>Exemplos de retorno: "FILME", "MUSICA", "LIVRO".</p>
     *
     * @return Uma {@code String} representando o tipo da mídia.
     */
    public abstract String getTipo();

    /**
     *  **Método Abstrato:** Deve ser implementado pelas subclasses para retornar uma string formatada
     * com os atributos que são exclusivos daquela subclasse.
     *
     * @return Uma {@code String} com os atributos específicos.
     */
    public abstract String exibirAtributosEspecificos();

    // --- MÉTODOS PADRÃO ---

    /**
     *  Sobrescreve o método {@code toString()} para fornecer uma representação
     * amigável da mídia.
     *
     * @return Uma string formatada como: {@code Titulo [TIPO] (Categoria)}.
     */
    @Override
    public String toString() {
        return String.format("%s [%s] (%s)", titulo, getTipo(), categoria);
    }
}