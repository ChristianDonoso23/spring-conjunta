package edu.espe.springprueba.repository;

import edu.espe.springprueba.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    // Corregido: 'BookTitle' ahora es 'Nombre'
    Optional<Device> findByNombre(String nombre);
    boolean existsByNombre(String nombre);

    // TAREA 6: Para el reporte (Cambiado de 'Returned' a 'Available')
    long countByAvailable(boolean available);

    // NUEVO: Para la Prueba 6 (Búsqueda por categoría parcial)
    // Corregido: 'BorrowerName' ahora es 'Categoría' (debe tener tilde porque tu entidad tiene tilde)
    List<Device> findByCategoríaContainingIgnoreCase(String categoria);
}