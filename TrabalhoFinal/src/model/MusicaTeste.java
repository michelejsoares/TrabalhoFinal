package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MusicaTeste {

	 @Test
    void testConstrutor() {
        Musica m = new Musica("c:/m.mp3", 120, "Song", 180, "Rock", "Band");
        assertEquals("Band", m.getArtista());
        assertEquals("MUSICA", m.getTipo());
    }

    @Test
    void testAtributosEspecificos() {
        Musica m = new Musica("l", 200, "X", 100, "Pop", "A");
        assertEquals("Artista: A | Duração(s): 100", m.exibirAtributosEspecificos());
    }

}
