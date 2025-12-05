package model;

/**
 * **Representa uma Mídia do tipo Filme.**
 *
 * <p>Esta classe estende a classe abstrata {@link Midia} e adiciona o
 * atributo específico {@code idiomaAudio}.</p>
 *
 * <p>O método {@code getTipo()} é sobrescrito para retornar a constante "FILME".</p>
 *
 * @see Midia
 */
public class Filme extends Midia{
	private String idiomaAudio;

    /**
     *  Construtor completo para criar um novo objeto Filme (sem ID, que será gerado pelo DAO).
     *
     * @param local O caminho físico onde o arquivo de mídia está armazenado.
     * @param tamanhoBytes O tamanho do arquivo em bytes.
     * @param titulo O título do filme.
     * @param duracaoMinutes A duração do filme em minutos.
     * @param categoria A categoria ou gênero do filme (ex: Ação, Drama).
     * @param idiomaAudio O idioma de áudio original ou principal do filme (ex: Inglês, Português).
     */
    public Filme(String local, long tamanhoBytes, String titulo, long duracaoMinutes, String categoria, String idiomaAudio) {
        super(local, tamanhoBytes, titulo, duracaoMinutes, categoria);
        this.idiomaAudio = idiomaAudio;
    }

    /**
     *  Construtor completo para recriar um objeto Filme a partir do armazenamento persistente (com ID já definido).
     *
     * @param id O ID único do filme.
     * @param local O caminho físico onde o arquivo de mídia está armazenado.
     * @param tamanhoBytes O tamanho do arquivo em bytes.
     * @param titulo O título do filme.
     * @param duracaoMinutes A duração do filme em minutos.
     * @param categoria A categoria ou gênero do filme.
     * @param idiomaAudio O idioma de áudio original ou principal do filme.
     */
    public Filme(int id, String local, long tamanhoBytes, String titulo, long duracaoMinutes, String categoria, String idiomaAudio) {
        super(id, local, tamanhoBytes, titulo, duracaoMinutes, categoria);
        this.idiomaAudio = idiomaAudio;
    }

    /**
     *  Obtém o idioma de áudio do filme.
     *
     * @return O idioma de áudio (String).
     */
    public String getIdiomaAudio() { return idiomaAudio; }

    /**
     *  Define o idioma de áudio do filme.
     *
     * @param idiomaAudio O novo idioma de áudio.
     */
    public void setIdiomaAudio(String idiomaAudio) {
    	this.idiomaAudio = idiomaAudio;
    }

    /**
     *  Retorna o tipo específico desta mídia.
     *
     * @return A string literal "FILME".
     */
    @Override
    public String getTipo() {
    	return "FILME";
    }

    /**
     * Retorna uma string formatada com os atributos específicos da classe {@code Filme}.
     *
     * @return Uma string contendo o idioma de áudio e a duração.
     */
    @Override
    public String exibirAtributosEspecificos() {
        return "Idioma áudio: " + idiomaAudio + " | Duração(min): " + getDuracao();
    }
}