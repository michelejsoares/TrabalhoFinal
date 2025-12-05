package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LivroTeste {
	@Test
    void testGetAutoresList() {
        Livro l = new Livro("c:/l.pdf", 100, "Livro", 10, "Drama", "Autor1, Autor2");
        assertEquals(2, l.getAutoresList().size());
    }

    @Test
    void testAutoresVazio() {
        Livro l = new Livro("c:/x.pdf", 50, "X", 5, "Cat", "");
        assertEquals(0, l.getAutoresList().size());
    }

}
