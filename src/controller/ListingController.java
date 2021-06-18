package controller;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.House;

public class ListingController {

    @FXML
    private ImageView imgView;

    @FXML
    private Label lblTitle;

    @FXML
    private Label lblCategory;

    @FXML
    private Label lblPrice;

    ListingListener listingListener;
    House house;

    @FXML
    public void onClickHandler(MouseEvent event) {
        listingListener.onClickListener(house);
    }

    public void setData(House house, ListingListener listingListener){

        this.listingListener = listingListener;
        this.house = house;

        lblTitle.setText(house.getTitle());
        lblCategory.setText(Integer.toString(house.getCategory()));
        lblPrice.setText(Integer.toString(house.getPrice()));

        try {

            Rectangle rectangle = new Rectangle(140, 140);
            rectangle.setArcHeight(80);
            rectangle.setArcWidth(80);

            imgView.setClip(rectangle);

            imgView.setImage(crop(house.getImage()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private Image crop(Image img) {
        double d = Math.min(img.getWidth(),img.getHeight());
        double x = (d-img.getWidth())/2;
        double y = (d-img.getHeight())/2;
        Canvas canvas = new Canvas(d, d);
        GraphicsContext g = canvas.getGraphicsContext2D();

        g.drawImage(img, x, y);
        return canvas.snapshot(null, null);
    }
}
