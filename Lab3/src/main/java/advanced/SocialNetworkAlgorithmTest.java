package advanced;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SocialNetworkAlgorithmTest {
    private SocialNetwork network;
    private Person radu;
    private Person sebastian;
    private Company bitDefender;
    private Company assist;

    @BeforeEach
    void setUp() {
        network = new SocialNetwork();

        radu = new Person(); radu.setName("Arseni Radu");
        sebastian = new Person(); sebastian.setName("Covita Sebastian");
        bitDefender = new Company(); bitDefender.setName("Bit Defender");
        assist = new Company(); assist.setName("ASSIST");

        // Construim relațiile din Main (bidirecționale)
        sebastian.addRelationShip(radu, "Best Friend");
        radu.addRelationShip(sebastian, "Best Friend");

        bitDefender.addRelationShip(sebastian, "Employee");
        sebastian.addRelationShip(bitDefender, "Employer");

        assist.addRelationShip(bitDefender, "Upper");
        bitDefender.addRelationShip(assist, "Under");

        network.addProfile(radu);
        network.addProfile(sebastian);
        network.addProfile(bitDefender);
        network.addProfile(assist);
    }

    @Test
    @DisplayName("Ar trebui să identifice punctele de articulare intr-o retea liniara")
    void testArticulationPointsInLinearNetwork() {
        Tarjan tarjan = new Tarjan(network);
        tarjan.showSolution();
        List<String> points=tarjan.getCutPoints();
        assertNotNull(points);
        assertEquals(2,tarjan.getCutPoints().size(),"Doua noduri stiu ca erau garantat noduri de articulatie");
    }

    @Test
    @DisplayName("Ar trebui să găsească 3 componente maximale (muchii) pentru 4 noduri liniare")
    void testMaximalPartsCount() {
        MaximalPart maximalParts = new MaximalPart(network);

        List<List<Edge>> components = maximalParts.getBiconnectedComponents();
        maximalParts.showSolution();
        assertNotNull(components);
        assertEquals(3, components.size(), "Un graf liniar cu 4 noduri are 3 muchii (componente biconexe)");
    }
}