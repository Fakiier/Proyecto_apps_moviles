package cl.cloro.api;

import cl.cloro.api.Modelos.Medicion;
import cl.cloro.api.Modelos.NuevaMedicion;
import cl.cloro.api.Modelos.Punto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CloroController {

    private final AlmacenCloro almacen;

    public CloroController(AlmacenCloro almacen) {
        this.almacen = almacen;
    }

    @GetMapping("/puntos")
    public List<Punto> puntos() {
        return almacen.puntos();
    }

    @GetMapping("/mediciones/ultimas")
    public List<Medicion> ultimas() {
        return almacen.ultimas();
    }

    // Mediciones posteriores a la fecha indicada (en milisegundos). Con 0 devuelve todas.
    @GetMapping("/mediciones")
    public List<Medicion> desde(@RequestParam(defaultValue = "0") long desde) {
        return almacen.desde(desde);
    }

    @PostMapping("/mediciones")
    public List<Medicion> registrar(@RequestBody List<NuevaMedicion> nuevas) {
        for (NuevaMedicion n : nuevas) {
            if (!almacen.existePunto(n.puntoId()) || n.nivelCloro() < 0 || n.nivelCloro() > 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Medición inválida");
            }
        }
        return almacen.agregar(nuevas);
    }
}