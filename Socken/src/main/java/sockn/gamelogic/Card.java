package sockn.gamelogic;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import sockn.enums.CardColor;
import sockn.enums.CardSymbol;

import static sockn.resources.config.Config.RESOURCES_IMAGES;

public class Card {
    private CardColor color;
    private CardSymbol symbol;
    private Image image;
    private ImageView imageView = new ImageView();

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

    Image getImage() {
        return this.image;
    }

    ImageView getImageView() {
        return this.imageView;
    }

    void setImage() {
        String url = getClass().getResource(RESOURCES_IMAGES + generatePictureUrl()).toExternalForm();
        this.image = new Image(url);
        this.imageView.setImage(this.image);
    }

    private String generatePictureUrl() {
        String capitalizedColor = (color.toString()).substring(0, 1).toUpperCase() + (color.toString()).toLowerCase().substring(1);
        String capitalizedSymbol= (symbol.toString()).substring(0, 1).toUpperCase() + (symbol.toString()).toLowerCase().substring(1);
        return capitalizedColor + "_" + capitalizedSymbol + ".gif";
    }
}
