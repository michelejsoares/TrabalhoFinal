package controle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.nio.file.*;
import java.util.*;

import org.junit.jupiter.api.*;
import dao.FileMidiaDAO;
import model.*;

class MidiaControleTeste {

	private Path tempDir;
    private FileMidiaDAO dao;
    private MidiaControle controle;

    @BeforeEach
    void setup() throws Exception {
        tempDir = Files.createTempDirectory("midias");
        dao = new FileMidiaDAO(tempDir);
        controle = new MidiaControle(dao);
    }

    @Test
    void testIncluirMedia() throws Exception {
        Musica m = new Musica("c:/teste.mp3", 1000, "Teste", 200, "Pop", "Artista");
        controle.incluirMedia(m);

        Optional<Midia> buscada = dao.buscarPorId(m.getId());
        assertTrue(buscada.isPresent());
    }

    @Test
    void testEditarMedia() throws Exception {
        Filme f = new Filme("c:/filme.mp4", 2000, "Filme", 120, "Ação", "PT-BR");
        controle.incluirMedia(f);

        f.setIdiomaAudio("EN");
        controle.editarMidia(f);

        Filme atualizado = (Filme) dao.buscarPorId(f.getId()).get();
        assertEquals("EN", atualizado.getIdiomaAudio());
    }

    @Test
    void testRemoverMidia() throws Exception {
        Livro l = new Livro("c:/livro.pdf", 500, "Livro", 10, "Drama", "Autor");
        controle.incluirMedia(l);

        boolean ok = controle.removerMidia(l.getId());
        assertTrue(ok);
        assertFalse(dao.buscarPorId(l.getId()).isPresent());
    }

    @Test
    void testMoverMedia() throws Exception {
        // 1 — cria arquivo real para mover
        Path arquivoOriginal = tempDir.resolve("original.mp3");
        Files.write(arquivoOriginal, "conteudo qualquer".getBytes());
        // 2 — cria objeto Midia apontando para o arquivo REAL
        Musica m = new Musica(arquivoOriginal.toString(), Files.size(arquivoOriginal), "Musica", 100, "Rock", "Artista");
        controle.incluirMedia(m);
        Path novaPasta = Files.createTempDirectory("destino");
        controle.moverMedia(m.getId(), novaPasta);
        Midia movida = dao.buscarPorId(m.getId()).get();
        assertTrue(movida.getLocal().startsWith(novaPasta.toString()));
        assertTrue(Files.exists(Paths.get(movida.getLocal())));
    }


    @Test
    void testRenomearArquivo() throws Exception {
        Path file = tempDir.resolve("som.mp3");
        Files.write(file, "abc".getBytes()); // cria arquivo real

        Musica m = new Musica(file.toString(), 1000, "Som", 100, "Rock", "Artista");
        controle.incluirMedia(m);

        controle.renomearArquivo(m.getId(), "NovoNome");

        Midia renomeada = dao.buscarPorId(m.getId()).get();
        assertTrue(renomeada.getLocal().contains("NovoNome.mp3"));
    }



}
