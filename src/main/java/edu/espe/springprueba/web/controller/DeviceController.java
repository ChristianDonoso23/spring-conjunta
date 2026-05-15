package edu.espe.springprueba.web.controller;

import edu.espe.springprueba.dto.DeviceCreateRequest;
import edu.espe.springprueba.dto.DeviceResponse;
import edu.espe.springprueba.dto.DeviceUpdateRequest;
import edu.espe.springprueba.service.DeviceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/christiandonoso/devices")
public class DeviceController {

    private final DeviceService service;

    public DeviceController(DeviceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DeviceResponse> createLoan(@Valid @RequestBody DeviceCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public ResponseEntity<List<DeviceResponse>> getAll() {
        return ResponseEntity.ok(service.list());
    }

    @PatchMapping("/{id}/return")
    public ResponseEntity<DeviceResponse> returnLoan(@PathVariable Long id) {
        return ResponseEntity.ok(service.returnLoan(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceResponse> updateLoan(@PathVariable Long id, @Valid @RequestBody DeviceUpdateRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @GetMapping("/report")
    public ResponseEntity<Map<String, Long>> getReport() {
        return ResponseEntity.ok(service.getReport());
    }
}