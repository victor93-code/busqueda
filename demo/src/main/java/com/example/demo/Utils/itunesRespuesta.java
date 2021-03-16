/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.Utils;

import java.util.List;

/**
 *
 * @author VictorReyes
 */
public class itunesRespuesta {
    Integer resultCount;
    List<itunesValores> results;

    public Integer getResultCount() {
        return resultCount;
    }

    public List<itunesValores> getResults() {
        return results;
    }

    public void setResultCount(Integer resultCount) {
        this.resultCount = resultCount;
    }

    public void setResults(List<itunesValores> results) {
        this.results = results;
    }
    
}
