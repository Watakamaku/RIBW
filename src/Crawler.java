import java.io.*;
import java.util.*;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;

import org.xml.sax.SAXException;

public class Crawler implements Serializable {

    private PrintWriter flujoSalida;
    private Map<String, Ocurrencias> diccionario;
    private ArrayList<File> FAT;
    private String KEY = "KEY_DICTIONARY";

    public Crawler() {
        try {
            diccionario = new TreeMap();
            flujoSalida = new PrintWriter("C:\\Users\\Watakamaku\\Desktop\\prueba\\salida.txt");
            FAT = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public class PdfParse {

        public static void main(final String[] args) throws IOException,TikaException {



            //parsing the document using PDF parser


            //getting the content of the document
            System.out.println("Contents of the PDF :" + handler.toString());

            //getting metadata of the document
            System.out.println("Metadata of the PDF:");
            String[] metadataNames = metadata.names();

            for(String name : metadataNames) {
                System.out.println(name+ " : " + metadata.get(name));
            }
        }
    }

    public String parsePDF(File fichero){
        try {
            BodyContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            FileInputStream inputstream = new FileInputStream(fichero);
            ParseContext pcontext = new ParseContext();

            PDFParser pdfparser = new PDFParser();
            pdfparser.parse(inputstream, handler, metadata,pcontext);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public Integer obtenerPosFAT(File fichero) {
        for (int i = 0; i < FAT.size(); i++) {
            if (FAT.get(i).getName().equals(fichero.getName())) {
                return i;
            }
        }
        return -1;
    }

    public void saveObject() {
        Hashtable h = new Hashtable();
        h.put(KEY, diccionario);
        try {
            FileOutputStream fos = new FileOutputStream("h.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(h);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void loadObject() {
        try {
            FileInputStream fis = new FileInputStream("h.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Hashtable h = (Hashtable) ois.readObject();
            diccionario = (Map<String, Ocurrencias>) h.get(KEY);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void WordCount(File fichero) {
        System.out.println(fichero);
        try {
            BufferedReader flujoEntrada = new BufferedReader(new FileReader(fichero));
            String linea;

            while ((linea = flujoEntrada.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(linea, " ()[].,:;{}\"\'");
                while (st.hasMoreTokens()) {
                    String s = st.nextToken();
                    Object o = diccionario.get(s);
                    Integer posFT = obtenerPosFAT(fichero);
//                    System.out.print("\nLa palabra " + s + " en el archivo " + fichero.getName());
                    if (o == null) {
                        diccionario.put(s, new Ocurrencias(posFT));
                    } else {
                        Ocurrencias ocu = (Ocurrencias) o;
                        ocu.añadirOcurrencia(posFT);
                        diccionario.put(s, ocu);
                    }
                }
            }
            flujoEntrada.close();
            List claves = new ArrayList(diccionario.keySet());
            Collections.sort(claves);

            Iterator i = claves.iterator();

            while (i.hasNext()) {
                Object k = i.next();
                flujoSalida.write(k + " aparece un total de " + diccionario.get(k));
//                System.out.println(k + " aparece un total de " + diccionario.get(k));
            }
            flujoSalida.flush();
            flujoSalida.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     *Método que devuelve la extensión de un archivo
     */
    private String obtenerExtension(File fichero) {
        String nombre = fichero.getName();
        int tamanoExtension = nombre.lastIndexOf(".");
        if (tamanoExtension == -1) {
            return "";
        }
        return nombre.substring(tamanoExtension);
    }

    public void ListIt(File fichero) {

        if (!fichero.exists()) {
            System.out.println("No se puede leer " + fichero);
            return;
        }

        if (fichero.isDirectory()) {
            File[] listaFicheros = fichero.listFiles();
            for (int i = 0; i < listaFicheros.length; i++) {
                ListIt(listaFicheros[i]);
            }
        } else if (obtenerExtension(fichero).equals(".txt") || obtenerExtension(fichero).equals(".java")) {
            FAT.add(fichero);
            WordCount(fichero);
        } else {
//            System.out.println("Estoy en un archivo que no es un txt o un java: " + fichero.getName());
        }
    }

    public static void main(String[] Args) {
        Crawler c = new Crawler();
        c.loadObject();
        if (c.diccionario.size() == 0) {
            c.ListIt(new File("C:\\Users\\Watakamaku\\Desktop\\prueba"));
            c.saveObject();
        }else{
            System.out.println("El diccionario ya existe");
        }
    }
}
