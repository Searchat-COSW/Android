package cosw.eci.edu.android.entities;

public class Lenguage {
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
}
