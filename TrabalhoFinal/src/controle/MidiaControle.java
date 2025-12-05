package controle;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JOptionPane;

import dao.MidiaDAO;
import model.Midia;

/**
 ***Controle da Entidade Mídia**
 *
 * <p>Esta classe atua como a **camada de controle (Business Logic)** para a entidade {@code Midia}.</p>
 *
 * <p>É responsável por coordenar as operações de CRUD (Criação, Leitura, Atualização, Deleção)
 * através do {@link MidiaDAO} e gerenciar a manipulação dos **arquivos físicos**
 * associados a cada mídia no sistema de arquivos (mover, renomear e deletar).</p>
 *
 * @see Midia
 * @see MidiaDAO
 */
public class MidiaControle {
	private final MidiaDAO dao;

    /**
     * 🛠️ Construtor que injeta a dependência do DAO (Data Access Object).
     *
     * @param dao A instância do {@code MidiaDAO} a ser utilizada para persistência.
     */
    public MidiaControle(MidiaDAO dao) {
        this.dao = dao;
    }

    // --- MÉTODOS DE MANIPULAÇÃO DE DADOS (CRUD) ---

    /**
     * Inclui um novo registro de mídia no armazenamento persistente.
     *
     * @param m O objeto {@link Midia} a ser incluído.
     * @return A mídia incluída (geralmente com o ID gerado pelo DAO).
     * @throws Exception Se ocorrer um erro na camada de persistência.
     */
    public Midia incluirMedia(Midia m) throws Exception {
        dao.salvar(m);
        return m;
    }

    /**
     *Edita um registro de mídia existente no armazenamento persistente.
     *
     * @param m O objeto {@link Midia} com os dados atualizados.
     * @throws Exception Se ocorrer um erro na camada de persistência.
     */
    public void editarMidia(Midia m) throws Exception {
    	System.out.println("LOCAL ORIGINAL = " + m.getLocal());
        dao.atualizar(m);
    }

    /**
     *Atualiza um registro de mídia existente no armazenamento persistente.
     * <p><i>Nota: Funcionalidade idêntica a {@link #editarMidia(Midia)}.</i></p>
     *
     * @param m O objeto {@link Midia} com os dados a serem atualizados.
     * @throws Exception Se ocorrer um erro na camada de persistência.
     */
    public void atualizarMidia(Midia m) throws Exception {
        dao.atualizar(m);
    }

    /**
     *Remove a mídia do armazenamento persistente e, se existir, deleta o arquivo físico associado.
     * <p>Executa uma exclusão dupla: <b>registro lógico</b> (DAO) e <b>arquivo físico</b>.</p>
     *
     * @param i O ID (inteiro) da mídia a ser removida.
     * @return {@code true} se o registro foi deletado do DAO; {@code false} se a mídia não foi encontrada.
     * @throws Exception Se ocorrer um erro de I/O ao tentar deletar o arquivo ou um erro na camada DAO.
     */
    public boolean removerMidia(int i) throws Exception {
        Optional<Midia> opt = dao.buscarPorId(i);
        if (opt.isPresent()) {
            Midia m = opt.get();

            // NORMALIZAR O CAMINHO (trocar \ por /)
            String caminho = m.getLocal().replace("\\", "/");
            Path arquivo = Paths.get(caminho).normalize();

            // apagar o arquivo físico, se existir
            if (Files.exists(arquivo)) {
                Files.delete(arquivo);
            }
            // remover o .tpoo
            return dao.deletar(i);
        }
        return false;
    }

    /**
     *Move o arquivo físico associado a uma mídia para um novo diretório e atualiza o registro no DAO.
     *
     * @param id O ID da mídia cujo arquivo será movido.
     * @param destinoDir O diretório de destino {@link Path} para onde o arquivo será movido.
     * @throws IllegalArgumentException Se a mídia com o ID fornecido não for encontrada.
     * @throws Exception Se ocorrer um erro de I/O durante a movimentação do arquivo ou um erro no DAO.
     */
    public void moverMedia(int id, Path destinoDir) throws Exception {
        Optional<Midia> opt = dao.buscarPorId(id);
        if (opt.isPresent()) {
            Midia m = opt.get();
            Path origem = Paths.get(m.getLocal());

            // cria a pasta de destino
            if (!Files.exists(destinoDir)) Files.createDirectories(destinoDir);

            Path alvo = destinoDir.resolve(origem.getFileName());

            // mover fisicamente, sobrescrevendo se o alvo já existir
            Files.move(origem, alvo, StandardCopyOption.REPLACE_EXISTING);

            // atualizar o caminho no objeto e persistir
            m.setLocal(alvo.toString());
            dao.atualizar(m);

        } else {
            throw new IllegalArgumentException("Arquivo não encontrado: " + id);
        }
    }

