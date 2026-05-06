package pl.psi.gui.proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import pl.psi.EconomyEngine;
import pl.psi.creatures.EconomyCreature;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.LinkedHashMap;
import java.util.Map;

public class EconomyEngineProxy extends EconomyEngine {
    private static final String BASE_URL = "http://localhost:8080/api/board";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EconomyEngineProxy(final pl.psi.hero.EconomyHero hero) {
        super(hero);
    }

    @Override
    public void buy(final EconomyCreature aEconomyCreature) {
        try {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("stats", aEconomyCreature.getStats().name());
            body.put("amount", aEconomyCreature.getAmount());
            body.put("goldCost", aEconomyCreature.getGoldCost());

            String json = objectMapper.writeValueAsString(body);
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/buyCreature"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            HttpResponse<String> res = httpClient.send(req, HttpResponse.BodyHandlers.ofString());
            if (res.statusCode() != 200) {
                throw new IllegalStateException("Server rejected buy: " + res.body());
            }
            super.buy(aEconomyCreature);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Buy request failed", e);
        }
    }
}
