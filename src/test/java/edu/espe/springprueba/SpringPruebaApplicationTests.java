package edu.espe.springprueba;

import edu.espe.springprueba.domain.Device;
import edu.espe.springprueba.dto.DeviceCreateRequest;
import edu.espe.springprueba.repository.DeviceRepository;
import edu.espe.springprueba.service.DeviceService;
import edu.espe.springprueba.web.advice.ConflictException;
import edu.espe.springprueba.web.advice.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest
@AutoConfigureMockMvc
class SpringPruebaApplicationTests {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceRepository deviceRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        deviceRepository.deleteAll();
    }

    @Test
    void prueba1_evitarSerialDuplicados() {
        DeviceCreateRequest req = new DeviceCreateRequest();
        req.setNombre("Dispositivo Test");
        req.setSereal("ABC-001");
        req.setCategoria("Router");

        deviceService.create(req);

        DeviceCreateRequest reqDuplicado = new DeviceCreateRequest();
        reqDuplicado.setNombre("Otro Dispositivo");
        reqDuplicado.setSereal("ABC-001");
        reqDuplicado.setCategoria("Laptop");

        assertThrows(ConflictException.class, () -> {
            deviceService.create(reqDuplicado);
        });
    }

    // Prueba 2: Excepción ID inexistente
    @Test
    void prueba2_noPermitirStockNegativo() {
        Device device = new Device();
        device.setNombre("Teclado");
        device.setSereal("TEC-123");
        device.setCategoría("Periféricos");
        device.setStock(-5);

        assertThrows(Exception.class, () -> {
            deviceRepository.save(device);
            deviceRepository.flush();
        });
    }

    // Prueba 3: PATCH - Devolver Dispositivo
    @Test
    void prueba3_desactivarDispositivo() {
        // Crear dispositivo activo
        Device device = new Device();
        device.setNombre("Monitor");
        device.setSereal("MON-001");
        device.setCategoría("Pantallas");
        device.setAvailable(true);
        device = deviceRepository.save(device);

        deviceService.returnLoan(device.getId());

        Device updated = deviceRepository.findById(device.getId()).get();
        assertFalse(updated.isAvailable());
        assertEquals("Monitor", updated.getNombre());
    }

    // Prueba 4: Estadísticas /report
    @Test
    void prueba4_estadisticasInventario() {
        crearDispositivo("Laptop", true);
        crearDispositivo("Mouse", true);
        crearDispositivo("Teclado", false);

        Map<String, Long> report = deviceService.getReport();
        assertEquals(3L, report.get("total"));

        assertEquals(2L, report.get("available"));
        assertEquals(1L, report.get("unavailable"));
    }
    @Test
    void prueba5_eliminacionLogica() {
        Device device = new Device();
        device.setNombre("Webcam");
        device.setSereal("WEB-001");
        device.setCategoría("Periféricos");
        device.setAvailable(true);
        device = deviceRepository.save(device);

        deviceService.returnLoan(device.getId());

        assertTrue(deviceRepository.findById(device.getId()).isPresent());

        assertFalse(deviceRepository.findById(device.getId()).get().isAvailable());
    }
    // Prueba 6: Funcionalidad extra - Búsqueda por nombre parcial
    @Test
    void prueba6_busquedaParcialCategoria() {
        crearDispositivoConCategoria("Asus", "Laptop");
        crearDispositivoConCategoria("Alienware", "Laptop Gamer");
        crearDispositivoConCategoria("TP-Link", "Router");

        List<Device> results = deviceRepository.findByCategoríaContainingIgnoreCase("lap");

        assertEquals(2, results.size());
    }

    private void crearDispositivo(String nombre, boolean disponible) {
        Device l = new Device();
        l.setNombre(nombre);
        l.setSereal("SR-" + nombre.substring(0, 2).toUpperCase());
        l.setCategoría("General");
        l.setAvailable(disponible);
        l.setStock(10);
        deviceRepository.save(l);
    }

    private void crearDispositivoConCategoria(String nombre, String categoria) {
        Device l = new Device();
        l.setNombre(nombre);
        l.setSereal("SR-" + nombre.substring(0, 2).toUpperCase());
        l.setCategoría(categoria);
        l.setAvailable(true);
        l.setStock(5);
        deviceRepository.save(l);
    }
}