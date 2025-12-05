package view;

import java.awt.BorderLayout;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import controle.MidiaControle;
import dao.FileMidiaDAO;
import model.Midia;
import controle.IdGerador;

public class Main extends JFrame {
    private static final long serialVersionUID = 1L;
    private final MidiaControle controle;
    private final MidiaTable tableModel = new MidiaTable();
    private final JTable table = new JTable(tableModel);
    private JComboBox<String> tipoFilter;
    private JComboBox<String> categoriaFilter;
    private JComboBox<String> ordenarCombo;

    public Main(MidiaControle controle) {
        super("Gerenciador de Mídias");
        this.controle = controle;
        init();
    }

    // ------------------------------------------------------------
    // MÉTODO UTILITÁRIO PARA LER O MAIOR ID NOS ARQUIVOS .tpoo
    // ------------------------------------------------------------
    private static int findMaxIdInStorage(Path storageDir) {
        int max = 0;
        try {
            if (!Files.exists(storageDir)) return 0;
            try (DirectoryStream<Path> ds = Files.newDirectoryStream(storageDir, "*.tpoo")) {
                for (Path p : ds) {
                    List<String> lines = Files.readAllLines(p);
                    for (String l : lines) {
                        if (l.startsWith("id=")) {
                            String v = l.substring(3).trim();
                            try {
                                int id = Integer.parseInt(v);
                                if (id > max) max = id;
                            } catch (NumberFormatException ex) {
                                // ignora
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return max;
    }

    private void init() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel top = new JPanel();
        JButton btnAdd = new JButton("Incluir");
        btnAdd.addActionListener(e -> onIncluir());
        JButton btnEdit = new JButton("Editar");
        btnEdit.addActionListener(e -> onEditar());
        JButton btnDel = new JButton("Remover");
        btnDel.addActionListener(e -> onRemover());
        JButton btnMove = new JButton("Mover");
        btnMove.addActionListener(e -> onMover());
        JButton btnRename = new JButton("Renomear");
        btnRename.addActionListener(e -> onRenomear());

        tipoFilter = new JComboBox<>(new String[]{"", "MUSICA", "FILME", "LIVRO"});
        tipoFilter.addActionListener(e -> reloadTable());
        categoriaFilter = new JComboBox<>(new String[]{"", "acao", "aventura", "rock"});
        categoriaFilter.addActionListener(e -> reloadTable());
        ordenarCombo = new JComboBox<>(new String[]{"", "ALFABETICA", "DURACAO"});
        ordenarCombo.addActionListener(e -> reloadTable());

        top.add(btnAdd);
        top.add(btnEdit);
        top.add(btnDel);
        top.add(btnMove);
        top.add(btnRename);
        top.add(new JLabel("Tipo:"));
        top.add(tipoFilter);
        top.add(new JLabel("Categoria:"));
        top.add(categoriaFilter);
        top.add(new JLabel("Ordenar:"));
        top.add(ordenarCombo);

        add(top, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        try {
            reloadTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }

    private void reloadTable() {
        try {
            Optional<String> t = Optional.ofNullable((String) tipoFilter.getSelectedItem()).filter(s -> !s.isEmpty());
            Optional<String> c = Optional.ofNullable((String) categoriaFilter.getSelectedItem()).filter(s -> !s.isEmpty());
            Optional<String> o = Optional.ofNullable((String) ordenarCombo.getSelectedItem()).filter(s -> !s.isEmpty());
            List<Midia> res = controle.listarFiltrados(t, c, o);
            tableModel.setMedias(res);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar: " + ex.getMessage());
        }
    }

    private void onIncluir() {
        MidiaDialog dlg = new MidiaDialog(this);
        dlg.setVisible(true);
        if (dlg.isConfirmed()) {
        	Midia m = dlg.buildMediaFromForm();
            try {
                controle.incluirMedia(m);
                reloadTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro incluir: " + ex.getMessage());
            }
        }
    }

    private void onEditar() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma mídia.");
            return;
        }

        Midia m = tableModel.getMediaAt(r);
        MidiaDialog dlg = new MidiaDialog(this);
        dlg.fillFromMedia(m);
        dlg.setVisible(true);

        if (dlg.isConfirmed()) {
            try {
                Midia updated = dlg.buildMediaFromForm();

                // *** AQUI ESTÁ A CORREÇÃO PRINCIPAL ***
                updated.setId(m.getId());
                updated.setLocal(m.getLocal()); // preserva arquivo original

                controle.atualizarMidia(updated);

                reloadTable();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro editar: " + ex.getMessage());
            }
        }
    }


    private void onRemover() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma mídia.");
            return;
        }
        Midia m = tableModel.getMediaAt(r);
        int conf = JOptionPane.showConfirmDialog(this, "Remover " + m.getTitulo() + "?", "Confirmar",
                JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION) {
            try {
                controle.removerMidia(m.getId());
                reloadTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro remover: " + ex.getMessage());
            }
        }
    }

    private void onMover() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma mídia.");
            return;
        }
        Midia m = tableModel.getMediaAt(r);
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int res = fc.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            try {
                controle.moverMedia(m.getId(), fc.getSelectedFile().toPath());
                reloadTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro mover: " + ex.getMessage());
            }
        }
    }

    private void onRenomear() {
        int r = table.getSelectedRow();
        if (r < 0) {
            JOptionPane.showMessageDialog(this, "Selecione uma mídia.");
            return;
        }
        Midia m = tableModel.getMediaAt(r);
        String novo = JOptionPane.showInputDialog(this,
                "Novo nome de arquivo (somente nome):", "",
                JOptionPane.PLAIN_MESSAGE);
        if (novo != null && !novo.trim().isEmpty()) {
            try {
                controle.renomearArquivo(m.getId(), novo.trim());
                reloadTable();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro renomear: " + ex.getMessage());
            }
        }
    }

    // ------------------------------------------------------------
    // MAIN — inicializa IdGenerator automaticamente!
    // ------------------------------------------------------------
    public static void main(String[] args) {
        try {
            Path data = Paths.get("data");

            int maxId = findMaxIdInStorage(data);
            IdGerador.iniciar(maxId);

            FileMidiaDAO dao = new FileMidiaDAO(data);
            MidiaControle controle = new MidiaControle(dao);

            javax.swing.SwingUtilities.invokeLater(() -> {
                Main f = new Main(controle);
                f.setVisible(true);
            });

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro inicial: " + ex.getMessage());
        }
    }
}
