package pl.psi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapData {
    private int width = 15;
    private int height = 11;
    private Map<String, String> specialFields = new HashMap<>(); // key: "x,y", value: FieldName (string)
}
