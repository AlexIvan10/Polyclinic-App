package org.example.test;

public class UsersDonatori {
    private String numeDonator;
    private String prenumeDonator;
    private String email;
    private String grupa;
    private String RH;
    private String dataColectarii;

    public UsersDonatori(String numeDonator, String prenumeDonator, String email, String grupa, String RH, String dataColectarii) {
        this.numeDonator = numeDonator;
        this.prenumeDonator = prenumeDonator;
        this.email = email;
        this.grupa = grupa;
        this.RH = RH;
        this.dataColectarii = dataColectarii;
    }

    public String getNumeDonator() {
        return numeDonator;
    }

    public void setNumeDonator(String numeDonator) {
        this.numeDonator = numeDonator;
    }

    public String getPrenumeDonator() {
        return prenumeDonator;
    }

    public void setPrenumeDonator(String prenumeDonator) {
        this.prenumeDonator = prenumeDonator;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public String getRH() {
        return RH;
    }

    public void setRH(String RH) {
        this.RH = RH;
    }

    public String getDataColectarii() {
        return dataColectarii;
    }

    public void setDataColectarii(String dataColectarii) {
        this.dataColectarii = dataColectarii;
    }
}
