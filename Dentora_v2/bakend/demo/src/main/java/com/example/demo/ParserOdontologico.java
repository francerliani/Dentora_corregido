package com.example.demo;

import java.util.regex.*;
import java.util.*;

public class ParserOdontologico {

    public static String parsear(String texto) {

        texto = texto.toLowerCase();

        List<Integer> piezas     = extraerPiezas(texto);
        List<String> superficies = detectarSuperficies(texto);
        List<String> patologias  = detectarPatologias(texto);
        String estado            = detectarEstado(texto);
        boolean esPrevio         = detectarSiEsPrevio(texto);

        return "{ " +
                "\"piezas\": "      + piezas                     + "," +
                "\"superficies\": " + convertirLista(superficies) + "," +
                "\"patologias\": "  + convertirLista(patologias)  + "," +
                "\"estado\": \""    + estado                      + "\"," +
                "\"esPrevio\": "    + esPrevio                    +
                " }";
    }

    // 🦷 EXTRAER PIEZAS — solo números FDI válidos de 2 dígitos
    private static List<Integer> extraerPiezas(String texto) {

        List<Integer> piezas = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\b(\\d{2})\\b");
        Matcher matcher = pattern.matcher(texto);

        while (matcher.find()) {
            int numero = Integer.parseInt(matcher.group());
            if (esPiezaValida(numero) && !piezas.contains(numero)) {
                piezas.add(numero);
            }
        }

        return piezas;
    }

    // ✅ VALIDAR SISTEMA FDI
    private static boolean esPiezaValida(int numero) {

        // Adultos
        if ((numero >= 11 && numero <= 18) ||
            (numero >= 21 && numero <= 28) ||
            (numero >= 31 && numero <= 38) ||
            (numero >= 41 && numero <= 48)) {
            return true;
        }

        // Temporales (niños)
        if ((numero >= 51 && numero <= 55) ||
            (numero >= 61 && numero <= 65) ||
            (numero >= 71 && numero <= 75) ||
            (numero >= 81 && numero <= 85)) {
            return true;
        }

        return false;
    }

    // 🔥 SUPERFICIES
    private static List<String> detectarSuperficies(String texto) {

        List<String> superficies = new ArrayList<>();

        if (texto.contains("oclusal"))    superficies.add("oclusal");
        if (texto.contains("mesial"))     superficies.add("mesial");
        if (texto.contains("distal"))     superficies.add("distal");
        if (texto.contains("vestibular")) superficies.add("vestibular");
        if (texto.contains("palatino"))   superficies.add("palatino");
        if (texto.contains("lingual"))    superficies.add("lingual");   // ✅ agregado
        if (texto.contains("incisal"))    superficies.add("incisal");   // ✅ agregado

        return superficies;
    }

    // 🧠 PATOLOGÍAS
    private static List<String> detectarPatologias(String texto) {

        List<String> patologias = new ArrayList<>();

        if (texto.contains("caries"))      patologias.add("caries");
        if (texto.contains("fractura"))    patologias.add("fractura");
        if (texto.contains("resina"))      patologias.add("resina");
        if (texto.contains("endodoncia"))  patologias.add("endodoncia");
        if (texto.contains("corona"))      patologias.add("corona");
        if (texto.contains("implante"))    patologias.add("implante");
        if (texto.contains("extraccion") ||
            texto.contains("extracción"))  patologias.add("extraccion"); // ✅ agregado
        if (texto.contains("sellante"))    patologias.add("sellante");   // ✅ agregado
        if (texto.contains("protesis") ||
            texto.contains("prótesis"))    patologias.add("protesis");   // ✅ agregado

        return patologias;
    }

    // 🦷 ESTADO
    private static String detectarEstado(String texto) {

        if (texto.contains("ausente"))                                return "ausente";
        if (texto.contains("extraida") || texto.contains("extraída")) return "extraida";
        if (texto.contains("implantada"))                             return "implantada";
        if (texto.contains("provisional"))                            return "provisional";

        return "normal";
    }

    // 🕐 PREVIO O ACTUAL — rojo = previo, azul = actual
    private static boolean detectarSiEsPrevio(String texto) {   // ✅ agregado

        return texto.contains("anterior")             ||
               texto.contains("previo")               ||
               texto.contains("previa")               ||
               texto.contains("ya tenia")             ||
               texto.contains("tenía")                ||
               texto.contains("restauracion anterior")||
               texto.contains("restauración anterior");
    }

    // 🔧 CONVERTIR LISTA A JSON
    private static String convertirLista(List<?> lista) {

        StringBuilder sb = new StringBuilder();
        sb.append("[");

        for (int i = 0; i < lista.size(); i++) {
            Object item = lista.get(i);
            if (item instanceof String) {
                sb.append("\"").append(item).append("\"");
            } else {
                sb.append(item);
            }
            if (i < lista.size() - 1) sb.append(",");
        }

        sb.append("]");
        return sb.toString();
    }
}
