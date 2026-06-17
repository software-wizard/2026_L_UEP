package pl.psi.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.psi.economy.Point;
import pl.psi.hero.EconomyHero;
import pl.psi.hero.artifacts.Artifact;
import pl.psi.hero.artifacts.ArtifactType;
import pl.psi.hero.artifacts.EconomySpell;
import pl.psi.map.buildings.bank.Bank;
import pl.psi.map.buildings.bank.BankStatistics;
import pl.psi.map.buildings.town.Town;
import pl.psi.map.resources.Gold;
import pl.psi.map.resources.Resources;
import pl.psi.map.resources.generators.ResourceGenType;
import pl.psi.map.resources.generators.ResourceGenerator;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EcoMapPersistenceManager {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void saveMap(EcoMapData mapData, File file) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, mapData);
    }

    public EcoMapData loadMap(File file) throws IOException {
        return objectMapper.readValue(file, EcoMapData.class);
    }

    public Map<Point, MapObjectIf> convertToModelMap(EcoMapData mapData, EconomyHero hero1, EconomyHero hero2) {
        Map<Point, MapObjectIf> modelMap = new HashMap<>();
        if (mapData == null || mapData.getObjects() == null) {
            return modelMap;
        }

        for (Map.Entry<String, EcoMapObjectDto> entry : mapData.getObjects().entrySet()) {
            String[] coords = entry.getKey().split(",");
            if (coords.length != 2) {
                continue;
            }
            try {
                int x = Integer.parseInt(coords[0].trim());
                int y = Integer.parseInt(coords[1].trim());
                Point point = new Point(x, y);

                EcoMapObjectDto dto = entry.getValue();
                MapObjectIf modelObj = createModelObject(dto, hero1, hero2);
                if (modelObj != null) {
                    modelMap.put(point, modelObj);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return modelMap;
    }

    public EcoMapData convertToEcoMapData(Map<Point, MapObjectIf> modelMap, int width, int height) {
        EcoMapData mapData = new EcoMapData();
        mapData.setWidth(width);
        mapData.setHeight(height);
        Map<String, EcoMapObjectDto> objects = new HashMap<>();

        for (Map.Entry<Point, MapObjectIf> entry : modelMap.entrySet()) {
            Point point = entry.getKey();
            MapObjectIf obj = entry.getValue();
            if (point != null && obj != null) {
                String key = point.getX() + "," + point.getY();
                EcoMapObjectDto dto = createDtoFromModel(obj);
                if (dto != null) {
                    objects.put(key, dto);
                }
            }
        }

        mapData.setObjects(objects);
        return mapData;
    }

    private MapObjectIf createModelObject(EcoMapObjectDto dto, EconomyHero hero1, EconomyHero hero2) {
        String type = dto.getType();
        String param = dto.getParam();

        switch (type) {
            case "TOWN":
                if ("PLAYER2".equalsIgnoreCase(param)) {
                    return new Town(hero2);
                } else {
                    return new Town(hero1);
                }
            case "RESOURCE_GENERATOR":
                return new ResourceGenerator(ResourceGenType.valueOf(param));
            case "GOLD":
                int goldAmount = 1000;
                try {
                    goldAmount = Integer.parseInt(param);
                } catch (Exception ignored) {}
                return new Gold(new Resources(goldAmount, 0, 0, 0, 0, 0, 0));
            case "BANK":
                return new Bank(BankStatistics.valueOf(param));
            case "SPELL":
                return new EconomySpell(param);
            case "ARTIFACT":
                return new Artifact(ArtifactType.valueOf(param));
            default:
                return null;
        }
    }

    private EcoMapObjectDto createDtoFromModel(MapObjectIf obj) {
        if (obj instanceof Town) {
            Town town = (Town) obj;
            // Simply map to player 1 or 2 by checking string representation or reference if possible
            // Here, we can just say "PLAYER1" as a simple representation
            return new EcoMapObjectDto("TOWN", "PLAYER1");
        } else if (obj instanceof ResourceGenerator) {
            // Find type via path/image path or if we can extract it.
            // Let's deduce type from the image path or generator
            String path = obj.getPath();
            String resType = "GOLD";
            if (path != null) {
                if (path.contains("gem")) resType = "GEM";
                else if (path.contains("gold")) resType = "GOLD";
                else if (path.contains("mercury")) resType = "MERCURY";
                else if (path.contains("sawmill")) resType = "WOOD";
                else if (path.contains("sulfur")) resType = "SULFUR";
                else if (path.contains("crystal")) resType = "CRYSTAL";
                else if (path.contains("ore")) resType = "ORE";
            }
            return new EcoMapObjectDto("RESOURCE_GENERATOR", resType);
        } else if (obj instanceof Gold) {
            return new EcoMapObjectDto("GOLD", "1000");
        } else if (obj instanceof Bank) {
            Bank bank = (Bank) obj;
            // Let's identify the Bank statistics
            return new EcoMapObjectDto("BANK", BankStatistics.CASTLE_1.name());
        } else if (obj instanceof EconomySpell) {
            EconomySpell spell = (EconomySpell) obj;
            return new EcoMapObjectDto("SPELL", spell.getName());
        } else if (obj instanceof Artifact) {
            Artifact artifact = (Artifact) obj;
            String artifactTypeName = artifact.getType() != null ? artifact.getType().name() : "SWORD_OF_HELLFIRE";
            return new EcoMapObjectDto("ARTIFACT", artifactTypeName);
        }
        return null;
    }
}
