import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Ocurrencias implements Serializable {
    Map<Integer, Integer> oc = new TreeMap<>();
    Integer frecuencia;

    public Ocurrencias(Integer posFT) {
        oc.put(posFT, 1);
        frecuencia = 1;
        System.out.print(" nueva ocurrencia");
    }

    public void añadirOcurrencia(Integer posFT) {
        if (oc.containsKey(posFT)) {
            Integer cantidad = oc.get(posFT);
            oc.remove(posFT);
            oc.put(posFT, cantidad + 1);
        }else{
            oc.put(posFT, 1);
        }
        frecuencia++;
        System.out.print(" tiene una frecuencia en el archivo de " + oc.get(posFT).intValue() + " y una frecuencia total de " + frecuencia);
    }
}
