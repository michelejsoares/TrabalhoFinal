package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FilmeTeste {

	@Test
    void testConstrutor() {
        Filme f = new Filme("local", 1000, "Titulo", 120, "Ação", "PT-BR");
        assertEquals("PT-BR", f.getIdiomaAudio());
        assertEquals("FILME", f.getTipo());
    }

    @Test
    void testConstrutorComId() {
        Filme f = new Filme(50, "l", 200, "T", 10, "Drama", "EN");
        assertEquals(50, f.getId());
    }

    @Test
    void testExibirAtributos() {
        Filme f = new Filme("l", 200, "Filme", 90, "Ação", "EN");
        assertEquals("Idioma áudio: EN | Duração(min): 90", f.exibirAtributosEspecificos());
    }


}
