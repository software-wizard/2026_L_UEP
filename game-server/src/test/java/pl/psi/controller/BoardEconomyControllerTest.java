package pl.psi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pl.psi.hero.EconomyHero;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardEconomyControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testBoardEconomyFlow() {
        List<EconomyHero> heroes = List.of(new EconomyHero(), new EconomyHero());

        ResponseEntity<String> startResponse = restTemplate.postForEntity(
                "/api/board/start", heroes, String.class
        );
        assertEquals(HttpStatus.OK, startResponse.getStatusCode());

        ResponseEntity<String> currentHeroResponse = restTemplate.getForEntity(
                "/api/board/currentHero", String.class
        );
        assertEquals(HttpStatus.OK, currentHeroResponse.getStatusCode());
        assertNotNull(currentHeroResponse.getBody());

        restTemplate.getForEntity("/api/board/isCurrentHero?x=0&y=0", String.class);
        restTemplate.getForEntity("/api/board/isHero?x=0&y=0", String.class);
        restTemplate.getForEntity("/api/board/mapObject?x=1&y=1", String.class);
        restTemplate.getForEntity("/api/board/canMove?x=1&y=1", String.class);
        restTemplate.getForEntity("/api/board/canAttack?x=2&y=2", String.class);
        restTemplate.getForEntity("/api/board/canInteract?x=3&y=3", String.class);
        restTemplate.getForEntity("/api/board/canEnter?x=4&y=4", String.class);

        ResponseEntity<String> moveResponse = restTemplate.postForEntity("/api/board/move?x=1&y=1", null, String.class);
        assertTrue(moveResponse.getStatusCode().is4xxClientError() || moveResponse.getStatusCode().is2xxSuccessful());

        ResponseEntity<String> interactResp = restTemplate.postForEntity("/api/board/interact?x=3&y=3", null, String.class);
        assertTrue(interactResp.getStatusCode().is4xxClientError() || interactResp.getStatusCode().is2xxSuccessful());

        ResponseEntity<String> enterResp = restTemplate.postForEntity("/api/board/enter?x=4&y=4", null, String.class);
        assertTrue(enterResp.getStatusCode().is4xxClientError() || enterResp.getStatusCode().is2xxSuccessful());

        ResponseEntity<String> secondInteractResp = restTemplate.postForEntity("/api/board/secondInteraction?x=5&y=5", null, String.class);
        assertTrue(secondInteractResp.getStatusCode().is4xxClientError() || secondInteractResp.getStatusCode().is2xxSuccessful());

        ResponseEntity<String> passResp = restTemplate.postForEntity("/api/board/pass", null, String.class);
        assertEquals(HttpStatus.OK, passResp.getStatusCode());
    }

    @Test
    public void testGrailDiggingAndTownUpgradingEndToEnd() {
        pl.psi.map.resources.Resources startingRes = new pl.psi.map.resources.Resources(100000, 100, 100, 100, 100, 100, 100);
        pl.psi.hero.Statistics stats = new pl.psi.hero.Statistics(10, 10, 10, 10);
        List<EconomyHero> heroes = List.of(
                new EconomyHero(EconomyHero.Fraction.NECROPOLIS, startingRes, stats),
                new EconomyHero(EconomyHero.Fraction.NECROPOLIS, startingRes, stats)
        );

        // Start Board Economy
        ResponseEntity<String> startResponse = restTemplate.postForEntity(
                "/api/board/start", heroes, String.class
        );
        assertEquals(HttpStatus.OK, startResponse.getStatusCode());

        // 1. Digging on empty tile (0, 0) should fail
        ResponseEntity<String> digEmptyResponse = restTemplate.postForEntity("/api/board/dig", null, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, digEmptyResponse.getStatusCode());
        assertEquals("There is nothing buried here!", digEmptyResponse.getBody());

        // 2. Trying to dig again on the same turn should fail due to "once per turn" limit
        ResponseEntity<String> digAgainResponse = restTemplate.postForEntity("/api/board/dig", null, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, digAgainResponse.getStatusCode());
        assertEquals("You can only dig once per turn!", digAgainResponse.getBody());

        // Reset turn (pass turn for hero1 -> switches to hero2, then pass for hero2 -> switches back to hero1)
        restTemplate.postForEntity("/api/board/pass", null, String.class);
        restTemplate.postForEntity("/api/board/pass", null, String.class);

        // 3. Move to Grail position (2, 3)
        ResponseEntity<String> moveResponse = restTemplate.postForEntity("/api/board/move?x=2&y=3", null, String.class);
        assertEquals(HttpStatus.OK, moveResponse.getStatusCode());

        // Dig on Grail position -> should succeed
        ResponseEntity<String> digGrailResponse = restTemplate.postForEntity("/api/board/dig", null, String.class);
        assertEquals(HttpStatus.OK, digGrailResponse.getStatusCode());
        assertEquals("Grail dug up successfully.", digGrailResponse.getBody());

        // 4. Try to build a building in town when not standing on town -> should fail
        ResponseEntity<String> buildFailResponse = restTemplate.postForEntity("/api/board/buildBuilding?buildingName=TAVERN", null, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, buildFailResponse.getStatusCode());
        assertEquals("Current hero is not on a town tile", buildFailResponse.getBody());

        // 5. Move to Town (1, 1)
        ResponseEntity<String> moveToTownResponse = restTemplate.postForEntity("/api/board/move?x=1&y=1", null, String.class);
        assertEquals(HttpStatus.OK, moveToTownResponse.getStatusCode());

        // Try to build a building that is already built (e.g. FORT) -> should fail
        ResponseEntity<String> buildAlreadyBuiltResponse = restTemplate.postForEntity("/api/board/buildBuilding?buildingName=FORT", null, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, buildAlreadyBuiltResponse.getStatusCode());
        assertEquals("Building already constructed.", buildAlreadyBuiltResponse.getBody());

        // Build a new building (TAVERN) -> should succeed
        ResponseEntity<String> buildSuccessResponse = restTemplate.postForEntity("/api/board/buildBuilding?buildingName=TAVERN", null, String.class);
        assertEquals(HttpStatus.OK, buildSuccessResponse.getStatusCode());
        assertEquals("Building constructed.", buildSuccessResponse.getBody());

        // Pass turn twice to reset the town build limit for the next day
        restTemplate.postForEntity("/api/board/pass", null, String.class);
        restTemplate.postForEntity("/api/board/pass", null, String.class);

        // Now we can build STRUCTURE_OF_THE_GRAIL (requires Grail, which we dug up) -> should succeed
        ResponseEntity<String> buildGrailResponse = restTemplate.postForEntity("/api/board/buildBuilding?buildingName=STRUCTURE_OF_THE_GRAIL", null, String.class);
        assertEquals(HttpStatus.OK, buildGrailResponse.getStatusCode());
        assertEquals("Building constructed.", buildGrailResponse.getBody());
    }
}