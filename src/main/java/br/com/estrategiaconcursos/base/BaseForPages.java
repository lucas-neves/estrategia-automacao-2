package br.com.estrategiaconcursos.base;

import br.com.estrategiaconcursos.util.Data;

/**
 * @author lucasns
 * @since #1.0
 */
public class BaseForPages extends Page {
    // ------------------------------ FIELDS ------------------------------
    // -------------------------- OTHER METHODS --------------------------
    protected BaseForPages() {
        Data.getResourceProperties("application.properties");
    }

    // -------------------------- END OF OTHER METHODS --------------------------
}