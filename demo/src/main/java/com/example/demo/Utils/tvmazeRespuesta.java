/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.Utils;

/**
 *
 * @author VictorReyes
 */
public class tvmazeRespuesta {
    Double score;
    tvmazeValores show;
    tvmazePersona person;

    public void setPerson(tvmazePersona person) {
        this.person = person;
    }

    public tvmazePersona getPerson() {
        return person;
    }

    public Double getScore() {
        return score;
    }

    public tvmazeValores getShow() {
        return show;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public void setShow(tvmazeValores show) {
        this.show = show;
    }
    
    
}
