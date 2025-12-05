package dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.nio.file.*;
import java.util.*;

import org.junit.jupiter.api.*;
import model.*;

class FileMidiaDAOTeste {
	private Path dir;
    private FileMidiaDAO dao;

    @BeforeEach
    void setup() throws Exception {
        dir = Files.createTempDirectory("dao");
        dao = new FileMidiaDAO(dir);
    }

    @Test
    void testSalvarEMontarFilme() throws Exception {
        Filme f = new Filme("c:/f.mp4", 1234, "Ação", 140, "Ação", "PT");
        dao.salvar(f);

        Optional<Midia> carregada = dao.buscarPorId(f.getId());
        assertTrue(carregada.isPresent());
        assertEquals("FILME", carregada.get().getTipo());
    }

    @Test
    void testDeletar() throws Exception {
        Musica m = new Musica("c:/x.mp3", 999, "X", 111, "Pop", "Autor");
        dao.salvar(m);

        assertTrue(dao.deletar(m.getId()));
        assertFalse(dao.buscarPorId(m.getId()).isPresent());
    }

    @Test
    void testListarTodos() throws Exception {
        dao.salvar(new Livro("c:/a.pdf", 100, "A", 10, "Drama", "Autor"));
        dao.salvar(new Filme("c:/b.mp4", 1000, "B", 100, "Ação", "EN"));

        List<Midia> list = dao.listarTodos();
        assertEquals(2, list.size());
    }


}
