package cl.cloro.api;

public final class Modelos {
    private Modelos() {}

    public record Punto(int id, String codigo, String granja, String nombre, String tipo) {}

    public record Medicion(long id, int puntoId, double nivelCloro, long fechaMillis, String registradoPor) {}

    public record NuevaMedicion(int puntoId, double nivelCloro, long fechaMillis, String registradoPor) {}
}