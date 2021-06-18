package models;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

public class House {
    byte[] imagebytes;
    String title;
    String features;
    int price;
    int category;

    Agent agent;

    public Image getImage() {

        Image image = new Image(new ByteArrayInputStream(imagebytes));
        return image;
    }

    public void setImage(File imageFile) {

        try {
            this.imagebytes = Files.readAllBytes(imageFile.toPath());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
