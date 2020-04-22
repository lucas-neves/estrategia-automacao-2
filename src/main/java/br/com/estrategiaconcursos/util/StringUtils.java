package br.com.estrategiaconcursos.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author lucasns
 * @since #1.0
 */
public class StringUtils {

    /**
     * Retira espaco.
     *
     * @param texto Texto que ira ser tratado.
     * @return Conteudo tratado
     */
    @Contract(pure = true)
    @NotNull
    private static String trim(String texto) {
        return texto.trim();
    }

    /**
     * Texto fica em letras minusculas.
     *
     * @param texto Texto que ira ser tratado.
     * @return Conteudo tratado
     */
    @NotNull
    private static String lowerCase(String texto) {
        return texto.toLowerCase();
    }

    /**
     * Texto fica com letras maiusculas.
     *
     * @param texto Texto que ira ser tratado.
     * @return Conteudo tratado
     */
    @NotNull
    private static String upperCase(String texto) {
        return texto.toUpperCase();
    }

    /**
     * Retira tipos de espacos diferentes.
     *
     * @param texto Texto que ira ser tratado.
     * @return Conteudo tratado
     */
    public static String espaco(String texto) {
        texto = texto.replaceAll(" ", ""); //são caracteres diferentes
        texto = texto.replaceAll(" ", ""); //são caracteres diferentes
        return texto;
    }

    /**
     * Retira caracteres especiais.
     *
     * @param texto Texto que ira ser tratado.
     * @return Conteudo tratado
     */
    @NotNull
    public static String caracteresEspeciais(String texto) {
        texto = texto.replaceAll("[ÂÀÁÄÃ]", "A");
        texto = texto.replaceAll("[âãàáä]", "a");
        texto = texto.replaceAll("[ÊÈÉË]", "E");
        texto = texto.replaceAll("[êèéë]", "e");
        texto = texto.replaceAll("ÎÍÌÏ", "I");
        texto = texto.replaceAll("îíìï", "i");
        texto = texto.replaceAll("[ÔÕÒÓÖ]", "O");
        texto = texto.replaceAll("[ôõòóö]", "o");
        texto = texto.replaceAll("[ÛÙÚÜ]", "U");
        texto = texto.replaceAll("[ûúùü]", "u");
        texto = texto.replaceAll("Ç", "C");
        texto = texto.replaceAll("ç", "c");
        texto = texto.replaceAll("[ýÿ]", "y");
        texto = texto.replaceAll("Ý", "Y");
        texto = texto.replaceAll("ñ", "n");
        texto = texto.replaceAll("Ñ", "N");
        texto = texto.replaceAll(":", "");
        texto = texto.replaceAll("-", "");
        texto = texto.replaceAll("[^\\p{ASCII}]", "");
        //texto = texto.replaceAll("[-+=*&amp;%$#@!_]", "");
        texto = texto.replaceAll("['\"]", "");
        texto = texto.replaceAll("[<>()\\{\\}]", "");
        texto = texto.replaceAll("['\\\\.,()|/]", "");
        //texto = texto.replaceAll("[^!-ÿ]{1}[^ -ÿ]{0,}[^!-ÿ]{1}|[^!-ÿ]{1}", " ");
        return texto.trim();
    }

    public static String email(String message) {
        message = message.replaceAll("\\[", "");
        message = message.replaceAll("]", "");
        message = message.replaceAll(",", "");
        return message;
    }

    /**
     * Retira numeros.
     *
     * @param texto Texto que ira ser tratado.
     * @return Conteudo tratado
     */
    private static String numeral(String texto) {
        texto = texto.replaceAll("1", "");
        texto = texto.replaceAll("2", "");
        texto = texto.replaceAll("3", "");
        texto = texto.replaceAll("4", "");
        texto = texto.replaceAll("5", "");
        texto = texto.replaceAll("6", "");
        texto = texto.replaceAll("7", "");
        texto = texto.replaceAll("8", "");
        texto = texto.replaceAll("9", "");
        texto = texto.replaceAll("0", "");
        return texto;
    }

    @NotNull
    public static String tudoL(String texto) {
        texto = replaceAll(texto);
        return lowerCase(texto);
    }

    @NotNull
    public static String tudoU(String texto) {
        texto = replaceAll(texto);
        return upperCase(texto);
    }

    private static String replaceAll(String texto) {
        texto = trim(texto);
        texto = espaco(texto);
        texto = caracteresEspeciais(texto);
        texto = numeral(texto);
        return texto;
    }

    public static String randomAlphaNumeric(int count) {
        String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    /**
     * Retorna o string em uma das cores conforme a cor escolhida
     * *Opcional: Cores.COR.clarear() para clarear a cor desejada
     * <p>
     *
     * @param text     Texto a ser colorido
     * @param cor      Selecionar dentro de Cores a cor desejada
     * @param fundo    Selecionar dentro de Fundo a cor do fundo desejado
     * @param formato  Selecionar dentro de Formatacao o Tipo de fonte desejada
     */

    @Contract(pure = true)
    public static String colored(String text, Cores cor, Fundo fundo, Formatacao formato) {
        String colored;

        if (cor == null)
            cor = Cores.PRETO;

        if (formato != null && fundo != null)
            colored = "\033[" + formato.value + ";" + cor.value + ";" + fundo.value + "m" + text + "\033[0m";

        else if (fundo != null)
            colored = "\033[" + cor.value + ";" + fundo.value + "m" + text + "\033[0m";

        else if (formato != null)
            colored = "\033[" + formato.value + ";" + cor.value + "m" + text + "\033[0m";

        else colored = "\033[" + cor.value + "m" + text + "\033[0m";

        return colored;
    }

    public enum Cores {
        PRETO("0;30"),
        VERMELHO("0;31"),
        VERDE("0;32"),
        MARROM("0;33"),
        AZUL("0;34"),
        ROXO("0;35"),
        CIANO("0;36"),
        CINZACLARO("0;37");

        public String value;

        Cores(String i){
            this.value = i;
        }

        public Cores clarear(){
            this.value = value.replace("0", "1");
            return this;
        }
    }

    public enum Formatacao {
        NEGRITO(1),
        ITALICO(3),
        SUBLINHADO(4),
        PISCANDO(5),
        INVERTIDO(7),
        OCULTO(8),
        RISCADO(9);

        public int value;

        Formatacao(int i){
            this.value = i;
        }
    }

    public enum Fundo {
        PRETO(40),
        VERMELHO(41),
        VERDE(42),
        MARROM(43),
        AZUL(44),
        ROXO(45),
        CIANO(46),
        CINZACLARO(47);

        public int value;

        Fundo(int i){
            this.value = i;
        }
    }
}
