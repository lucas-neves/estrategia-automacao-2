package br.com.estrategiaconcursos.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author lucasns
 * @since #1.0
 */
public class Make {
    @JsonProperty("ID")
    private int id;
    @JsonProperty("Name")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
