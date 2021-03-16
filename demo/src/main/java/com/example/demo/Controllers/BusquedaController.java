/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.Controllers;



import com.example.demo.Services.BusquedaService;
import com.example.demo.Utils.respuesta;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author VictorReyes
 */
@RestController
public class BusquedaController {
    
       @Autowired
        BusquedaService busquedaservice;
    
	//@RequestMapping("/busqueda",method = RequestMethod.GET)
       @RequestMapping(value = "/busqueda", method = RequestMethod.GET)
	public List<respuesta> index(@RequestParam(required = false) String nombre, 
                @RequestParam(required = false) String pais, @RequestParam(required = false) String categoria,
                @RequestParam(required = false) Integer id)
        {
            
            return busquedaservice.buscar(nombre,pais, categoria, id);
	}
}
