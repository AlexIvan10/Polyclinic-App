package org.example.test;

public class UsersA {
    private String medic;
    private String dataProgramare;
    private String oraProgramare;

    public UsersA(String medic, String dataProgramare, String oraProgramare) {
        this.medic = medic;
        this.dataProgramare = dataProgramare;
        this.oraProgramare = oraProgramare;
    }

    public String getMedic() {
        return medic;
    }

    public void setMedic(String medic) {
        this.medic = medic;
    }

    public String getDataProgramare() {
        return dataProgramare;
    }

    public void setDataProgramare(String dataProgramare) {
        this.dataProgramare = dataProgramare;
    }

    public String getOraProgramare() {
        return oraProgramare;
    }

    public void setOraProgramare(String oraProgramare) {
        this.oraProgramare = oraProgramare;
    }
}
