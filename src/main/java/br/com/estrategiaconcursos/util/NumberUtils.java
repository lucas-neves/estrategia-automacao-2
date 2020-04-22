package br.com.estrategiaconcursos.util;

import br.com.estrategiaconcursos.base.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lucasns
 * @since #1.0
 */
public class NumberUtils {
    // ------------------------------ FIELDS ------------------------------
    private static final Logger log = LoggerFactory.getLogger(Util.class.getSimpleName());

    // -------------------------- OTHER METHODS --------------------------

    /**
     * Gera numero aleatorio.
     *
     * @param numeroMinimo Numero minimo para gerar aleatoriamente.
     * @param numeroMaximo Numero maximo para gerar aleatoriamente.
     * @return Retorna um numero aleatorio entre o maximo e o minimo.
     */
    public static Integer gerarNumeroAleatorio(Integer numeroMinimo, Integer numeroMaximo) {
        if (numeroMinimo >= numeroMaximo)
            throw new IllegalArgumentException("o numero maximo precisa ser maior que o minimo");

        Random r = new Random();
        return r.nextInt((numeroMaximo - numeroMinimo) + 1) + numeroMinimo;
    }

    public static List<Double> extrairNumeros(String texto) {
        List<Double> lista = new ArrayList<>();
        Pattern regex = Pattern.compile("(\\d+(?:\\.\\d+)?)");
        Matcher matcher = regex.matcher(texto);
        while(matcher.find()){
            lista.add(Double.valueOf(matcher.group(1)));
        }
        return lista;
    }
}