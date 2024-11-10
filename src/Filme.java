import java.io.*;
import java.util.*;

public class Filme {
    private String codigo;
    private String nome;
    private String genero;
    private String sinopse;

    public Filme(String codigo, String nome, String genero, String sinopse) {
        this.codigo = codigo;
        this.nome = nome;
        this.genero = genero;
        this.sinopse = sinopse;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return codigo + "," + nome + "," + genero + "," + sinopse;
    }

    public static Filme fromString(String csv) {
        try {
            String[] dados = csv.split(",");
            if (dados.length < 4) {
                throw new IllegalArgumentException("Dados insuficientes para criar um Filme");
            }
            return new Filme(dados[0], dados[1], dados[2], dados[3]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato inválido para Filme: " + csv);
        }
    }

    public static List<Filme> carregarFilmes(String filepath) throws IOException {
        List<Filme> filmes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    filmes.add(fromString(line));
                }
            }
        }
        return filmes;
    }

    public static void salvarFilmes(List<Filme> filmes, String filepath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filepath))) {
            for (Filme filme : filmes) {
                pw.println(filme.toString());
            }
        }
    }
}