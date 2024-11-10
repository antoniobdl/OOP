import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Sessao {
    private Sala sala;
    private LocalDateTime horario;
    private Filme filme;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Sessao(Sala sala, LocalDateTime horario, Filme filme) {
        if (sala == null || horario == null || filme == null) {
            throw new IllegalArgumentException("Sala, horário e filme não podem ser nulos");
        }
        this.sala = sala;
        this.horario = horario;
        this.filme = filme;
    }

    public Sala getSala() {
        return sala;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public Filme getFilme() {
        return filme;
    }

    @Override
    public String toString() {
        return sala.getNumero() + "," + sala.getQuantidadeDeAssentos() + "," +
                horario.format(DATE_FORMAT) + "," +
                filme.getCodigo() + "," + filme.getNome() + "," +
                filme.getGenero() + "," + filme.getSinopse();
    }

    public static Sessao fromString(String csv) {
        try {
            String[] partes = csv.split(",");
            // Check if the line is a header
            if (partes[0].equalsIgnoreCase("sala_numero")) {
                return null; // This is the header, skip it
            }
            Sala sala = new Sala(Integer.parseInt(partes[0]), Integer.parseInt(partes[1]));
            LocalDateTime horario = LocalDateTime.parse(partes[2], DATE_FORMAT);
            Filme filme = new Filme(partes[3], partes[4], partes[5], partes[6]);

            return new Sessao(sala, horario, filme);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato inválido para Sessão: " + csv);
        }
    }


    public static List<Sessao> carregarSessoes(String filepath) throws IOException {
        List<Sessao> sessoes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Sessao sessao = fromString(line);
                    if (sessao != null) {
                        sessoes.add(sessao);
                    }
                }
            }
        }
        return sessoes;
    }

    public static void salvarSessoes(List<Sessao> sessoes, String filepath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filepath))) {
            // Write header
            pw.println("sala_numero,horario,filme_codigo,nome,genero,sinopse");
            // Write data
            for (Sessao sessao : sessoes) {
                pw.println(sessao.toString());
            }
        }
    }

}