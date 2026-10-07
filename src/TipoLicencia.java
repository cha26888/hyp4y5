public enum TipoLicencia {
    A("ABC"),
    B("BC"),
    C("C"),
    M("M");

    private final String autorizadas;

    private TipoLicencia(String autorizadas) {
        this.autorizadas = autorizadas;
    }

    public boolean autoriza(TipoLicencia requerida) {
        return requerida != null && autorizadas.contains(requerida.name());
    }
}
