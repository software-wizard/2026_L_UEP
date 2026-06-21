package pl.psi.gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class EconomyTile extends StackPane
{

    private final Rectangle rect;
    private final Label label;

    EconomyTile( final String aName )
    {
        rect = new Rectangle( 60, 60 );
        rect.setFill( Color.WHITE );
        rect.setStroke( Color.RED );
        getChildren().add( rect );
        label = new Label( aName );
        getChildren().add( label );
    }

    public void setImage(String imagePath) {
        try {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(59);
            imageView.setFitHeight(59);
            imageView.setMouseTransparent(true);
            getChildren().add(imageView);
            label.toFront();
        } catch (Exception e) {
            setName("Obj");
        }
    }

    void setHero(boolean currentHero) {
        setImage("/heroes/hero1.png");
        label.setText(currentHero ? "Hero" : "Enemy");
        label.setTextFill(currentHero ? Color.LIMEGREEN : Color.RED);
        label.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.65);");
        label.toFront();
    }

    void setName( final String aName )
    {
        label.setText( aName );
    }

    void setBackground( final Color aColor )
    {
        rect.setFill( aColor );
    }
}
