package cosw.eci.edu.android.data.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class Event implements Serializable{
    private int id;
    private String name;
    private String description;
    private User administrator;
    private List<Lenguage> lenguages;
    private String location;
    private String date;
    private List<User> participants;
    private Long price;
    private double longitude;
    private double latitude;

    private String image;



    public Event(String name, String description, User administrator, List<Lenguage> lenguages, String location, String date, List<User> participants, Long price, double longitude, double latitude, String image) {
        this.name = name;
        this.description = description;
        this.administrator = administrator;
        this.lenguages = lenguages;
        this.location = location;
        this.date = date;
        this.participants = participants;
        this.price = price;
        this.longitude = longitude;
        this.latitude = latitude;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getAdministrator() {
        return administrator;
    }

    public void setAdministrator(User administrator) {
        this.administrator = administrator;
    }

    public List<Lenguage> getLenguages() {
        return lenguages;
    }

    public void setLenguages(List<Lenguage> lenguages) {
        this.lenguages = lenguages;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", administrator=" + administrator +
                ", lenguages=" + lenguages +
                ", location='" + location + '\'' +
                ", date=" + date +
                ", participants=" + participants +
                ", price=" + price +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", image='" + image + '\'' +
                '}';
    }
}
