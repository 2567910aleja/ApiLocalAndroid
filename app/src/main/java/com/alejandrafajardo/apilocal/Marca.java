package com.alejandrafajardo.apilocal;

public class Marca {
    private int id;
    private String nombre;
    private String descripcion;

    public Marca() {
    }

    @Override
    public String toString(){
        return id +" "+nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
