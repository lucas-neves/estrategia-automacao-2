package br.com.estrategiaconcursos.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author lucasns
 * @since #1.0
 */
public class Model {
    @JsonProperty("MakeID")
    private int makeId;
    @JsonProperty("ID")
    private int id;
    @JsonProperty("Name")
    private String name;

    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

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
