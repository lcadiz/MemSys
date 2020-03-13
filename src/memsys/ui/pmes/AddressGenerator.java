/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package memsys.ui.pmes;

/**
 *
 * @author LESTER
 */
public class AddressGenerator {
    private String sitio;
    private String brgy;
    private String area;
    
    public String getAddress(String s, String b, String a){
        AddressGenerator ag = new AddressGenerator();
        ag.setSitio(s);
        ag.setBrgy(b);
        ag.setArea(a);
        return sitio +", "+ 
               brgy+", "+
               area+", NEGROS ORIENTAL";
    }

    public String getSitio() {
        return sitio;
    }

    public void setSitio(String sitio) {
        this.sitio = sitio;
    }

    public String getBrgy() {
        return brgy;
    }

    public void setBrgy(String brgy) {
        this.brgy = brgy;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    
}
