import java.io.*;
import java.util.*;

public class Sala {
    private int numero;
    private int quantidadeDeAssentos;

    public Sala(int numero, int quantidadeDeAssentos) {
        this.numero = numero;
        this.quantidadeDeAssentos = quantidadeDeAssentos;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getQuantidadeDeAssentos() {
        return quantidadeDeAssentos;
    }

    public void setQuantidadeDeAssentos(int quantidadeDeAssentos) {
        this.quantidadeDeAssentos = quantidadeDeAssentos;
    }

    @Override
    public String toString() {
        return numero + "," + quantidadeDeAssentos;
    }

    public static Sala fromString(String csv) {
        try {
            String[] partes = csv.split(",");
            // Skip the header row
            if (partes[0].equals("numero") || partes[0].equalsIgnoreCase("numero")) {
                return null;
            }
            return new Sala(Integer.parseInt(partes[0]), Integer.parseInt(partes[1]));
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato inválido para Sala: " + csv);
        }
    }

    public static List<Sala> carregarSalas(String filepath) throws IOException {
        List<Sala> salas = new ArrayList<>();
        boolean primeiraLinha = true;
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue; // Skip the header row
                }
                if (!line.trim().isEmpty()) {
                    salas.add(fromString(line));
                }
            }
        }
        return salas;
    }

    public static void salvarSalas(List<Sala> salas, String filepath) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filepath))) {
            // Write header
            pw.println("numero,quantidade_assentos");
            // Write data
            for (Sala sala : salas) {
                pw.println(sala.toString());
            }
        }
    }
}