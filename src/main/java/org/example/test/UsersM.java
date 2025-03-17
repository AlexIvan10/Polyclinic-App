package org.example.test;

public class UsersM {
    private String numePacient;
    private String prenumePacient;
    private String serviciu;
    private String data;
    private String durata;
    private String pret;
    private String raportMedical;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UsersM(String numePacient, String prenumePacient, String serviciu, String data, String durata, String pret, String email, String raportMedical) {
        this.numePacient = numePacient;
        this.prenumePacient = prenumePacient;
        this.serviciu = serviciu;
        this.data = data;
        this.durata = durata;
        this.pret = pret;
        this.email = email;
        this.raportMedical = raportMedical;
    }
    public String getRaportMedical() {
        return raportMedical;
    }

    public void setRaportMedical(String raportMedical) {
        this.raportMedical = raportMedical;
    }
    public String getNumePacient() {
        return numePacient;
    }

    public void setNumePacient(String numePacient) {
        this.numePacient = numePacient;
    }

    public String getPrenumePacient() {
        return prenumePacient;
    }

    public void setPrenumePacient(String prenumePacient) {
        this.prenumePacient = prenumePacient;
    }

    public String getServiciu() {
        return serviciu;
    }

    public void setServiciu(String serviciu) {
        this.serviciu = serviciu;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDurata() {
        return durata;
    }

    public void setDurata(String durata) {
        this.durata = durata;
    }

    public String getPret() {
        return pret;
    }

    public void setPret(String pret) {
        this.pret = pret;
    }
}
