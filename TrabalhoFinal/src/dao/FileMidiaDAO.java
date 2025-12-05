package dao;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import model.*;

/**
 ***Implementação do Data Access Object (DAO) para Mídia, usando o Sistema de Arquivos.**
 *
 * <p>Esta classe implementa a interface {@link MidiaDAO}, persistindo cada objeto {@link Midia}
 * em um **arquivo de texto separado** dentro de um diretório de armazenamento especificado.</p>
 *
 * <p>O formato de cada arquivo é simples, utilizando a estrutura {@code chave=valor},
 * onde o nome do arquivo é o ID da mídia seguido pela extensão {@code .tpoo} (ex: {@code 123.tpoo}).</p>
 *
 * <p>Possui métodos internos para mapear objetos {@link Midia} para um {@code Map<String, String>} (para salvar)
 * e para recriar objetos {@link Midia} (incluindo subtipos como {@link Musica}, {@link Filme} e {@link Livro})
 * a partir do arquivo (para buscar).</p>
 *
 * @see MidiaDAO
 * @see Midia
 */
public class FileMidiaDAO implements MidiaDAO{
	private Path storageDir = null;

    /**
     * Construtor da classe.
     * <p>Garante que o diretório de armazenamento exista.</p>
     *
     * @param storageDir O caminho {@link Path} para o diretório onde os arquivos {@code .tpoo} serão armazenados.
     * @throws IOException Se ocorrer um erro ao criar o diretório de armazenamento, caso ele não exista.
     */
    public FileMidiaDAO(Path storageDir) throws IOException {
        this.storageDir = storageDir;
        if (!Files.exists(storageDir)) Files.createDirectories(storageDir);
    }

    /**
     * Salva (persiste) um objeto {@link Midia} em um arquivo.
     * <p>O arquivo é nomeado com o ID da mídia e a extensão {@code .tpoo}
     * (ex: {@code [id].tpoo}).</p>
     *
     * @param m O objeto {@link Midia} a ser salvo.
     * @throws Exception Se ocorrer um erro de I/O ao escrever no arquivo.
     */
    @Override
    public void salvar(Midia m) throws Exception {
        Path p = storageDir.resolve(m.getId() + ".tpoo");
        try (BufferedWriter w = Files.newBufferedWriter(p)) {
            Map<String,String> map = toMap(m);
            for (Map.Entry<String,String> e : map.entrySet()) {
                w.write(e.getKey() + "=" + e.getValue());
                w.newLine();
            }
        }
    }

    /**
     * Deleta o arquivo persistido da mídia correspondente ao ID.
     *
     * @param id O ID da mídia a ser deletada.
     * @return {@code true} se o arquivo existia e foi deletado; {@code false} caso contrário.
     * @throws Exception Se ocorrer um erro de I/O ao tentar deletar o arquivo.
     */
    @Override
    public boolean deletar(int id) throws Exception {
        Path p = storageDir.resolve(id + ".tpoo");
        if (Files.exists(p)) {
            Files.delete(p);
            return true;
        }
        return false;
    }

    /**
     * Atualiza um registro de mídia.
     * <p>Devido à natureza de persistência em arquivo único por objeto, esta operação
     * é equivalente a salvar (sobrescrever) o arquivo existente.</p>
     *
     * @param m O objeto {@link Midia} com os dados atualizados.
     * @throws Exception Se ocorrer um erro durante a operação de salvamento.
     */
    @Override
    public void atualizar(Midia m) throws Exception {
        salvar(m);
    }

