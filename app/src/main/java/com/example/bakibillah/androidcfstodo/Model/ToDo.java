package com.example.bakibillah.androidcfstodo.Model;

/**
 * Created by BakiBillah on 10/10/2017.
 */

public class ToDo {

    private  String id;
    private String tile;
    private String description;

    public ToDo() {
    }

    public ToDo(String id, String tile, String description) {
        this.id = id;
        this.tile = tile;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
