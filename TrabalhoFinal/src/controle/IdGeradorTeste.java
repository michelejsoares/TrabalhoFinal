package controle;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.nio.file.*;
import org.junit.jupiter.api.*;

class IdGeradorTeste {
	@Test
    void testGeracaoSequencial() {
        int a = IdGerador.novoId();
        int b = IdGerador.novoId();
        assertEquals(a + 1, b);
    }

    @Test
    void testIniciar() {
        IdGerador.iniciar(500);
        assertTrue(IdGerador.getAtual() >= 500);
    }


}
