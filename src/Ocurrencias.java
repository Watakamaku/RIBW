import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Ocurrencias implements Serializable {
    private Map<Integer, Integer> oc = new TreeMap<>();
    private Integer frecuencia;

    public Ocurrencias(Integer posFT) {
        oc.put(posFT, 1);
        frecuencia = 1;
        Crawler.getInstance().aumentarPalabrasTotales(posFT);
    }

    public Map<Integer, Integer> getTree() {
        return oc;
    }

    public void añadirOcurrencia(Integer posFT) {
        if (oc.containsKey(posFT)) {
            Integer cantidad = oc.get(posFT);
            oc.remove(posFT);
            oc.put(posFT, cantidad + 1);
        } else {
            oc.put(posFT, 1);
        }
        frecuencia++;
        Crawler.getInstance().aumentarPalabrasTotales(posFT);
    }

    public Integer getfrecuenciaTotal() {
        return frecuencia;
    }

    public void show() {
        System.out.print(oc.toString() + " y aparece un total de " + frecuencia + " veces\n");
    }
}
