package pl.psi.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EconomyControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;


//    @Test
//    public void testEconomyStart() {
//        EconomyHero startingHero = new EconomyHero(EconomyHero.Fraction.NECROPOLIS, 3000);
//        ResponseEntity<String> startResponse = restTemplate.postForEntity(
//                "/api/economy/start",
//                startingHero,
//                String.class
//        );
//        assertEquals(HttpStatus.OK, startResponse.getStatusCode());
//
//        ResponseEntity<EconomyHero> heroResponse = restTemplate.getForEntity(
//                "/api/economy/hero",
//                EconomyHero.class
//        );
//        assertEquals(HttpStatus.OK, heroResponse.getStatusCode());
//        assertNotNull(heroResponse.getBody());
//        assertEquals(3000, heroResponse.getBody().getGold().getAmount());
//
//        EconomyCreature skeleton = new EconomyCreature();
//        ResponseEntity<String> buyResponse = restTemplate.postForEntity(
//                "/api/economy/buy",
//                skeleton,
//                String.class
//        );
//        assertEquals(HttpStatus.OK, buyResponse.getStatusCode());
//    }

    @Test
    public void testEconomyStart() {
        ResponseEntity<String> startResponse = restTemplate.postForEntity(
                "/api/economy/start?fraction=NECROPOLIS",
                null,
                String.class
        );
        assertEquals(HttpStatus.OK, startResponse.getStatusCode());

        ResponseEntity<String> heroResponse = restTemplate.getForEntity(
                "/api/economy/hero",
                String.class
        );
        assertEquals(HttpStatus.OK, heroResponse.getStatusCode());
        assertNotNull(heroResponse.getBody());

        ResponseEntity<String> buyResponse = restTemplate.postForEntity(
                "/api/economy/buy?upgraded=false&tier=1&amount=1",
                null,
                String.class
        );
        assertEquals(HttpStatus.OK, buyResponse.getStatusCode());
    }
}