    /**
     * Lista todos os objetos {@link Midia} salvos no diretório de armazenamento.
     * <p>Busca todos os arquivos com a extensão {@code .tpoo} no {@code storageDir}
     * e os converte em objetos {@link Midia}.</p>
     *
     * @return Uma lista {@code List<Midia>} contendo todas as mídias encontradas. Retorna uma lista vazia se o diretório não existir.
     * @throws Exception Se ocorrer um erro de I/O ao listar o diretório ou ler os arquivos.
     */
    @Override
    public List<Midia> listarTodos() throws Exception {
        if (!Files.exists(storageDir)) return Collections.emptyList();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(storageDir, "*.tpoo")) {
            List<Midia> list = new ArrayList<>();
            for (Path p : ds) {
            	Midia m = fromFile(p);
                if (m != null) list.add(m);
            }
            return list;
        }
    }

    /**
     * Busca uma mídia específica pelo seu ID.
     *
     * @param id O ID da mídia a ser buscada.
     * @return Um {@code Optional<Midia>} contendo a mídia se o arquivo {@code [id].tpoo} for encontrado,
     * ou {@code Optional.empty()} caso contrário.
     * @throws Exception Se ocorrer um erro de I/O ao ler o arquivo.
     */
    @Override
    public Optional<Midia> buscarPorId(int id) throws Exception {
        Path p = storageDir.resolve(id + ".tpoo");
        if (Files.exists(p)) {
            return Optional.ofNullable(fromFile(p));
        }
        return Optional.empty();
    }

    /**
     * Converte um objeto {@link Midia} (e seus subtipos) em um {@code Map<String, String>}
     * para facilitar a escrita no arquivo.
     * <p>Garante que todos os campos obrigatórios e específicos do subtipo sejam incluídos.</p>
     *
     * @param m O objeto {@link Midia} a ser mapeado.
     * @return Um {@code Map} com as chaves e valores dos atributos da mídia.
     */
    private Map<String,String> toMap(Midia m) {
        Map<String,String> map = new LinkedHashMap<>();
        map.put("id", String.valueOf(m.getId()));
        map.put("tipo", m.getTipo());
        if (m.getLocal() == null) {
            map.put("local", "");
        }else{
            map.put("local", m.getLocal());
        }
        map.put("tamanhoBytes", String.valueOf(m.getTamanhoBytes()));
        if (m.getTitulo() == null) {
            map.put("titulo", "");
        }else{
            map.put("titulo", m.getTitulo());
        }
        map.put("duracao", String.valueOf(m.getDuracao()));
        if (m.getCategoria() == null) {
            map.put("categoria", "");
        }else{
            map.put("categoria", m.getCategoria());
        }
        if (m instanceof Musica) {
            Musica mus = (Musica) m;
            if (mus.getArtista() == null) {
                map.put("artista", "");
            }else{
                map.put("artista", mus.getArtista());
            }
        }else if (m instanceof Filme){
            Filme f = (Filme) m;
            if (f.getIdiomaAudio() == null) {
                map.put("idiomaAudio", "");
            }else{
                map.put("idiomaAudio", f.getIdiomaAudio());
            }
        }else if (m instanceof Livro){
            Livro l = (Livro) m;
            if (l.getAutores() == null) {
                map.put("autores", "");
            }else{
                map.put("autores", l.getAutores());
            }
        }
        return map;
    }

    /**
     * Cria um objeto {@link Midia} (ou seu subtipo correto) a partir do conteúdo de um arquivo.
     * <p>Lê as linhas do arquivo, mapeia as chaves/valores e usa o campo "tipo"
     * para instanciar a classe concreta correta ({@code MUSICA}, {@code FILME} ou {@code LIVRO}).</p>
     *
     * @param p O caminho {@link Path} para o arquivo {@code .tpoo} a ser lido.
     * @return O objeto {@link Midia} instanciado, ou {@code null} se a leitura falhar ou o tipo for desconhecido.
     */
    private Midia fromFile(Path p) {
        try {
            List<String> lines = Files.readAllLines(p);
            Map<String,String> map = new HashMap<>();
            for (String l : lines) {
                int idx = l.indexOf('=');
                if (idx>0) {
                    String k = l.substring(0,idx);
                    String v = l.substring(idx+1);
                    map.put(k,v);
                }
            }
            String tipo = map.getOrDefault("tipo","");
            String local = map.getOrDefault("local","");
            int id = Integer.parseInt(map.getOrDefault("id","0")); // parse int
            long tamanho = Long.parseLong(map.getOrDefault("tamanhoBytes","0"));
            String titulo = map.getOrDefault("titulo","");
            long dur = Long.parseLong(map.getOrDefault("duracao","0"));
            String categoria = map.getOrDefault("categoria","");
            switch(tipo) {
                case "MUSICA":
                    //return new Musica(id, local, tamanho, titulo, dur, categoria, map.getOrDefault("artista",""));
                    return new Musica(id, local, tamanho, titulo, dur, categoria, map.getOrDefault("artista",""));
                case "FILME":
                    return new Filme(id, local, tamanho, titulo, dur, categoria, map.getOrDefault("idiomaAudio",""));
                case "LIVRO":
                    return new Livro(id, local, tamanho, titulo, dur, categoria, map.getOrDefault("autores",""));
                default:
                    return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}