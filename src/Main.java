import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.*;
import java.io.File;

public class Main extends JFrame {

    private static final String SRC_PATH = new File("src").getAbsolutePath() + File.separator;
    private static final String FILMES_PATH = SRC_PATH + "filmes.csv";
    private static final String SALAS_PATH = SRC_PATH + "salas.csv";
    private static final String SESSOES_PATH = SRC_PATH + "sessoes.csv";
    private static final String VENDAS_PATH = SRC_PATH + "vendas.csv";

    private ArrayList<Filme> filmes;
    private ArrayList<Sala> salas;
    private ArrayList<Sessao> sessoes;
    private ArrayList<VendaDeIngressos> vendas;
    private JPanel mainPanel;
    private JTable table;
    private DefaultTableModel tableModel;

    public Main() {
        filmes = new ArrayList<>();
        salas = new ArrayList<>();
        sessoes = new ArrayList<>();
        vendas = new ArrayList<>();

        File srcDir = new File("src");
        if (!srcDir.exists()) {
            srcDir.mkdir();
            JOptionPane.showMessageDialog(this,
                    "Diretório 'src' foi criado para armazenar os arquivos CSV.");
        }

        carregarDados();
        configurarJanela();
        criarMenu();
        criarPainelPrincipal();
        setVisible(true);
    }

