package edu.espe.springprueba.service;

import edu.espe.springprueba.dto.DeviceCreateRequest;
import edu.espe.springprueba.dto.DeviceResponse;
import edu.espe.springprueba.dto.DeviceUpdateRequest;

import java.util.List;
import java.util.Map;

public interface DeviceService {
    DeviceResponse create(DeviceCreateRequest request);
    List<DeviceResponse> list();
    DeviceResponse returnLoan(Long id);
    DeviceResponse update(Long id, DeviceUpdateRequest request);
    Map<String, Long> getReport();
}