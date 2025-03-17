package org.example.test;

public class UsersR {
    private String nume;
    private String prenume;
    private String ora;
    private String doctor;
    private String data;
    private String suma;

    public UsersR(String nume, String prenume, String doctor, String ora, String suma, String data) {
        this.nume = nume;
        this.prenume = prenume;
        this.doctor = doctor;
        this.ora = ora;
        this.suma = suma;
        this.data = data;
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

    public String getOra() {
        return ora;
    }

    public void setOra(String ora) {
        this.ora = ora;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSuma() {
        return suma;
    }

    public void setSuma(String suma) {
        this.suma = suma;
    }
}
