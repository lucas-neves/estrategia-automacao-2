package br.com.estrategiaconcursos.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author lucasns
 * @since #1.0
 */
public class Data {
    public static final Properties p = System.getProperties();
    // ------------------------------ FIELDS ------------------------------
    private final static Logger log = LoggerFactory.getLogger(Data.class.getSimpleName());
    private static final HashMap<String, String> data = new HashMap<>();
    // -------------------------- OTHER METHODS --------------------------

    /**
     * Grava as propriedades na target...
     */
    public static String getProperties() {
        StringBuilder propriedadesParaGravar = new StringBuilder("#-- listing properties --\n");
        Set set = data.entrySet();

        for (Object aSet : set) {
            Map.Entry entry = (Map.Entry) aSet;
            log.debug("Gravando... " + entry.getKey() + " = " + entry.getValue());
            String key = (String) entry.getKey();
            String value = data.get(key);
            if (!(key.contains("java.") || key.contains("user.") ||
                    key.contains("os.") || key.contains("path.") ||
                    key.contains("sun.") || key.contains("idea.") ||
                    key.contains("file.") || key.contains("line.") ||
                    key.contains("jnidispatch.") || key.contains("awt.") ||
                    key.contains("surefire.") || key.contains("webdriver.") ||
                    key.contains("wdm.") || key.contains("urlInicial") ||
                    key.contains("webdriver.") || key.contains("default.") ||
                    key.contains("password") || key.contains("url.") ||
                    key.contains("urlFinal")))

                propriedadesParaGravar.append(key).append(" = ").append(value).append("\n");
        }
        return propriedadesParaGravar.toString();
    }

    /**
     * Grava a propriedade que deseja (com log)
     *
     * @param nomeProperties  Nome para a propriedade
     * @param valorProperties Valor que deseja dar a propriedade
     */
    public static void set(String nomeProperties, String valorProperties) {
        set(nomeProperties, valorProperties, true);
    }

    /**
     * Grava a propriedade que deseja (sem log)
     *
     * @param nomeProperties  Nome para a propriedade
     * @param valorProperties Valor que deseja dar a propriedade
     */
    public static void set(String nomeProperties, String valorProperties, boolean writeLog) {
        remove(nomeProperties);
        data.put(nomeProperties, valorProperties);

        if (writeLog)
            log.info("Gravando... " + nomeProperties + " = " + valorProperties);
    }

    /**
     * Pega a propriedade desejada
     *
     * @param nomeProperties Nome da propriedade que deseja pegar
     * @return Retorna o valor dado a propriedade anteriormente
     */
    public static String get(String nomeProperties) {
        String valor = data.get(nomeProperties);

        if (valor == null) {
            log.debug("Tentando pegar do System Properties...");
            log.debug("Properties - " + nomeProperties + "...");
            valor = p.getProperty(nomeProperties);
        }

        log.debug("Pegando valor... " + nomeProperties + " = " + valor);
        return valor;
    }

    /**
     * Pega de um arquivo os valores predefinodos para gravar como propriedade
     *
     * @param file nome do arquivo para pegar as propriedades
     */
    public static void getResourceProperties(String file) {
        try {
            InputStream resource = Data.class.getClassLoader().getResourceAsStream(file);
            p.load(resource);
        } catch (Exception e) {
            log.warn("Falha ao tentar ler as propriedades...\n" + e);
        }
    }

    /**
     * Remove a propriedade com o valor da memoria
     *
     * @param nomeProperites nome da propriedade que deseja que seja removido
     */
    public static void remove(String nomeProperites) {
        data.remove(nomeProperites);
    }

    /**
     * Limpa completamente as propriedades
     */
    public static void clear() {
        data.clear();
    }
}