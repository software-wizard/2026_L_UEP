package pl.psi.gui;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class MapTile extends StackPane
{

    private final Rectangle rect;
    private final Rectangle highlightRect;
    private final Label label;
    private Color originalColor = Color.WHITE;

    MapTile( final String aName )
    {
        rect = new Rectangle( 50, 50 );
        rect.setFill( originalColor );
        rect.setStroke( Color.RED );

        highlightRect = new Rectangle( 50, 50 );
        highlightRect.setFill( Color.TRANSPARENT );
        highlightRect.setMouseTransparent( true ); // Allow clicks to pass through

        getChildren().addAll( rect, highlightRect );
        
        label = new Label( aName );
        getChildren().add( label );
    }

    public void setHighlightColor( Color aColor )
    {
        highlightRect.setFill( aColor );
    }

    public void clearHighlight()
    {
        highlightRect.setFill( Color.TRANSPARENT );
    }

    public void setName( final String aName )
    {
        label.setText( aName );
    }

    public void setBackground( final Color aColor )
    {
        originalColor = aColor;
        rect.setFill( aColor );
    }

    public void setHoverBackground( final Color aColor )
    {
        rect.setFill( aColor );
    }

    public void clearHoverBackground()
    {
        rect.setFill( originalColor );
    }
}
