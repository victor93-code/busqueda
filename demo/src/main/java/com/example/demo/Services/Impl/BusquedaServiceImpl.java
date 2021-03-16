/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.Services.Impl;


import com.example.demo.Services.BusquedaService;
import com.example.demo.Utils.itunesRespuesta;
import com.example.demo.Utils.itunesValores;
import com.example.demo.Utils.respuesta;
import com.example.demo.Utils.tvmazeRespuesta;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import static com.sun.deploy.uitoolkit.impl.fx.ui.resources.ResourceManager.getString;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.springframework.stereotype.Service;
import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.Document;
/**
 *
 * @author VictorReyes
 */
@Service
public class BusquedaServiceImpl implements BusquedaService {
    public List<respuesta> buscar(String nombre, String pais, String categoria, Integer id) {
        
        if (id == null){
            List<respuesta> rsp = llamadaItunes(nombre, pais, categoria);
            List<respuesta> rs = llamadaTvmaze(nombre, pais, categoria);
            List<respuesta> listaNueva = Stream.concat(rsp.stream(), rs.stream())
                             .collect(Collectors.toList());
            return listaNueva;
        }else{
            return llamadaSoap(id);
        }
        
    }

    @Override
    public List<respuesta> llamadaSoap(Integer id) {
        List<respuesta> rsp = new ArrayList();
        
        try {
            String url = "http://www.crcind.com/csp/samples/SOAP.Demo.cls?soap_method=FindPerson&id="+id;
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.flush();
            wr.close(
            );
            BufferedReader in = new BufferedReader(new InputStreamReader(
            con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
            DocumentBuilder builder = null;
            try
            {
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(response.toString())));
                
                respuesta r = new respuesta();
                r.setNombre(doc.getElementsByTagName("Name").item(0).getTextContent());
                r.setTipo("person");
                r.setOrigen("soap");
                r.setMensajeOriginal(response.toString());
                rsp.add(r);
                
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
                System.out.println("response:" + response.toString());

            } catch (Exception e) {
                System.out.println("Error al consultar el servicio soap: "+e);
            }
        return rsp;
    }

    @Override
    public List<respuesta> llamadaItunes(String nombre, String pais, String categoria) {
        List<respuesta> respuesta = new ArrayList();
        String api  = "https://itunes.apple.com/search";
        
        if (nombre != null && pais == null && categoria == null){
            api = api+"?term="+nombre+""; 
        }else if (pais != null && nombre == null && categoria == null){
            api = api+"?country="+pais+"";
        }else if (categoria != null && nombre == null && pais == null){
            api = api+"?term="+categoria+"";
        }else {
            api = api+"?term="+categoria+"&country="+pais+"";
        }
        
        try {
            URL url = new URL(api);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            itunesRespuesta apod = mapper.readValue(responseStream, itunesRespuesta.class);
            
            for (itunesValores valores : apod.getResults()) 
            { 
                respuesta r = new respuesta();
                r.setNombre(valores.getArtistName());
                r.setOrigen("ITunes");
                r.setTipo(valores.getKind());
                r.setMensajeOriginal(mapper.writeValueAsString(valores));
                respuesta.add(r);
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(BusquedaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BusquedaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }

    @Override
    public List<respuesta> llamadaTvmaze(String nombre, String pais, String categoria) {
        List<respuesta> respuesta = new ArrayList();
        String api  = "http://api.tvmaze.com/";
        
        if (nombre != null && pais == null && categoria == null){
            api = api+"search/people?q="+nombre+""; 
        }else if (pais != null && nombre == null && categoria == null){
            api = api+"schedule?country="+pais+"";
        }else if (categoria != null && nombre == null && pais == null){
            api = api+"search/shows?q="+categoria+"";
        }
        
        
        try {
            URL url = new URL(api);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            InputStream responseStream = connection.getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<tvmazeRespuesta> apod = new ArrayList();
            tvmazeRespuesta[] myObjects = mapper.readValue(responseStream, tvmazeRespuesta[].class);
            
            for (tvmazeRespuesta valores : myObjects) 
            { 
                if (nombre != null){
                    respuesta r = new respuesta();
                    r.setNombre(valores.getPerson().getName());
                    r.setOrigen("Tvmaze");
                    r.setTipo("person");
                    r.setMensajeOriginal(mapper.writeValueAsString(valores));   
                    respuesta.add(r);
                }else if (categoria != null){
                    respuesta r = new respuesta();
                    r.setNombre(valores.getShow().getName());
                    r.setOrigen("Tvmaze");
                    r.setTipo("show");
                    r.setMensajeOriginal(mapper.writeValueAsString(valores));   
                    respuesta.add(r);
                }else{
                    respuesta r = new respuesta();
                    r.setNombre(valores.getShow().getName());
                    r.setOrigen("Tvmaze");
                    r.setTipo("country");
                    r.setMensajeOriginal(mapper.writeValueAsString(valores));   
                    respuesta.add(r);
                }

            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(BusquedaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BusquedaServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return respuesta;
    }
}
