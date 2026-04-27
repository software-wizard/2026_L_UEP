package pl.psi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoardEconomyControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testBoardEconomyFlow() {
        ResponseEntity<String> startResponse = restTemplate.postForEntity(
                "/api/board/start?fraction1=NECROPOLIS&fraction2=NECROPOLIS", null, String.class
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
}