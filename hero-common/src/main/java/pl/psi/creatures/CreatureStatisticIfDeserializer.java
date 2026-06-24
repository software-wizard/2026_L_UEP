package pl.psi.creatures;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Range;

import java.io.IOException;

public class CreatureStatisticIfDeserializer extends JsonDeserializer<CreatureStatisticIf> {
    @Override
    public CreatureStatisticIf deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Scenariusz 1: Enum zapisany po prostu jako String (np. "SKELETON")
        if (node.isTextual()) {
            try {
                return CreatureStatistic.valueOf(node.asText());
            } catch (IllegalArgumentException e) {
                return null;
            }
        }

        // Scenariusz 2: Obiekt JSON reprezentujący statystyki
        if (node.isObject() && node.has("name")) {
            String name = node.get("name").asText();
            // Próbujemy dopasować nazwę do Enuma (najczęstszy przypadek dla potworów z fabryki)
            String enumName = name.toUpperCase().replace(" ", "_");

            try {
                return CreatureStatistic.valueOf(enumName);
            } catch (IllegalArgumentException e) {
                // Scenariusz 3: To nie Enum, to niestandardowy CreatureStats (bohater z buffami z walki)
                // Budujemy go ręcznie używając Buildera, co całkowicie omija błędy z pustymi obiektami i Lombokiem
                int attack = node.has("attack") ? node.get("attack").asInt() : 0;
                int armor = node.has("armor") ? node.get("armor").asInt() : 0;
                int maxHp = node.has("maxHp") ? node.get("maxHp").asInt() : 1;
                int moveRange = node.has("moveRange") ? node.get("moveRange").asInt() : 0;
                int tier = node.has("tier") ? node.get("tier").asInt() : 1;

                // Czasami boolean jest w JSONie jako "isUpgraded", a czasem po prostu "upgraded"
                boolean isUpgraded = (node.has("isUpgraded") && node.get("isUpgraded").asBoolean()) ||
                        (node.has("upgraded") && node.get("upgraded").asBoolean());

                String description = node.has("description") ? node.get("description").asText() : "";

                Range<Integer> damage = Range.closed(1, 1); // Zapasowa wartość
                if (node.has("damage") && node.get("damage").isObject()) {
                    JsonNode dmgNode = node.get("damage");
                    if (dmgNode.has("lowerEndpoint") && dmgNode.has("upperEndpoint")) {
                        damage = Range.closed(dmgNode.get("lowerEndpoint").asInt(), dmgNode.get("upperEndpoint").asInt());
                    }
                }

                return CreatureStats.builder()
                        .name(name)
                        .attack(attack)
                        .armor(armor)
                        .maxHp(maxHp)
                        .moveRange(moveRange)
                        .damage(damage)
                        .tier(tier)
                        .description(description)
                        .isUpgraded(isUpgraded)
                        .build();
            }
        }
        return null;
    }
}
