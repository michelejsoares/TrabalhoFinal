package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MidiaTeste {

	@Test
    void testConstrutorComId() {
        Musica m = new Musica(150, "l", 100, "T", 10, "A", "Art");
        assertEquals(150, m.getId());
    }

    @Test
    void testToString() {
        Musica m = new Musica("l", 100, "Musica", 10, "Pop", "Art");
        assertNotNull(m.toString());
        assertTrue(m.toString().contains("Musica"));
    }

}
