package view;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.*;

public class MidiaDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JComboBox<String> tipoCombo;
    private JTextField tituloField;
    private JTextField localField;
    private JTextField durField;
    private JTextField categoriaField;
    private JTextField extraField;

    private JButton btnChoose;
    private boolean confirmed = false;

    private boolean durCalc = false;
    private long duracaoTemp = 0;

    public MidiaDialog(Frame owner) {
        super(owner, "Incluir/Editar Midia", true);
        init();
    }

    private void init() {

        setLayout(new BorderLayout());
        JPanel p = new JPanel(new GridLayout(0, 2, 5, 5));

        tipoCombo = new JComboBox<>(new String[]{"MUSICA", "FILME", "LIVRO"});
        tituloField = new JTextField();
        localField = new JTextField();
        durField = new JTextField();
        categoriaField = new JTextField();
        extraField = new JTextField();

        btnChoose = new JButton("Escolher arquivo...");
        btnChoose.addActionListener(e -> escolherArquivo());

        p.add(new JLabel("Tipo")); p.add(tipoCombo);
        p.add(new JLabel("Título")); p.add(tituloField);
        p.add(new JLabel("Local (caminho)")); p.add(localField);
        p.add(new JLabel("Arquivo (escolher)")); p.add(btnChoose);
        p.add(new JLabel("Duração (min/s/pg)")); p.add(durField);
        p.add(new JLabel("Categoria")); p.add(categoriaField);
        p.add(new JLabel("Artista/Idioma/Autores")); p.add(extraField);

        add(p, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        JButton ok = new JButton("OK");
        ok.addActionListener(e -> { confirmed = true; setVisible(false); });

        JButton cancel = new JButton("Cancelar");
        cancel.addActionListener(e -> { confirmed = false; setVisible(false); });

        bottom.add(ok);
        bottom.add(cancel);

        add(bottom, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
    }

    private void escolherArquivo() {

        JFileChooser fc = new JFileChooser();
        int r = fc.showOpenDialog(this);

        if (r == JFileChooser.APPROVE_OPTION) {

            File f = fc.getSelectedFile();
            String path = f.getAbsolutePath();
            localField.setText(path);

            String ext = getExtensao(path);
            String tipo = (String) tipoCombo.getSelectedItem();

            // validação pelas extensões
            if ("MUSICA".equalsIgnoreCase(tipo)) {
                if (!ext.equals("mp3")) {
                    JOptionPane.showMessageDialog(this, "Músicas só podem ser MP3.");
                    return;
                }
            }
            else if ("FILME".equalsIgnoreCase(tipo)) {
                if (!ext.equals("mp4") && !ext.equals("mkv")) {
                    JOptionPane.showMessageDialog(this, "Filmes só podem ser MP4 ou MKV.");
                    return;
                }
            }
            else if ("LIVRO".equalsIgnoreCase(tipo)) {
                if (!ext.equals("pdf") && !ext.equals("epub")) {
                    JOptionPane.showMessageDialog(this, "Livros só podem ser PDF ou EPUB.");
                    return;
                }
            }

        }
    }

    private String getExtensao(String caminho) {
        int p = caminho.lastIndexOf('.');
        if (p >= 0) {
            return caminho.substring(p + 1).toLowerCase();
        }
        return "";
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Midia buildMediaFromForm() {

        String tipo = (String) tipoCombo.getSelectedItem();
        String titulo = tituloField.getText().trim();
        String local = localField.getText().trim();
        String categoria = categoriaField.getText().trim();
        String extra = extraField.getText().trim();

        // Se não foi calculado AO ESCOLHER O ARQUIVO, tentar pegar agora
        if (!durCalc) {
            try {
                duracaoTemp = Long.parseLong(durField.getText().trim());
            } catch (Exception ex) {
                duracaoTemp = 0;
            }
        }

        File f = null;
        long tamanhoBytes = 0;

        if (local != null && !local.isEmpty()) {
            f = new File(local);
            tamanhoBytes = f.length();
        }

        if ("MUSICA".equalsIgnoreCase(tipo)) {

            return new Musica(local, tamanhoBytes, titulo, duracaoTemp, categoria, extra);

        } else if ("FILME".equalsIgnoreCase(tipo)) {

            return new Filme(local, tamanhoBytes, titulo, duracaoTemp, categoria, extra);

        } else {

            return new Livro(local, tamanhoBytes, titulo, duracaoTemp, categoria, extra);
        }
    }

    public void fillFromMedia(Midia m) {

        tipoCombo.setSelectedItem(m.getTipo());
        tituloField.setText(m.getTitulo());
        localField.setText(m.getLocal());
        durField.setText(String.valueOf(m.getDuracao()));
        categoriaField.setText(m.getCategoria());

        extraField.setText(m.exibirAtributosEspecificos());
    }
}