    private void configurarJanela() {
        setTitle("Sistema de Gerenciamento de Cinema");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salvarDados();
            }
        });
    }

    private void criarPainelPrincipal() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Criar tabela padrão
        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }

    private void criarMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Menu Filmes
        JMenu menuFilmes = new JMenu("Filmes");
        menuBar.add(menuFilmes);
        adicionarItemMenu(menuFilmes, "Adicionar Filme", e -> adicionarFilme());
        adicionarItemMenu(menuFilmes, "Listar Filmes", e -> listarFilmes());
        adicionarItemMenu(menuFilmes, "Atualizar Filme", e -> atualizarFilme());
        adicionarItemMenu(menuFilmes, "Deletar Filme", e -> deletarFilme());

        // Menu Salas
        JMenu menuSalas = new JMenu("Salas");
        menuBar.add(menuSalas);
        adicionarItemMenu(menuSalas, "Adicionar Sala", e -> adicionarSala());
        adicionarItemMenu(menuSalas, "Listar Salas", e -> listarSalas());
        adicionarItemMenu(menuSalas, "Atualizar Sala", e -> atualizarSala());
        adicionarItemMenu(menuSalas, "Deletar Sala", e -> deletarSala());

        // Menu Sessões
        JMenu menuSessoes = new JMenu("Sessões");
        menuBar.add(menuSessoes);
        adicionarItemMenu(menuSessoes, "Adicionar Sessão", e -> adicionarSessao());
        adicionarItemMenu(menuSessoes, "Listar Sessões", e -> listarSessoes());
        adicionarItemMenu(menuSessoes, "Atualizar Sessão", e -> atualizarSessao());
        adicionarItemMenu(menuSessoes, "Deletar Sessão", e -> deletarSessao());

        // Menu Ingressos
        JMenu menuIngressos = new JMenu("Ingressos");
        menuBar.add(menuIngressos);
        adicionarItemMenu(menuIngressos, "Vender Ingresso", e -> venderIngresso());

        // Menu Relatórios
        JMenu menuRelatorios = new JMenu("Relatórios");
        menuBar.add(menuRelatorios);
        adicionarItemMenu(menuRelatorios, "Vendas por Filme", e -> relatorioVendasPorFilme());
        adicionarItemMenu(menuRelatorios, "Vendas por Horário", e -> relatorioVendasPorHorario());
        adicionarItemMenu(menuRelatorios, "Salas mais Utilizadas", e -> relatorioSalasMaisUtilizadas());
        adicionarItemMenu(menuRelatorios, "Filmes mais Exibidos", e -> relatorioFilmesMaisExibidos());
    }

    private void adicionarItemMenu(JMenu menu, String nome, ActionListener listener) {
        JMenuItem menuItem = new JMenuItem(nome);
        menuItem.addActionListener(listener);
        menu.add(menuItem);
    }

    // Métodos de Gerenciamento de Filmes
    private void adicionarFilme() {
        JTextField codigoField = new JTextField();
        JTextField nomeField = new JTextField();
        JTextField generoField = new JTextField();
        JTextField sinopseField = new JTextField();

        Object[] fields = {
                "Código:", codigoField,
                "Nome:", nomeField,
                "Gênero:", generoField,
                "Sinopse:", sinopseField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Adicionar Filme",
                JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                Filme filme = new Filme(
                        codigoField.getText(),
                        nomeField.getText(),
                        generoField.getText(),
                        sinopseField.getText()
                );
                filmes.add(filme);
                JOptionPane.showMessageDialog(this, "Filme adicionado com sucesso!");
                listarFilmes();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar filme: " + e.getMessage());
            }
        }
    }

    private void listarFilmes() {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"Código", "Nome", "Gênero", "Sinopse"});

        for (Filme filme : filmes) {
            tableModel.addRow(new Object[]{
                    filme.getCodigo(),
                    filme.getNome(),
                    filme.getGenero(),
                    filme.getSinopse()
            });
        }
    }

    private void atualizarFilme() {
        if (filmes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há filmes cadastrados.");
            return;
        }

        String[] codigos = filmes.stream()
                .map(Filme::getCodigo)
                .toArray(String[]::new);

        String selectedCodigo = (String) JOptionPane.showInputDialog(this,
                "Selecione o filme para atualizar:",
                "Atualizar Filme",
                JOptionPane.QUESTION_MESSAGE,
                null,
                codigos,
                codigos[0]);

        if (selectedCodigo != null) {
            Filme filmeParaAtualizar = filmes.stream()
                    .filter(f -> f.getCodigo().equals(selectedCodigo))
                    .findFirst()
                    .orElse(null);

            if (filmeParaAtualizar != null) {
                JTextField nomeField = new JTextField(filmeParaAtualizar.getNome());
                JTextField generoField = new JTextField(filmeParaAtualizar.getGenero());
                JTextField sinopseField = new JTextField(filmeParaAtualizar.getSinopse());

                Object[] fields = {
                        "Nome:", nomeField,
                        "Gênero:", generoField,
                        "Sinopse:", sinopseField
                };

                int result = JOptionPane.showConfirmDialog(this, fields,
                        "Atualizar Filme", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    filmeParaAtualizar.setNome(nomeField.getText());
                    filmeParaAtualizar.setGenero(generoField.getText());
                    filmeParaAtualizar.setSinopse(sinopseField.getText());

                    JOptionPane.showMessageDialog(this, "Filme atualizado com sucesso!");
                    listarFilmes();
                }
            }
        }
    }

    private void deletarFilme() {
        if (filmes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há filmes cadastrados.");
            return;
        }

        String[] codigos = filmes.stream()
                .map(Filme::getCodigo)
                .toArray(String[]::new);

        String selectedCodigo = (String) JOptionPane.showInputDialog(this,
                "Selecione o filme para deletar:",
                "Deletar Filme",
                JOptionPane.QUESTION_MESSAGE,
                null,
                codigos,
                codigos[0]);

        if (selectedCodigo != null) {
            filmes.removeIf(f -> f.getCodigo().equals(selectedCodigo));
            JOptionPane.showMessageDialog(this, "Filme removido com sucesso!");
            listarFilmes();
        }
    }

    // Métodos de Gerenciamento de Salas
    private void adicionarSala() {
        JTextField numeroField = new JTextField();
        JTextField assentosField = new JTextField();

        Object[] fields = {
                "Número da Sala:", numeroField,
                "Quantidade de Assentos:", assentosField
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                "Adicionar Sala", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Sala sala = new Sala(
                        Integer.parseInt(numeroField.getText()),
                        Integer.parseInt(assentosField.getText())
                );
                salas.add(sala);
                JOptionPane.showMessageDialog(this, "Sala adicionada com sucesso!");
                listarSalas();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar sala: " + e.getMessage());
            }
        }
    }

    private void listarSalas() {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{"Número da Sala", "Quantidade de Assentos"});

        for (Sala sala : salas) {
            tableModel.addRow(new Object[]{
                    sala.getNumero(),
                    sala.getQuantidadeDeAssentos()
            });
        }
    }

    private void atualizarSala() {
        if (salas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há salas cadastradas.");
            return;
        }

        String[] numeros = salas.stream()
                .map(s -> String.valueOf(s.getNumero()))
                .toArray(String[]::new);

        String selectedNumero = (String) JOptionPane.showInputDialog(this,
                "Selecione a sala para atualizar:",
                "Atualizar Sala",
                JOptionPane.QUESTION_MESSAGE,
                null,
                numeros,
                numeros[0]);

        if (selectedNumero != null) {
            int numero = Integer.parseInt(selectedNumero);
            Sala salaParaAtualizar = salas.stream()
                    .filter(s -> s.getNumero() == numero)
                    .findFirst()
                    .orElse(null);

            if (salaParaAtualizar != null) {
                JTextField assentosField = new JTextField(
                        String.valueOf(salaParaAtualizar.getQuantidadeDeAssentos()));

                Object[] fields = {
                        "Quantidade de Assentos:", assentosField
                };

                int result = JOptionPane.showConfirmDialog(this, fields,
                        "Atualizar Sala", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    try {
                        salaParaAtualizar.setQuantidadeDeAssentos(
                                Integer.parseInt(assentosField.getText()));
                        JOptionPane.showMessageDialog(this, "Sala atualizada com sucesso!");
                        listarSalas();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this,
                                "Erro ao atualizar sala: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void deletarSala() {
        if (salas.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há salas cadastradas.");
            return;
        }

        String[] numeros = salas.stream()
                .map(s -> String.valueOf(s.getNumero()))
                .toArray(String[]::new);

        String selectedNumero = (String) JOptionPane.showInputDialog(this,
                "Selecione a sala para deletar:",
                "Deletar Sala",
                JOptionPane.QUESTION_MESSAGE,
                null,
                numeros,
                numeros[0]);

        if (selectedNumero != null) {
            int numero = Integer.parseInt(selectedNumero);
            salas.removeIf(s -> s.getNumero() == numero);
            JOptionPane.showMessageDialog(this, "Sala removida com sucesso!");
            listarSalas();
        }
    }

    // Métodos de Gerenciamento de Sessões
    private void adicionarSessao() {
        if (filmes.isEmpty() || salas.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "É necessário ter filmes e salas cadastrados para criar uma sessão.");
            return;
        }

        JComboBox<String> salaCombo = new JComboBox<>(
                salas.stream()
                        .map(s -> String.valueOf(s.getNumero()))
                        .toArray(String[]::new));

        JComboBox<String> filmeCombo = new JComboBox<>(
                filmes.stream()
                        .map(Filme::getNome)
                        .toArray(String[]::new));

        JTextField dataField = new JTextField("YYYY-MM-DD");
        JTextField horaField = new JTextField("HH:mm");

        Object[] fields = {
                "Sala:", salaCombo,
                "Filme:", filmeCombo,
                "Data:", dataField,
                "Hora:", horaField
        };

        int result = JOptionPane.showConfirmDialog(this, fields,
                "Adicionar Sessão", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String selectedSala = (String) salaCombo.getSelectedItem();
                String selectedFilme = (String) filmeCombo.getSelectedItem();

                Sala sala = salas.stream()
                        .filter(s -> String.valueOf(s.getNumero()).equals(selectedSala))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Sala não encontrada"));

                Filme filme = filmes.stream()
                        .filter(f -> f.getNome().equals(selectedFilme))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Filme não encontrado"));

                LocalDateTime horario = LocalDateTime.parse(
                        dataField.getText() + " " + horaField.getText(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                Sessao sessao = new Sessao(sala, horario, filme);
                sessoes.add(sessao);

                JOptionPane.showMessageDialog(this, "Sessão adicionada com sucesso!");
                listarSessoes();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao adicionar sessão: " + e.getMessage());
            }
        }
    }

    private void listarSessoes() {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{
                "Sala", "Filme", "Data", "Hora", "Assentos Totais"
        });

        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

        for (Sessao sessao : sessoes) {
            tableModel.addRow(new Object[]{
                    sessao.getSala().getNumero(),
                    sessao.getFilme().getNome(),
                    sessao.getHorario().format(dateFormat),
                    sessao.getHorario().format(timeFormat),
                    sessao.getSala().getQuantidadeDeAssentos()
            });
        }
    }

    private void atualizarSessao() {
        if (sessoes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há sessões cadastradas.");
            return;
        }

        String[] sessoesStr = sessoes.stream()
                .map(s -> String.format("%s - %s - %s",
                        s.getFilme().getNome(),
                        s.getSala().getNumero(),
                        s.getHorario().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .toArray(String[]::new);

        String selectedSessao = (String) JOptionPane.showInputDialog(this,
                "Selecione a sessão para atualizar:",
                "Atualizar Sessão",
                JOptionPane.QUESTION_MESSAGE,
                null,
                sessoesStr,
                sessoesStr[0]);

        if (selectedSessao != null) {
            int index = Arrays.asList(sessoesStr).indexOf(selectedSessao);
            Sessao sessaoParaAtualizar = sessoes.get(index);

            JComboBox<String> salaCombo = new JComboBox<>(
                    salas.stream()
                            .map(s -> String.valueOf(s.getNumero()))
                            .toArray(String[]::new));
            salaCombo.setSelectedItem(String.valueOf(sessaoParaAtualizar.getSala().getNumero()));

            JComboBox<String> filmeCombo = new JComboBox<>(
                    filmes.stream()
                            .map(Filme::getNome)
                            .toArray(String[]::new));
            filmeCombo.setSelectedItem(sessaoParaAtualizar.getFilme().getNome());

            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");

            JTextField dataField = new JTextField(
                    sessaoParaAtualizar.getHorario().format(dateFormat));
            JTextField horaField = new JTextField(
                    sessaoParaAtualizar.getHorario().format(timeFormat));

            Object[] fields = {
                    "Sala:", salaCombo,
                    "Filme:", filmeCombo,
                    "Data:", dataField,
                    "Hora:", horaField
            };

            int result = JOptionPane.showConfirmDialog(this, fields,
                    "Atualizar Sessão", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    String selectedSalaNum = (String) salaCombo.getSelectedItem();
                    String selectedFilmeNome = (String) filmeCombo.getSelectedItem();

                    Sala novaSala = salas.stream()
                            .filter(s -> String.valueOf(s.getNumero()).equals(selectedSalaNum))
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Sala não encontrada"));

                    Filme novoFilme = filmes.stream()
                            .filter(f -> f.getNome().equals(selectedFilmeNome))
                            .findFirst()
                            .orElseThrow(() -> new IllegalStateException("Filme não encontrado"));

                    LocalDateTime novoHorario = LocalDateTime.parse(
                            dataField.getText() + " " + horaField.getText(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

                    sessoes.set(index, new Sessao(novaSala, novoHorario, novoFilme));
                    JOptionPane.showMessageDialog(this, "Sessão atualizada com sucesso!");
                    listarSessoes();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao atualizar sessão: " + e.getMessage());
                }
            }
        }
    }

    private void deletarSessao() {
        if (sessoes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há sessões cadastradas.");
            return;
        }

        String[] sessoesStr = sessoes.stream()
                .map(s -> String.format("%s - %s - %s",
                        s.getFilme().getNome(),
                        s.getSala().getNumero(),
                        s.getHorario().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .toArray(String[]::new);

        String selectedSessao = (String) JOptionPane.showInputDialog(this,
                "Selecione a sessão para deletar:",
                "Deletar Sessão",
                JOptionPane.QUESTION_MESSAGE,
                null,
                sessoesStr,
                sessoesStr[0]);

        if (selectedSessao != null) {
            int index = Arrays.asList(sessoesStr).indexOf(selectedSessao);
            sessoes.remove(index);
            JOptionPane.showMessageDialog(this, "Sessão removida com sucesso!");
            listarSessoes();
        }
    }

    private void venderIngresso() {
        if (sessoes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há sessões disponíveis.");
            return;
        }

        String[] sessoesStr = sessoes.stream()
                .map(s -> String.format("%s - %s - %s",
                        s.getFilme().getNome(),
                        s.getSala().getNumero(),
                        s.getHorario().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))))
                .toArray(String[]::new);

        String selectedSessao = (String) JOptionPane.showInputDialog(this,
                "Selecione a sessão:",
                "Vender Ingresso",
                JOptionPane.QUESTION_MESSAGE,
                null,
                sessoesStr,
                sessoesStr[0]);

        if (selectedSessao != null) {
            int index = Arrays.asList(sessoesStr).indexOf(selectedSessao);
            Sessao sessao = sessoes.get(index);

            VendaDeIngressos venda = vendas.stream()
                    .filter(v -> v.getSessao() == sessao)
                    .findFirst()
                    .orElseGet(() -> {
                        VendaDeIngressos novaVenda = new VendaDeIngressos(sessao);
                        vendas.add(novaVenda);
                        return novaVenda;
                    });

            String quantidadeStr = JOptionPane.showInputDialog(this,
                    "Quantidade de ingressos:",
                    "1");

            if (quantidadeStr != null) {
                try {
                    int quantidade = Integer.parseInt(quantidadeStr);
                    venda.venderIngresso(quantidade);
                    JOptionPane.showMessageDialog(this, "Venda realizada com sucesso!");
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Erro ao vender ingressos: " + e.getMessage());
                }
            }
        }
    }

    // Métodos de Relatórios
    private void relatorioVendasPorFilme() {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{
                "Filme", "Total de Ingressos Vendidos", "Valor Total"
        });

        Map<Filme, Integer> vendasPorFilme = new HashMap<>();
        for (VendaDeIngressos venda : vendas) {
            Filme filme = venda.getSessao().getFilme();
            vendasPorFilme.merge(filme, venda.getIngressosVendidos(), Integer::sum);
        }

        for (Map.Entry<Filme, Integer> entry : vendasPorFilme.entrySet()) {
            tableModel.addRow(new Object[]{
                    entry.getKey().getNome(),
                    entry.getValue(),
                    String.format("R$ %.2f", entry.getValue() * 20.0) // Preço fixo de R$ 20,00
            });
        }
    }

    private void relatorioVendasPorHorario() {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{
                "Horário", "Total de Ingressos Vendidos"
        });

        Map<LocalDateTime, Integer> vendasPorHorario = new TreeMap<>();
        for (VendaDeIngressos venda : vendas) {
            LocalDateTime horario = venda.getSessao().getHorario();
            vendasPorHorario.merge(horario, venda.getIngressosVendidos(), Integer::sum);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        for (Map.Entry<LocalDateTime, Integer> entry : vendasPorHorario.entrySet()) {
            tableModel.addRow(new Object[]{
                    entry.getKey().format(formatter),
                    entry.getValue()
            });
        }
    }

    private void relatorioSalasMaisUtilizadas() {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{
                "Sala", "Quantidade de Sessões", "Total de Ingressos Vendidos"
        });

        Map<Sala, Integer> sessoesPorSala = new HashMap<>();
        Map<Sala, Integer> ingressosPorSala = new HashMap<>();

        for (Sessao sessao : sessoes) {
            sessoesPorSala.merge(sessao.getSala(), 1, Integer::sum);
        }

        for (VendaDeIngressos venda : vendas) {
            Sala sala = venda.getSessao().getSala();
            ingressosPorSala.merge(sala, venda.getIngressosVendidos(), Integer::sum);
        }

        for (Sala sala : salas) {
            tableModel.addRow(new Object[]{
                    sala.getNumero(),
                    sessoesPorSala.getOrDefault(sala, 0),
                    ingressosPorSala.getOrDefault(sala, 0)
            });
        }
    }

    private void relatorioFilmesMaisExibidos() {
        tableModel.setRowCount(0);
        tableModel.setColumnIdentifiers(new String[]{
                "Filme", "Quantidade de Sessões", "Total de Ingressos Vendidos"
        });

        Map<Filme, Integer> sessoesPorFilme = new HashMap<>();
        Map<Filme, Integer> ingressosPorFilme = new HashMap<>();

        for (Sessao sessao : sessoes) {
            sessoesPorFilme.merge(sessao.getFilme(), 1, Integer::sum);
        }

        for (VendaDeIngressos venda : vendas) {
            Filme filme = venda.getSessao().getFilme();
            ingressosPorFilme.merge(filme, venda.getIngressosVendidos(), Integer::sum);
        }

        for (Filme filme : filmes) {
            tableModel.addRow(new Object[]{
                    filme.getNome(),
                    sessoesPorFilme.getOrDefault(filme, 0),
                    ingressosPorFilme.getOrDefault(filme, 0)
            });
        }
    }

    private void carregarDados() {
        try {
            // Create files if they don't exist
            new File("src").mkdirs();
            Arrays.asList(FILMES_PATH, SALAS_PATH, SESSOES_PATH, VENDAS_PATH)
                    .forEach(path -> {
                        try {
                            new File(path).createNewFile();
                        } catch (IOException e) {
                            System.err.println("Erro ao criar arquivo: " + path);
                        }
                    });

            if (new File(FILMES_PATH).length() == 0) {
                // First time running the program
                JOptionPane.showMessageDialog(this, "Bem-vindo! Por favor, adicione filmes e salas para começar.");
            }

            // Load data
            filmes = new ArrayList<>(Filme.carregarFilmes(FILMES_PATH));
            salas = new ArrayList<>(Sala.carregarSalas(SALAS_PATH));
            sessoes = new ArrayList<>(Sessao.carregarSessoes(SESSOES_PATH));
            vendas = new ArrayList<>(VendaDeIngressos.carregarVendas(VENDAS_PATH));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void salvarDados() {
        try {
            Filme.salvarFilmes(filmes, FILMES_PATH);
            Sala.salvarSalas(salas, SALAS_PATH);
            Sessao.salvarSessoes(sessoes, SESSOES_PATH);
            VendaDeIngressos.salvarVendas(vendas, VENDAS_PATH);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar dados: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        GeradorCSV.main(args);
        SwingUtilities.invokeLater(() -> new Main());
    }
}