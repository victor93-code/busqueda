/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.Services;

import com.example.demo.Utils.respuesta;
import java.util.List;

/**
 *
 * @author VictorReyes
 */
public interface BusquedaService {
    public abstract List<respuesta> buscar(String nombre, String pais, String categoria, Integer id);
    public abstract List<respuesta> llamadaSoap(Integer id);
    public abstract List<respuesta> llamadaItunes(String nombre, String pais, String categoria);
    public abstract List<respuesta> llamadaTvmaze(String nombre, String pais, String categoria);
}
