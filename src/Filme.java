public class Filme {
    private String codigoFilme;
    private String titulo;
    private String genero;
    private String sinopse;

    public Filme(String codigoFilme, String titulo, String genero, String sinopse) {
        this.codigoFilme = codigoFilme;
        this.titulo = titulo;
        this.genero = genero;
        this.sinopse = sinopse;
    }

    public String getCodigoFilme() {
        return codigoFilme;
    }

    public void setCodigoFilme(String codigoFilme) {
        this.codigoFilme = codigoFilme;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
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
        return "Filme{" +
                "codigoFilme='" + codigoFilme + '\'' +
                ", titulo='" + titulo + '\'' +
                ", genero='" + genero + '\'' +
                ", sinopse='" + sinopse + '\'' +
                '}';
    }
}