package edu.espe.springprueba.service.impl;

import edu.espe.springprueba.domain.Device;
import edu.espe.springprueba.dto.DeviceCreateRequest;
import edu.espe.springprueba.dto.DeviceResponse;
import edu.espe.springprueba.dto.DeviceUpdateRequest;
import edu.espe.springprueba.repository.DeviceRepository;
import edu.espe.springprueba.service.DeviceService;
import edu.espe.springprueba.web.advice.ConflictException;
import edu.espe.springprueba.web.advice.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DeviceServiceImpl implements DeviceService {

    private final DeviceRepository repo;

    public DeviceServiceImpl(DeviceRepository repo) {
        this.repo = repo;
    }

    @Override
    public DeviceResponse create(DeviceCreateRequest request) {
        // CORRECCIÓN PRUEBA 1: Validar por Sereal, no por nombre
        if (repo.existsBySereal(request.getSereal())) {
            throw new ConflictException("El dispositivo ya está registrado");
        }
        Device l = new Device();
        l.setNombre(request.getNombre());
        l.setCategoría(request.getCategoria());
        l.setSereal(request.getSereal());
        l.setAvailable(true);

        return toResponse(repo.save(l));
    }

    @Override
    public DeviceResponse returnLoan(Long id) {
        Device l = repo.findById(id).orElseThrow(() -> new NotFoundException("Préstamo no encontrado"));
        // CORRECCIÓN PRUEBA 3 y 5: Desactivar es poner available en false (antes estaba en true)
        l.setAvailable(false);
        return toResponse(repo.save(l));
    }

    @Override
    public Map<String, Long> getReport() {
        Map<String, Long> report = new HashMap<>();
        report.put("total", repo.count());
        // CORRECCIÓN PRUEBA 4: Los nombres de las llaves deben coincidir con la prueba
        report.put("available", repo.countByAvailable(true));
        report.put("unavailable", repo.countByAvailable(false));
        return report;
    }

    @Override
    public List<DeviceResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public DeviceResponse update(Long id, DeviceUpdateRequest req) {
        Device l = repo.findById(id).orElseThrow(() -> new NotFoundException("Préstamo no encontrado"));

        l.setNombre(req.getNombre());
        l.setCategoría(req.getCategoria());
        l.setSereal(req.getSereal());
        return toResponse(repo.save(l));
    }

    private DeviceResponse toResponse(Device l){
        DeviceResponse r = new DeviceResponse();
        r.setId(l.getId());
        r.setNombre(l.getNombre());
        r.setCategoría(l.getCategoría());
        r.setAvailable(l.isAvailable());
        return r;
    }
}