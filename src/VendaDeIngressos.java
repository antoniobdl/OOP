import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;


public class VendaDeIngressos {
    private Sessao sessao;
    private int ingressosVendidos;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public VendaDeIngressos(Sessao sessao) {
        if (sessao == null) {
            throw new IllegalArgumentException("Sessão não pode ser nula");
        }
        this.sessao = sessao;
        this.ingressosVendidos = 0;
    }

    public Sessao getSessao() {
        return sessao;
    }

    public int getIngressosVendidos() {
        return ingressosVendidos;
    }

    public void venderIngresso(int quantidade) {
        if (quantidade <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }

        int assentosDisponiveis = sessao.getSala().getQuantidadeDeAssentos() - ingressosVendidos;
        if (quantidade > assentosDisponiveis) {
            throw new IllegalStateException("Não há assentos suficientes disponíveis");
        }

        this.ingressosVendidos += quantidade;
    }

    @Override
    public String toString() {
        return sessao.getSala().getNumero() + "," +
                sessao.getSala().getQuantidadeDeAssentos() + "," +
                sessao.getHorario().format(DATE_FORMAT) + "," +
                sessao.getFilme().getCodigo() + "," +
                sessao.getFilme().getNome() + "," +
                sessao.getFilme().getGenero() + "," +
                sessao.getFilme().getSinopse() + "," +
                ingressosVendidos;
    }

    public static VendaDeIngressos fromString(String csv) {
        try {
            String[] partes = csv.split(",");
            // Check if the line is a header
            if (partes[0].equalsIgnoreCase("sessao_id")) {
                return null; // This is the header, skip it
            }
            Sessao sessao = Sessao.fromString(String.join(",", Arrays.copyOfRange(partes, 0, 7)));
            VendaDeIngressos venda = new VendaDeIngressos(sessao);
            venda.ingressosVendidos = Integer.parseInt(partes[7]);
            return venda;
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato inválido para Venda: " + csv);
        }
    }


    public static List<VendaDeIngressos> carregarVendas(String filepath) throws IOException {
        List<VendaDeIngressos> vendas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    VendaDeIngressos venda = fromString(line);
                    if (venda != null) {
                        vendas.add(venda);
                    }
                }
            }
        }
        return vendas;
    }

    public static void salvarVendas(List<VendaDeIngressos> vendas, String filepath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filepath))) {
            // Write header
            pw.println("sessao_id,ingressos_vendidos");
            for (VendaDeIngressos venda : vendas) {
                pw.println(venda.toString());
            }
        }
    }

}