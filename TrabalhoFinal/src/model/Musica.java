package model;

/**
 * **Representa uma Mídia do tipo Música.**
 *
 * <p>Esta classe estende a classe abstrata {@link Midia} e adiciona o
 * atributo específico {@code artista}.</p>
 *
 * <p>O campo {@code duracao} (da classe pai) é utilizado para armazenar
 * a duração da música, tipicamente em segundos.</p>
 *
 * @see Midia
 */
public class Musica extends Midia{
	private String artista;

	/**
     *  Construtor completo para criar um novo objeto Música (sem ID, que será gerado pelo {@link controle.IdGerador}).
     *
     * @param local O caminho físico onde o arquivo de áudio está armazenado.
     * @param tamanhoBytes O tamanho do arquivo em bytes.
     * @param titulo O título da música.
     * @param duracao A duração da música, geralmente em segundos.
     * @param categoria A categoria ou gênero musical (ex: Rock, Pop, Clássica).
     * @param artista O nome do artista ou banda.
     */
	public Musica(String local, long tamanhoBytes, String titulo, long duracao, String categoria, String artista) {
        super(local, tamanhoBytes, titulo, duracao, categoria); // gera novo id
        this.artista = artista;
    }

    /**
     *  Construtor completo para recriar um objeto Música a partir do armazenamento persistente (com ID já definido).
     *
     * @param id O ID único da música.
     * @param local O caminho físico onde o arquivo de áudio está armazenado.
     * @param tamanhoBytes O tamanho do arquivo em bytes.
     * @param titulo O título da música.
     * @param duracao A duração da música, geralmente em segundos.
     * @param categoria A categoria ou gênero musical.
     * @param artista O nome do artista ou banda.
     */
    public Musica(int id, String local, long tamanhoBytes, String titulo, long duracao, String categoria, String artista) {
        super(id, local, tamanhoBytes, titulo, duracao, categoria); // usa id passado
        this.artista = artista;
    }

    /**
     *  Obtém o nome do artista ou banda.
     *
     * @return O artista (String).
     */
    public String getArtista() { return artista; }

    /**
     *  Define o nome do artista ou banda.
     *
     * @param artista O novo nome do artista.
     */
    public void setArtista(String artista) {
    	this.artista = artista;
    }

    /**
     *  Retorna o tipo específico desta mídia.
     *
     * @return A string literal "MUSICA".
     */
    @Override
    public String getTipo() {
    	return "MUSICA";
    }

    /**
     *  Retorna uma string formatada com os atributos específicos da classe {@code Musica}.
     *
     * <p>Exibe o artista e a duração da música em segundos.</p>
     *
     * @return Uma string contendo o artista e a duração.
     */
    @Override
    public String exibirAtributosEspecificos() {
        return "Artista: " + artista + " | Duração(s): " + getDuracao();
    }
}