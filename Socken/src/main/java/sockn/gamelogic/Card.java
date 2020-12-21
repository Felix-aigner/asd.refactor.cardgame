package sockn.gamelogic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sockn.enums.CardColor;
import sockn.enums.CardSymbol;

import static sockn.resources.config.Config.RESOURCES_IMAGES;

public class Card {
    private final CardColor color;
    private final CardSymbol symbol;
    private final ImageView imageView = new ImageView();

    public Card(CardColor color, CardSymbol symbol) {
        this.color = color;
        this.symbol = symbol;
        setImage();
    }

    CardColor getColor() {
        return this.color;
    }

    CardSymbol getSymbol() {
        return this.symbol;
    }

    ImageView getImageView() {
        return this.imageView;
    }

    void setImage() {
        String url = getClass().getResource(RESOURCES_IMAGES + generatePictureUrl()).toExternalForm();
        Image image = new Image(url);
        this.imageView.setImage(image);
    }

    private String generatePictureUrl() {
        String capitalizedColor = (color.toString()).substring(0, 1).toUpperCase() + (color.toString()).toLowerCase().substring(1);
        String capitalizedSymbol= (symbol.toString()).substring(0, 1).toUpperCase() + (symbol.toString()).toLowerCase().substring(1);
        return capitalizedColor + "_" + capitalizedSymbol + ".gif";
    }
}
