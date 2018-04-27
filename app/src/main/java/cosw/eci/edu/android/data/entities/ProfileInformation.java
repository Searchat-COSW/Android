package cosw.eci.edu.android.data.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProfileInformation implements Serializable{

    private String username;


    private String nationality;
    private List<Lenguage> languages = new ArrayList<>();
    private String aboutYou;
    private String image;

    public ProfileInformation(String username, String nationality, List<Lenguage> languages, String aboutYou, String image) {
        this.username = username;
        this.nationality = nationality;
        this.languages = languages;
        this.aboutYou = aboutYou;
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public List<Lenguage> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Lenguage> languages) {
        this.languages = languages;
    }

    public String getAboutYou() {
        return aboutYou;
    }

    public void setAboutYou(String aboutYou) {
        this.aboutYou = aboutYou;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ProfileInformation{" +
                "username='" + username + '\'' +
                ", nationality='" + nationality + '\'' +
                ", languages=" + languages +
                ", aboutYou='" + aboutYou + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
