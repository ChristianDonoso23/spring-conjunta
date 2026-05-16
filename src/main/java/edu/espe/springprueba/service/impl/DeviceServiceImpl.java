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
        if (repo.existsByNombre(request.getNombre())) {
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
    public List<DeviceResponse> list() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public DeviceResponse returnLoan(Long id) {
        Device l = repo.findById(id).orElseThrow(() -> new NotFoundException("Préstamo no encontrado"));
        l.setAvailable(true);
        return toResponse(repo.save(l));
    }

    @Override
    public DeviceResponse update(Long id, DeviceUpdateRequest req) {
        Device l = repo.findById(id).orElseThrow(() -> new NotFoundException("Préstamo no encontrado"));

        l.setNombre(req.getNombre());
        l.setCategoría(req.getCategoria());
        l.setSereal(req.getSereal());
        return toResponse(repo.save(l));
    }

    @Override
    public Map<String, Long> getReport() {
        Map<String, Long> report = new HashMap<>();
        report.put("total", repo.count());
        report.put("returned", repo.countByAvailable(true));
        report.put("pending", repo.countByAvailable(false));
        return report;
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