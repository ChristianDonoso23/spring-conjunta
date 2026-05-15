package edu.espe.springprueba;

import edu.espe.springprueba.domain.Device;
import edu.espe.springprueba.dto.DeviceCreateRequest;
import edu.espe.springprueba.repository.DeviceRepository;
import edu.espe.springprueba.service.DeviceService;
import edu.espe.springprueba.web.advice.ConflictException;
import edu.espe.springprueba.web.advice.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
        req.setSereal("Clean Code");
        req.setNombre("Christian Donoso");

        deviceService.create(req);

        assertThrows(ConflictException.class, () -> {
            deviceService.create(req);
        });
    }

    // Prueba 2: Excepción ID inexistente
    @Test
    void prueba2_excepcionIdInexistente() {
        assertThrows(NotFoundException.class, () -> {
            deviceService.returnLoan(9999L);
        });
    }

    // Prueba 3: PATCH - Devolver Dispositivo
    @Test
    void prueba3_devolverLibro() {
        Device device = new Device();
        device.setNombre("Test Book");
        device.setCategoría("Christian Donoso");
        device.setAvailable(false);
        device = deviceRepository.save(device);

        deviceService.returnLoan(device.getId());

        Device updated = deviceRepository.findById(device.getId()).get();
        assertTrue(updated.isAvailable());
    }

    // Prueba 4: Estadísticas /report
    @Test
    void prueba4_estadisticasReporte() {
        crearPrestamo("Dispositivo 1", true);
        crearPrestamo("Dispotitivo 2", true);
        crearPrestamo("Dispositivo 3", false);

        Map<String, Long> report = deviceService.getReport();
        assertEquals(3L, report.get("total"));
        assertEquals(2L, report.get("available"));
        assertEquals(1L, report.get("unavailable"));
    }

    // Prueba 5: Interceptor - Header presente
    @Test
    void prueba5_interceptorHeader() throws Exception {
        mockMvc.perform(get("/api/christiandonoso/devices"))
                .andExpect(status().isOk())
                .andExpect(header().exists("X-Request-Count"))
                .andExpect(header().string("X-Request-Count", notNullValue()));
    }

    // Prueba 6: Funcionalidad extra - Búsqueda por nombre parcial
    @Test
    void prueba6_busquedaNombreParcial() {
        crearPrestamoConNombre("Laptop");
        crearPrestamoConNombre("Laptop Gamer");
        crearPrestamoConNombre("Router");

        List<Device> results = deviceRepository.
                findByBorrowerNameContainingIgnoreCase("lap");
        assertEquals(3, results.size());
    }

    // Métodos auxiliares
    private void crearPrestamo(String title, boolean returned) {
        Device l = new Device();
        l.setNombre(title);
        l.setSereal("Christian");
        l.setAvailable(returned);
        deviceRepository.save(l);
    }

    private void crearPrestamoConNombre(String name) {
        Device l = new Device();
        l.setNombre("Libro " + name);
        l.setSereal(name);
        l.setAvailable(false);
        deviceRepository.save(l);
    }
}