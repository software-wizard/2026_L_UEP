package pl.psi.gui;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.stage.Stage;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

//@Disabled
@Testcontainers
public class ClientDownloadAndE2EIT extends ApplicationTest {

    @Container
    public static ComposeContainer environment;

    static {
        File composeFile = new File("../docker-compose.yaml");
        environment = new ComposeContainer(composeFile)
                .withExposedService("game-server", 8080)
                .withLocalCompose(true);
    }

    @Override
    public void start(Stage stage) throws Exception {
        new pl.psi.gui.EconomyBoardStart().start(stage);
    }

    @Test
    public void shouldDownloadClientAndBuyCreature() throws Exception {
        String serverUrl = "http://" + environment.getServiceHost("game-server", 8080) +
                ":" + environment.getServicePort("game-server", 8080);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/api/download-client"))
                .build();

        Path zipPath = Path.of("target/test-downloads/client.zip");
        java.nio.file.Files.createDirectories(zipPath.getParent());
        client.send(request, HttpResponse.BodyHandlers.ofFile(zipPath));

        HttpRequest startBoardReq = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/api/board/start"))
                .POST(HttpRequest.BodyPublishers.ofString("[...]"))
                .header("Content-Type", "application/json")
                .build();

        client.send(startBoardReq, HttpResponse.BodyHandlers.discarding());

        clickOn("#start");
        Thread.sleep(1000);
        clickOn("#tile_8_4");
        Thread.sleep(500);
        clickOn("#passButton");
        Thread.sleep(500);
        clickOn("#tile_9_4");
        Thread.sleep(500);
        clickOn("#passButton");
        Thread.sleep(500);
        clickOn("#otherHero");
        Thread.sleep(5000);
        targetWindow("Mapa bitwy");
        clickOn("#battle_tile_3_2");
        Thread.sleep(500);
        clickOn("#passButtonBattle");
        Thread.sleep(500);
        clickOn("#battle_tile_11_2");
        Thread.sleep(500);
        clickOn("#passButtonBattle");
        Thread.sleep(500);
        clickOn("#battle_tile_6_2");
        Thread.sleep(500);
        clickOn("#passButtonBattle");
        Thread.sleep(500);
        clickOn("#battle_tile_8_2");
        Thread.sleep(500);
        clickOn("#passButtonBattle");
        Thread.sleep(500);
        clickOn("#battle_tile_7_2");
        Thread.sleep(500);
        clickOn("#battle_tile_8_2");
        Thread.sleep(500);
        clickOn("#battle_tile_7_2");
    }
}