    /**
     * 🏷️ Renomeia o arquivo físico associado a uma mídia, preservando a extensão original, e atualiza o registro no DAO.
     *
     * <p>A extensão do arquivo original é mantida, ignorando qualquer extensão incluída no {@code novoNome}
     * fornecido pelo usuário.</p>
     *
     * @param id O ID da mídia cujo arquivo será renomeado.
     * @param novoNome O novo nome base desejado para o arquivo (sem ou com extensão).
     * @throws IllegalArgumentException Se a mídia não for encontrada pelo ID.
     * @throws IOException Se o arquivo físico não existir, se o novo nome for igual ao atual, ou se o arquivo de destino já existir.
     * @throws Exception Se ocorrer um erro na camada DAO.
     */
    public void renomearArquivo(int id, String novoNome) throws Exception {
        Optional<Midia> opt = dao.buscarPorId(id);
        if (!opt.isPresent()) {
            throw new IllegalArgumentException("Arquivo não encontrado: " + id);
        }

        Midia m = opt.get();
        Path origem = Paths.get(m.getLocal());

        // Verifica se arquivo existe
        if (!Files.exists(origem)) {
            throw new IOException("Arquivo físico não existe: " + origem);
        }

        // Extrai a extensão original
        String nomeOriginal = origem.getFileName().toString();
        int idx = nomeOriginal.lastIndexOf(".");
        String extensao = (idx >= 0) ? nomeOriginal.substring(idx) : "";

        // Remove a extensão que pode ter sido digitada pelo usuário
        int idx2 = novoNome.lastIndexOf(".");
        if (idx2 >= 0) {
            novoNome = novoNome.substring(0, idx2);
        }

        String novoNomeCompleto = novoNome + extensao;

        Path destino = origem.resolveSibling(novoNomeCompleto);

        // Validações
        if (origem.equals(destino)) {
            throw new IOException("O novo nome é igual ao nome atual.");
        }
        if (Files.exists(destino)) {
            throw new IOException("Já existe um arquivo com esse nome: " + destino);
        }

        // Renomear (mover)
        Files.move(origem, destino);

        // Atualizar no objeto e no DAO
        m.setLocal(destino.toString());
        dao.atualizar(m);
    }

    // --- MÉTODOS DE CONSULTA E FILTRAGEM ---

    /**
     * Lista todos os registros de mídia do armazenamento persistente.
     *
     * @return Uma {@code List} contendo todos os objetos {@link Midia}.
     * @throws Exception Se ocorrer um erro na camada DAO.
     */
    public List<Midia> listarTodos() throws Exception {
        return dao.listarTodos();
    }

    /**
     * Lista as mídias aplicando filtros opcionais (Tipo e Categoria) e ordenação.
     *
     * <ul>
     * <li><b>Filtro por Tipo:</b> Se {@code tipoOpt} estiver presente, filtra por {@code Midia.tipo} (case-insensitive).</li>
     * <li><b>Filtro por Categoria:</b> Se {@code categoriaOpt} estiver presente, filtra por {@code Midia.categoria} (case-insensitive e ignora {@code null}).</li>
     * <li><b>Ordenação:</b>
     * <ul>
     * <li>"ALFABETICA": Ordena por {@code Midia.titulo} (case-insensitive).</li>
     * <li>"DURACAO": Ordena por {@code Midia.duracao}.</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @param tipoOpt {@code Optional<String>} com o tipo de mídia para filtrar.
     * @param categoriaOpt {@code Optional<String>} com a categoria de mídia para filtrar.
     * @param ordenar {@code Optional<String>} com o critério de ordenação ("ALFABETICA" ou "DURACAO").
     * @return Uma {@code List} de {@link Midia} filtrada e/ou ordenada.
     * @throws Exception Se ocorrer um erro ao buscar todos os registros no DAO.
     */
    public List<Midia> listarFiltrados(Optional<String> tipoOpt, Optional<String> categoriaOpt, Optional<String> ordenar) throws Exception {
        List<Midia> all = listarTodos();
        Stream<Midia> s = all.stream();

        // Aplica Filtro por Tipo
        if (tipoOpt.isPresent()) {
            String t = tipoOpt.get();
            s = s.filter(m -> m.getTipo().equalsIgnoreCase(t));
        }

        // Aplica Filtro por Categoria
        if (categoriaOpt.isPresent()) {
            String c = categoriaOpt.get();
            s = s.filter(m -> m.getCategoria()!=null && m.getCategoria().equalsIgnoreCase(c));
        }

        List<Midia> res = s.collect(Collectors.toList());

        // Aplica Ordenação
        if (ordenar.isPresent()) {
            String o = ordenar.get();
            if ("ALFABETICA".equalsIgnoreCase(o)) {
                // Ordena por título, colocando nulos por último
                res.sort(Comparator.comparing(Midia::getTitulo, Comparator.nullsLast(String::compareToIgnoreCase)));
            } else if ("DURACAO".equalsIgnoreCase(o)) {
                // Ordena por duração
                res.sort(Comparator.comparingLong(Midia::getDuracao));
            }
        }
        return res;
    }
}