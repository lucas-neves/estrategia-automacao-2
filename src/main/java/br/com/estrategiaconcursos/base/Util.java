package br.com.estrategiaconcursos.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @author lucasns
 * @since #1.0
 */
public class Util {
    // ------------------------------ FIELDS ------------------------------
    private static final Logger log = LoggerFactory.getLogger(Util.class.getSimpleName());

    // -------------------------- OTHER METHODS --------------------------

    /**
     * Pega o Caminho inicial do projeto.
     *
     * @param path Recebe o parametro de onde está o arquivo (\src\main\resources\config\pasta\arquivo.xml).
     * @return Caminho completo (c:\projeto\pasta\src\main\resources\config\pasta\arquivo.xml).
     */
    public static String pegarResourcePath(String path) {
        log.debug("Pegando o caminho Resource");

        log.debug(path);

        String sistemaOperacional = Util.pegarSistemaOperacional();
        String caminho = "";

        try {
            File file = new File(path);
            caminho = file.getCanonicalPath();

            if (sistemaOperacional.contains("Linux"))
                caminho = caminho.replaceAll("\\\\", "/");

        } catch (Exception e) {
            log.error("Não foi possivel pegar o path... " + e);
        }
        return caminho;
    }

    public static String pegarSistemaOperacional() {
        return System.getProperty("os.name");
    }

    public static String lerArquivo(String fileName){
        File file = new File(Util.class.getClassLoader().getResource(fileName).getFile());

        try {
            return org.apache.commons.io.FileUtils.readFileToString(file, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}