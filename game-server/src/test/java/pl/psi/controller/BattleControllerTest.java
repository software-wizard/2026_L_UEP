package pl.psi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BattleControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


//    @Test
//    public void testBattleFlow() {
//        Hero hero1 = new Hero(List.of(),List.of());
//        Hero hero2 = new Hero(List.of(),List.of());
//        List<Hero> heroes = List.of(hero1, hero2);
//
//        ResponseEntity<String> startResponse = restTemplate.postForEntity(
//                "/api/battle/start",
//                heroes,
//                String.class
//        );
//        assertEquals(HttpStatus.OK, startResponse.getStatusCode());
//
//        ResponseEntity<String> passResponse = restTemplate.postForEntity(
//                "/api/battle/pass",
//                null,
//                String.class
//        );
//        assertEquals(HttpStatus.OK, passResponse.getStatusCode());
//        assertEquals("Turn passed", passResponse.getBody());
//    }

    @Test
    public void testBattleFlow() {
        ResponseEntity<String> startResponse = restTemplate.postForEntity(
                "/api/battle/start",
                null,
                String.class
        );
        assertEquals(HttpStatus.OK, startResponse.getStatusCode());

        ResponseEntity<String> passResponse = restTemplate.postForEntity(
                "/api/battle/pass",
                null,
                String.class
        );
        assertEquals(HttpStatus.OK, passResponse.getStatusCode());
        assertEquals("Turn passed", passResponse.getBody());
    }
}