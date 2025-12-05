package controle;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 *Gerador de IDs Sequenciais Persistentes
 *
 * Esta classe é responsável por **gerar e gerenciar IDs sequenciais** que são
 * **persistidos** em um arquivo de texto chamado {@code idContador.txt}.
 *
 * O contador é carregado automaticamente na inicialização da classe (bloco estático).
 * É **thread-safe**, garantindo que as operações de obtenção e incremento de ID
 * sejam atômicas.
 *
 * <br>
 *
 * **Comportamento na Inicialização:**
 * <ul>
 * <li>Se {@code idContador.txt} **não existir**, ele é criado com o valor "0".</li>
 * <li>Se **existir**, o valor é lido e usado para inicializar o contador.</li>
 * <li>Se a leitura falhar (ex.: arquivo vazio, formato inválido), o contador
 * é resetado para **0** e o arquivo é sobrescrito com "0".</li>
 * </ul>
 */
public class IdGerador {
    private static final Path PATH = Path.of("idContador.txt");
    private static int contador = 0;

    static {
        try {
            if (!Files.exists(PATH)) {
                // Se o arquivo não existe, cria e inicializa com 0.
                Files.writeString(PATH, "0");
                contador = 0;
            } else {
                // Se o arquivo existe, tenta ler o valor atual.
                String s = Files.readString(PATH).trim();
                if (s.isEmpty()) {
                    // Arquivo vazio, reseta para 0.
                    contador = 0;
                    Files.writeString(PATH, "0", StandardOpenOption.TRUNCATE_EXISTING);
                } else {
                    try {
                        // Converte o valor lido para int.
                        contador = Integer.parseInt(s);
                    } catch (NumberFormatException ex) {
                        // Falha na conversão, reseta para 0 e corrige o arquivo.
                        contador = 0;
                        Files.writeString(PATH, "0", StandardOpenOption.TRUNCATE_EXISTING);
                    }
                }
            }
        } catch (IOException e) {
            // Em caso de qualquer erro de I/O na inicialização, imprime o stack trace e usa 0.
            e.printStackTrace();
            contador = 0;
        }
    }

    /**
     ***Inicializa o contador com o maior ID existente (apenas incremento).**
     *
     * Este método deve ser usado na inicialização da aplicação para garantir que o
     * {@code contador} interno seja **maior ou igual** ao ID máximo atualmente
     * em uso em qualquer coleção de dados persistente.
     *
     * **Nota:** O contador **não é decrementado**; se {@code lastId} for menor
     * que o valor atual do contador, nada acontece.
     *
     * @param lastId O maior ID (máximo) encontrado no armazenamento de dados.
     * Deve ser maior que 0 para ter efeito.
     * @see #novoId()
     */
    public static synchronized void iniciar(int lastId) {
        if (lastId <= 0) return; // nada a fazer se o ID máximo for 0 ou negativo.

        if (lastId > contador) {
            contador = lastId;
            try {
                // Persiste o novo valor maior no arquivo.
                Files.writeString(PATH, String.valueOf(contador), StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     ***Retorna o próximo ID sequencial.**
     *
     * **Incrementa** o contador interno em 1 e **persiste** o novo valor no arquivo
     * {@code idContador.txt} antes de retorná-lo.
     *
     * A operação é **sincronizada** para garantir que cada chamada retorne um ID único.
     *
     * @return O próximo ID sequencial ({@code int > 0}), ou **-1** em caso de erro de persistência.
     */
    public static synchronized int novoId() {
        try {
            contador = contador + 1;
            // Persiste o novo ID.
            Files.writeString(PATH, String.valueOf(contador), StandardOpenOption.TRUNCATE_EXISTING);
            return contador;
        } catch (IOException e) {
            // Em caso de erro de I/O na persistência, imprime o stack trace e retorna -1.
            e.printStackTrace();
            return -1;
        }
    }

    /**
     ***Retorna o valor atual do contador.**
     *
     * Retorna o último ID gerado ou carregado. Esta chamada **não incrementa** o contador.
     *
     * @return O valor atual do contador (último ID gerado/carregado).
     */
    public static synchronized int getAtual() {
        return contador;
    }
}