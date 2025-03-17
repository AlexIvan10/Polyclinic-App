package org.example.test;

public class UsersSA {
    private String nume;
    private String prenume;
    private String email;
    private String adresa;
    private String numarTelefon;
    private String CNP;
    private String IBAN;
    private String functie;
    private String policlinica;

    public UsersSA(String nume, String prenume, String email, String adresa, String numarTelefon, String CNP, String IBAN, String functie, String policlinica) {
        this.nume = nume;
        this.prenume = prenume;
        this.email = email;
        this.adresa = adresa;
        this.numarTelefon = numarTelefon;
        this.CNP = CNP;
        this.IBAN = IBAN;
        this.functie = functie;
        this.policlinica = policlinica;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getNumarTelefon() {
        return numarTelefon;
    }

    public void setNumarTelefon(String nrTel) {
        this.numarTelefon = nrTel;
    }

    public String getCNP() {
        return CNP;
    }

    public void setCNP(String CNP) {
        this.CNP = CNP;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public String getFunctie() {
        return functie;
    }

    public void setFunctie(String functie) {
        this.functie = functie;
    }

    public String getPoliclinica() {
        return policlinica;
    }

    public void setPoliclinica(String policlinica) {
        this.policlinica = policlinica;
    }
}
