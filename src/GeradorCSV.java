
        for (String filePath : files) {
            File file = new File(filePath);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                    // Write header line to each file
                    FileWriter writer = new FileWriter(file);
                    switch (filePath) {
                        case "src/filmes.csv":
                            writer.write("codigo,nome,genero,sinopse\n");
                            break;
                        case "src/salas.csv":
                            writer.write("numero,quantidade_assentos\n");
                            break;
                        case "src/sessoes.csv":
                            writer.write("sala_numero,horario,filme_codigo\n");
                            break;
                        case "src/vendas.csv":
                            writer.write("sessao_id,ingressos_vendidos\n");
                            break;
                    }
                    writer.close();
                    System.out.println("Created file: " + filePath);
                } catch (IOException e) {
                    System.err.println("Error creating file " + filePath + ": " + e.getMessage());
                }
            }
        }
    }
}