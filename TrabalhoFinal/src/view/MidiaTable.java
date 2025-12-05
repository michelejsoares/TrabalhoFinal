package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.Midia;

public class MidiaTable extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    private final String[] cols = {"ID","Título","Tipo","Categoria","Tamanho","Duração","Local","Específico"};
    private List<Midia> dados = new ArrayList<>();

    public void setMedias(List<Midia> medias) {
        this.dados = medias == null ? new ArrayList<>() : medias;
        fireTableDataChanged();
    }

    public Midia getMediaAt(int row) {
        if (row < 0 || row >= dados.size()) return null;
        return dados.get(row);
    }

    @Override
    public int getRowCount() { return dados.size(); }

    @Override
    public int getColumnCount() { return cols.length; }

    @Override
    public String getColumnName(int col) { return cols[col]; }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Midia m = dados.get(rowIndex);
        switch(columnIndex) {
            case 0: return m.getId();
            case 1: return m.getTitulo();
            case 2: return m.getTipo();
            case 3: return m.getCategoria();
            case 4: return formatTamanho(m.getTamanhoBytes());
            case 5: return formatDuracao(m);
            case 6: return m.getLocal();
            case 7: return m.exibirAtributosEspecificos();
            default: return "";
        }
    }

    // formata bytes -> "40 KB" (arredonda)
    private String formatTamanho(long bytes) {
        if (bytes <= 0) return "0 KB";
        long kb = Math.round(bytes / 1024.0);
        return kb + " KB";
    }

    // formata duração conforme tipo e flag duracaoCalculada
    private String formatDuracao(Midia m) {
        long d = m.getDuracao();
        String tipo = m.getTipo() == null ? "" : m.getTipo().toUpperCase();

        // se a duração foi calculada (provavelmente a partir do tamanho), mostramos "NN KB"
        try {
            boolean calc = false;
            // tentativa de checar método isDuracaoCalculada (caso exista)
            calc = (boolean) Midia.class.getMethod("isDuracaoCalculada").invoke(m);
            if (calc) {
                // mostrar como KB (já que foi calculada a partir do tamanho)
                return d + " KB";
            }
        } catch (NoSuchMethodException ns) {
            // método não existe — seguir regras abaixo
        } catch (Exception e) {
            // falha ao invocar — ignorar e prosseguir
        }

        // se não foi calculada, formatar por tipo
        switch (tipo) {
            case "MUSICA":
                return d + " s";      // segundos
            case "FILME":
                return d + " min";    // minutos
            case "LIVRO":
                return d + " pg";     // páginas
            default:
                return String.valueOf(d);
        }
    }
}
