package pl.psi.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.Range;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.psi.creatures.CreatureStatisticIf;
import pl.psi.creatures.CreatureStats;

import java.io.IOException;

@Configuration
public class JacksonConfig {

    @Bean
    public Module creatureStatisticModule() {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(CreatureStatisticIf.class, new JsonDeserializer<CreatureStatisticIf>() {
            @Override
            public CreatureStatisticIf deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                JsonNode node = jp.getCodec().readTree(jp);
                String name = node.has("name") ? node.get("name").asText() : "";
                int attack = node.has("attack") ? node.get("attack").asInt() : 0;
                int armor = node.has("armor") ? node.get("armor").asInt() : 0;
                int maxHp = node.has("maxHp") ? node.get("maxHp").asInt() : 0;
                int moveRange = node.has("moveRange") ? node.get("moveRange").asInt() : 0;
                int tier = node.has("tier") ? node.get("tier").asInt() : 1;
                String description = node.has("description") ? node.get("description").asText() : "";
                
                boolean isUpgraded = false;
                if (node.has("isUpgraded")) {
                    isUpgraded = node.get("isUpgraded").asBoolean();
                } else if (node.has("upgraded")) {
                    isUpgraded = node.get("upgraded").asBoolean();
                }

                Range<Integer> damage = Range.closed(0, 0);
                if (node.has("damage")) {
                    JsonNode dmgNode = node.get("damage");
                    int lower = 0;
                    int upper = 0;
                    if (dmgNode.has("lowerEndpoint")) {
                        lower = dmgNode.get("lowerEndpoint").asInt();
                    }
                    if (dmgNode.has("upperEndpoint")) {
                        upper = dmgNode.get("upperEndpoint").asInt();
                    }
                    damage = Range.closed(lower, upper);
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
        });
        return module;
    }
}
