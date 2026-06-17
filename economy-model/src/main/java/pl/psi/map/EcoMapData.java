package pl.psi.map;

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
public class EcoMapData {
    private int width = 18;
    private int height = 9;
    private Map<String, EcoMapObjectDto> objects = new HashMap<>(); // key: "x,y"
}
