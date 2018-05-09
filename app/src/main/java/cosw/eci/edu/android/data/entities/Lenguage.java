package cosw.eci.edu.android.data.entities;

import java.io.Serializable;

public class Lenguage implements Serializable{
    private String lenguage;

    public Lenguage(String lenguage) {
        this.lenguage = lenguage;
    }

    public String getLenguage() {
        return lenguage;
    }

    public void setLenguage(String lenguage) {
        this.lenguage = lenguage;
    }

    @Override
    public String toString() {
        return "Lenguage{" +
                "lenguage='" + lenguage + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return this.lenguage.equals(((Lenguage) obj).getLenguage());
    }
}
