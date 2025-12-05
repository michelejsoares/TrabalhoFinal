package dao;

import java.util.List;
import java.util.Optional;
import model.Midia;

/**
 * **Interface de Acesso a Dados (DAO) para a Entidade Mídia.**
 *
 * <p>Define o **contrato** para todas as implementações de persistência
 * da entidade {@link Midia}. Esta interface especifica as operações
 * básicas de CRUD (Create, Read, Update, Delete) necessárias para
 * gerenciar objetos {@code Midia} em qualquer tipo de armazenamento
 * (e.g., arquivos, banco de dados, memória).</p>
 *
 * <p>Todas as implementações devem fornecer uma maneira de salvar, deletar,
 * atualizar e buscar mídias.</p>
 *
 * @see Midia
 * @see FileMidiaDAO
 */
public interface MidiaDAO {

    /**
     * Salva um novo objeto {@link Midia} no armazenamento persistente.
     * <p>Se a mídia já possuir um ID, a implementação pode tratá-lo como uma atualização
     * ou gerar um novo, dependendo da lógica do DAO.</p>
     *
     * @param m O objeto {@link Midia} a ser persistido.
     * @throws Exception Se ocorrer um erro durante a operação de salvamento.
     */
	void salvar(Midia m) throws Exception;

    /**
     *Deleta o registro de uma mídia no armazenamento persistente usando seu ID.
     *
     * @param id O ID da mídia a ser removida.
     * @return {@code true} se o registro foi encontrado e deletado; {@code false} caso contrário.
     * @throws Exception Se ocorrer um erro durante a operação de deleção.
     */
    boolean deletar(int id) throws Exception;

    /**
     * Atualiza um registro de mídia existente no armazenamento persistente.
     * <p>Este método é tipicamente usado para sobrescrever os dados de uma mídia
     * que já foi salva anteriormente (baseando-se no ID).</p>
     *
     * @param m O objeto {@link Midia} com os dados atualizados.
     * @throws Exception Se ocorrer um erro durante a operação de atualização.
     */
    void atualizar(Midia m) throws Exception;

    /**
     *  Lista todas as mídias presentes no armazenamento persistente.
     *
     * @return Uma {@code List} contendo todos os objetos {@link Midia} persistidos.
     * @throws Exception Se ocorrer um erro durante a operação de leitura.
     */
    List<Midia> listarTodos() throws Exception;

    /**
     *  Busca uma mídia específica pelo seu ID.
     *
     * @param id O ID da mídia a ser buscada.
     * @return Um {@code Optional<Midia>} contendo a mídia se ela for encontrada;
     * {@code Optional.empty()} caso o ID não corresponda a nenhum registro.
     * @throws Exception Se ocorrer um erro durante a operação de busca.
     */
    Optional<Midia> buscarPorId(int id) throws Exception;
}