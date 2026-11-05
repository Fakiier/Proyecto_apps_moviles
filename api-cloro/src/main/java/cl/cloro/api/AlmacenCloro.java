package cl.cloro.api;

import cl.cloro.api.Modelos.Medicion;
import cl.cloro.api.Modelos.NuevaMedicion;
import cl.cloro.api.Modelos.Punto;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class AlmacenCloro {

    private final List<Punto> puntos = List.of(
            new Punto(1, "GN-01", "Granja Norte", "Galpón 1", "Bebedero"),
            new Punto(2, "GN-02", "Granja Norte", "Galpón 2", "Bebedero"),
            new Punto(3, "GN-03", "Granja Norte", "Tanque principal", "Tanque"),
            new Punto(4, "GN-04", "Granja Norte", "Línea de distribución", "Línea"),
            new Punto(5, "GS-01", "Granja Sur", "Galpón 1", "Bebedero"),
            new Punto(6, "GS-02", "Granja Sur", "Galpón 2", "Bebedero"),
            new Punto(7, "GS-03", "Granja Sur", "Tanque principal", "Tanque"),
            new Punto(8, "GS-04", "Granja Sur", "Línea de distribución", "Línea"),
            new Punto(9, "GC-01", "Granja Centro", "Galpón 1", "Bebedero"),
            new Punto(10, "GC-02", "Granja Centro", "Galpón 2", "Bebedero"),
            new Punto(11, "GC-03", "Granja Centro", "Tanque principal", "Tanque"),
            new Punto(12, "GC-04", "Granja Centro", "Línea de distribución", "Línea")
    );

    // Nivel de cloro típico de cada punto (mismo orden que la lista de puntos)
    private final double[] base = {3.80, 2.60, 4.20, 3.10, 1.70, 4.50, 5.60, 3.90, 6.40, 3.30, 4.80, 2.90};

    private final List<Medicion> mediciones = new ArrayList<>();
    private final AtomicLong siguienteId = new AtomicLong(1);

    @PostConstruct
    public void iniciar() {
        generarCiclo();
    }

    // Cada ciclo genera una medición nueva por punto
    @Scheduled(
            fixedRateString = "${cloro.intervalo-ms:60000}",
            initialDelayString = "${cloro.intervalo-ms:60000}"
    )
    public synchronized void generarCiclo() {
        long ahora = System.currentTimeMillis();
        ThreadLocalRandom azar = ThreadLocalRandom.current();
        for (int i = 0; i < puntos.size(); i++) {
            double nivel = base[i] + azar.nextDouble(-0.6, 0.6);
            if (azar.nextInt(10) == 0) {
                nivel += azar.nextBoolean() ? 2.0 : -2.0;   // desviación ocasional
            }
            guardar(puntos.get(i).id(), nivel, ahora, "Sensor simulado");
        }
    }

    private Medicion guardar(int puntoId, double nivel, long fecha, String por) {
        double redondeado = Math.round(Math.max(0.1, Math.min(9.9, nivel)) * 100.0) / 100.0;
        Medicion m = new Medicion(siguienteId.getAndIncrement(), puntoId, redondeado, fecha, por);
        mediciones.add(m);
        if (mediciones.size() > 5000) {
            mediciones.remove(0);
        }
        return m;
    }

    public List<Punto> puntos() {
        return puntos;
    }

    public boolean existePunto(int id) {
        return puntos.stream().anyMatch(p -> p.id() == id);
    }

    public synchronized List<Medicion> desde(long desde) {
        return mediciones.stream()
                .filter(m -> m.fechaMillis() > desde)
                .sorted(Comparator.comparingLong(Medicion::fechaMillis))
                .toList();
    }

    public synchronized List<Medicion> ultimas() {
        Map<Integer, Medicion> mapa = new HashMap<>();
        for (Medicion m : mediciones) {
            Medicion actual = mapa.get(m.puntoId());
            if (actual == null || m.fechaMillis() >= actual.fechaMillis()) {
                mapa.put(m.puntoId(), m);
            }
        }
        return mapa.values().stream()
                .sorted(Comparator.comparingInt(Medicion::puntoId))
                .toList();
    }

    // Guarda las mediciones que sube la app. Si un reintento repite una, no la duplica.
    public synchronized List<Medicion> agregar(List<NuevaMedicion> nuevas) {
        List<Medicion> resultado = new ArrayList<>();
        for (NuevaMedicion n : nuevas) {
            Optional<Medicion> existente = mediciones.stream()
                    .filter(m -> m.puntoId() == n.puntoId()
                            && m.fechaMillis() == n.fechaMillis()
                            && Objects.equals(m.registradoPor(), n.registradoPor()))
                    .findFirst();
            resultado.add(existente.orElseGet(
                    () -> guardar(n.puntoId(), n.nivelCloro(), n.fechaMillis(), n.registradoPor())));
        }
        return resultado;
    }
}