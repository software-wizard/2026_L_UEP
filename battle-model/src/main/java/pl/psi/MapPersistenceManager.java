package pl.psi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MapPersistenceManager {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void saveMap(MapData mapData, File file) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, mapData);
    }

    public MapData loadMap(File file) throws IOException {
        return objectMapper.readValue(file, MapData.class);
    }

    public BiMap<BattlePoint, SpecialField> convertToBiMap(MapData mapData) {
        BiMap<BattlePoint, SpecialField> biMap = HashBiMap.create();
        if (mapData == null || mapData.getSpecialFields() == null) {
            return biMap;
        }

        for (Map.Entry<String, String> entry : mapData.getSpecialFields().entrySet()) {
            String[] coords = entry.getKey().split(",");
            if (coords.length != 2) {
                continue;
            }
            try {
                int x = Integer.parseInt(coords[0].trim());
                int y = Integer.parseInt(coords[1].trim());
                SpecialField.FieldName fieldName = SpecialField.FieldName.valueOf(entry.getValue());
                SpecialField specialField = createField(fieldName);
                biMap.put(new BattlePoint(x, y), specialField);
            } catch (Exception e) {
                // Ignore parsing errors for robust load
                e.printStackTrace();
            }
        }
        return biMap;
    }

    public MapData convertToMapData(BiMap<BattlePoint, SpecialField> specialFields, int width, int height) {
        MapData mapData = new MapData();
        mapData.setWidth(width);
        mapData.setHeight(height);
        Map<String, String> mapFields = new HashMap<>();

        for (Map.Entry<BattlePoint, SpecialField> entry : specialFields.entrySet()) {
            BattlePoint point = entry.getKey();
            SpecialField field = entry.getValue();
            if (point != null && field != null && field.getFieldName() != null) {
                String key = point.getX() + "," + point.getY();
                mapFields.put(key, field.getFieldName().name());
            }
        }

        mapData.setSpecialFields(mapFields);
        return mapData;
    }

    public static SpecialField createField(SpecialField.FieldName fieldName) {
        switch (fieldName) {
            case DMG_FIELD:
                return new DmgField();
            case DEBUFF_FIELD:
                return new DebuffField();
            case BUFF_FIELD:
                return new BuffField();
            case FIELD_CAN_ONLY_BE_FLOWN:
                return new FieldCanOnlyBeFlown();
            case FIRE_FIELD:
                return new FireWall(20);
            case CRACKED_ICE:
                return new CrackedIceField();
            case QUICKSAND:
                return new QuicksandField();
            case FIELDS_OF_GLORY:
                return new FieldsOfGloryField();
            case MAGIC_PLAINS:
                return new MagicPlainsField();
            case SPELL_FIELD:
                return new SpellField();
            default:
                throw new IllegalArgumentException("Unknown field name: " + fieldName);
        }
    }
}
