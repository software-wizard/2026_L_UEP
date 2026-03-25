package pl.psi;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Objects;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Point2D implements PropertyChangeListener{
    @Setter
    private int x;
    private int y;
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    void addObserver(PropertyChangeListener aP1) {
        pcs.addPropertyChangeListener("setY", aP1);
    }

    void setY(int y) {
        this.y = y;
        pcs.firePropertyChange("setY", null, y);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (Objects.equals(evt.getPropertyName(), "setY")) {
            this.y = (int) evt.getNewValue();
        }
    }
}
