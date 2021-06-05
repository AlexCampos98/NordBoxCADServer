/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nordboxcad;

import java.io.Serializable;


public class ListEventoApuntados implements Serializable {
    Integer idEvento, idEntrendaor;
    String nombreEvento;
    Integer numeroApuntados, deportista1, deportista2, deportista3, deportista4, deportista5,
            deportista6, deportista7, deportista8;
    String btnOpcion, color;

    public ListEventoApuntados() {
    }

    public ListEventoApuntados(Integer idEvento, Integer idEntrendaor, String nombreEvento, Integer numeroApuntados, Integer deportista1, Integer deportista2, Integer deportista3, Integer deportista4, Integer deportista5, Integer deportista6, Integer deportista7, Integer deportista8, String btnOpcion, String color) {
        this.idEvento = idEvento;
        this.idEntrendaor = idEntrendaor;
        this.nombreEvento = nombreEvento;
        this.numeroApuntados = numeroApuntados;
        this.deportista1 = deportista1;
        this.deportista2 = deportista2;
        this.deportista3 = deportista3;
        this.deportista4 = deportista4;
        this.deportista5 = deportista5;
        this.deportista6 = deportista6;
        this.deportista7 = deportista7;
        this.deportista8 = deportista8;
        this.btnOpcion = btnOpcion;
        this.color = color;
    }

    public Integer getIdEvento()
    {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento)
    {
        this.idEvento = idEvento;
    }

    public Integer getIdEntrendaor() {
        return idEntrendaor;
    }

    public void setIdEntrendaor(Integer idEntrendaor) {
        this.idEntrendaor = idEntrendaor;
    }

    public String getNombreEvento() {
        return nombreEvento;
    }

    public void setNombreEvento(String nombreEvento) {
        this.nombreEvento = nombreEvento;
    }

    public Integer getNumeroApuntados() {
        return numeroApuntados;
    }

    public void setNumeroApuntados(Integer numeroApuntados) {
        this.numeroApuntados = numeroApuntados;
    }

    public Integer getDeportista1() {
        return deportista1;
    }

    public void setDeportista1(Integer deportista1) {
        this.deportista1 = deportista1;
    }

    public Integer getDeportista2() {
        return deportista2;
    }

    public void setDeportista2(Integer deportista2) {
        this.deportista2 = deportista2;
    }

    public Integer getDeportista3() {
        return deportista3;
    }

    public void setDeportista3(Integer deportista3) {
        this.deportista3 = deportista3;
    }

    public Integer getDeportista4() {
        return deportista4;
    }

    public void setDeportista4(Integer deportista4) {
        this.deportista4 = deportista4;
    }

    public Integer getDeportista5() {
        return deportista5;
    }

    public void setDeportista5(Integer deportista5) {
        this.deportista5 = deportista5;
    }

    public Integer getDeportista6() {
        return deportista6;
    }

    public void setDeportista6(Integer deportista6) {
        this.deportista6 = deportista6;
    }

    public Integer getDeportista7() {
        return deportista7;
    }

    public void setDeportista7(Integer deportista7) {
        this.deportista7 = deportista7;
    }

    public Integer getDeportista8() {
        return deportista8;
    }

    public void setDeportista8(Integer deportista8) {
        this.deportista8 = deportista8;
    }

    public String getBtnOpcion() {
        return btnOpcion;
    }

    public void setBtnOpcion(String btnOpcion) {
        this.btnOpcion = btnOpcion;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString()
    {
        return "ListEventoApuntados{" + "idEvento=" + idEvento + ", idEntrendaor=" + idEntrendaor + ", nombreEvento=" + nombreEvento + ", numeroApuntados=" + numeroApuntados + ", deportista1=" + deportista1 + ", deportista2=" + deportista2 + ", deportista3=" + deportista3 + ", deportista4=" + deportista4 + ", deportista5=" + deportista5 + ", deportista6=" + deportista6 + ", deportista7=" + deportista7 + ", deportista8=" + deportista8 + ", btnOpcion=" + btnOpcion + ", color=" + color + '}';
    }
}
