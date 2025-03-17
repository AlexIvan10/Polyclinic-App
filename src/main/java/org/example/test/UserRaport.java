package org.example.test;

public class UserRaport {
    private static int pacientID;

    public static int getID() {
        return pacientID;
    }

    public static void setID(int pacientID) {
        UserRaport.pacientID = pacientID;
    }
}
