package edu.espe.springprueba.repository;

import edu.espe.springprueba.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByBookTitle(String nombre);
    boolean existsLoanByBookTitle(String nombre);

    // TAREA 6: Para el reporte
    long countByReturned(boolean returned);

    // NUEVO: Para la Prueba 6 (Búsqueda por nombre parcial)
    List<Device> findByBorrowerNameContainingIgnoreCase(String Categoria);
}