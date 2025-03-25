package ru.ispras.wtprac.dealership;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.ispras.wtprac.dealership.DAO.BrandDAO;
import ru.ispras.wtprac.dealership.DAO.ModelDAO;
import ru.ispras.wtprac.dealership.model.Brand;
import ru.ispras.wtprac.dealership.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestPropertySource(locations = "classpath:application.properties")
public class ModelDAOTests {

    @Autowired
    private ModelDAO modelDAO;

    @Autowired
    private BrandDAO brandDAO;

    @Autowired
    private SessionFactory sessionFactory;

    @Test
    void testGetAllModels() {
        List<Model> modelList = modelDAO.getAll().stream().toList();
        assertEquals(3, modelList.size());

        modelDAO.deleteOne(modelList.get(0));
        List<Model> modelList2 = modelDAO.getAll().stream().toList();
        assertEquals(2, modelList2.size());

        modelDAO.saveOne(new Model("Model Y"));
        List<Model> modelList3 = modelDAO.getAll().stream().toList();
        assertEquals(3, modelList3.size());

        List<Model> modelList4 = modelDAO.getAllModelsByBrand("Chevrolet").stream().toList();
        assertEquals(1, modelList4.size());
        Model model = modelList4.get(0);
        model.getAdditionalInfo().put("Color", "Yellow");
        modelDAO.updateOne(model);

        List<Model> modelList5 = modelDAO.getAllModelsByBrand("Chevrolet").stream().toList();
        assertEquals(1, modelList5.size());
        model = modelList5.get(0);
        assertEquals("Camaro", model.getName());
        assertEquals("Yellow", model.getAdditionalInfo().get("Color"));
    }

    @Test
    void testGetPorsche() {
        List<Model> modelList = modelDAO.getAllModelsByBrand("Porsche").stream().toList();
        assertEquals(1, modelList.size());

        Model modelPorsche = modelList.get(0);
        assertEquals("Porsche", modelPorsche.getBrand().getName());
        assertEquals("911", modelPorsche.getName());
        assertEquals("PDK", modelPorsche.getAdditionalInfo().get("transmission"));
    }

    @Test
    void testGetModelsByYear() {
        List<Model> modelList = modelDAO.getAllModelsByYear(2016, 2021).stream().toList();
        assertEquals(2, modelList.size());
        assertEquals("911", modelList.get(0).getName());
        assertEquals("Camaro", modelList.get(1).getName());

        modelList = modelDAO.getAllModelsByYear(2022).stream().toList();
        assertEquals(1, modelList.size());
        Model modelToyota = modelList.get(0);
        assertEquals(2, modelToyota.getId());
    }

    @BeforeEach
    void setUp() {
        List<Brand> brandList = new ArrayList<>();
        brandList.add(new Brand("Porsche"));
        brandList.add(new Brand("Toyota"));
        brandList.add(new Brand("Chevrolet"));
        brandDAO.saveCollection(brandList);

        List<Model> modelList = new ArrayList<>();
        modelList.add(new Model(1L, "911", brandDAO.getBrandByName("Porsche"), 2021,
                Map.of("transmission", "PDK")));
        modelList.add(new Model(2L, "Camry", brandDAO.getBrandByName("Toyota"), 2022,
                new HashMap<String, String>()));
        modelList.add(new Model(3L, "Camaro", brandDAO.getBrandByName("Chevrolet"), 2019,
                new HashMap<String, String>()));
        modelDAO.saveCollection(modelList);
    }

    @BeforeAll
    @AfterEach
    void endUp() {
        modelDAO.deleteAllEntries();
        brandDAO.deleteAllEntries();
    }
}
