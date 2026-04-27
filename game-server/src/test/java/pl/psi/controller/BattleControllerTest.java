package pl.psi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BattleControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testBattleFlow() {
        ResponseEntity<String> startResponse = restTemplate.postForEntity(
                "/api/battle/start", null, String.class
        );
        assertEquals(HttpStatus.OK, startResponse.getStatusCode());

        restTemplate.getForEntity("/api/battle/creature?x=0&y=0", String.class);
        restTemplate.getForEntity("/api/battle/isCurrentCreature?x=0&y=0", String.class);
        restTemplate.getForEntity("/api/battle/canMove?x=1&y=1", String.class);
        restTemplate.getForEntity("/api/battle/canAttack?x=2&y=2", String.class);
        restTemplate.getForEntity("/api/battle/specialField?x=3&y=3", String.class);

        ResponseEntity<String> moveResp = restTemplate.postForEntity("/api/battle/move?x=1&y=1", null, String.class);
        assertTrue(moveResp.getStatusCode().is4xxClientError() || moveResp.getStatusCode().is2xxSuccessful());

        ResponseEntity<String> attackResp = restTemplate.postForEntity("/api/battle/attack?x=2&y=2", null, String.class);
        assertTrue(attackResp.getStatusCode().is4xxClientError() || attackResp.getStatusCode().is2xxSuccessful());

        ResponseEntity<String> interactResp = restTemplate.postForEntity("/api/battle/interact?x=3&y=3", null, String.class);
        assertTrue(interactResp.getStatusCode().is4xxClientError() || interactResp.getStatusCode().is2xxSuccessful());

        ResponseEntity<String> passResponse = restTemplate.postForEntity("/api/battle/pass", null, String.class);
        assertTrue(passResponse.getStatusCode().is4xxClientError() || passResponse.getStatusCode().is2xxSuccessful());
    }
}