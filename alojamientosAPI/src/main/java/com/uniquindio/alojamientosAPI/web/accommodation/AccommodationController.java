package com.uniquindio.alojamientosAPI.web.accommodation;

import com.uniquindio.alojamientosAPI.domain.dto.accommodation.AccommodationDTO;
import com.uniquindio.alojamientosAPI.domain.mapper.accommodation.AccommodationMapper;
import com.uniquindio.alojamientosAPI.domain.service.accommodation.AccommodationService;
import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.AccommodationEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;
    private final AccommodationMapper accommodationMapper;

    @PreAuthorize("hasRole('ANFITRION')")
    @PostMapping
    public ResponseEntity<AccommodationDTO> create(@RequestBody AccommodationDTO dto) {
        AccommodationEntity saved = accommodationService.create(accommodationMapper.toEntity(dto));
        return ResponseEntity.ok(accommodationMapper.toDto(saved));
    }

    @PreAuthorize("hasRole('ANFITRION')")
    @PutMapping("/{id}")
    public ResponseEntity<AccommodationDTO> update(@PathVariable Long id, @RequestBody AccommodationDTO dto) {
        AccommodationEntity updated = accommodationService.update(id, accommodationMapper.toEntity(dto));
        return ResponseEntity.ok(accommodationMapper.toDto(updated));
    }

    @PreAuthorize("hasRole('ANFITRION')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        accommodationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<AccommodationDTO>> listAll() {
        List<AccommodationDTO> result = accommodationService.listAll()
                .stream().map(accommodationMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccommodationDTO> findById(@PathVariable Long id) {
        return accommodationService.findById(id)
                .map(accommodationMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<AccommodationDTO>> findByCity(@PathVariable Long cityId) {
        List<AccommodationDTO> result = accommodationService.findByCity(cityId)
                .stream().map(accommodationMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/host/{hostUserId}")
    public ResponseEntity<List<AccommodationDTO>> findByHost(@PathVariable Long hostUserId) {
        List<AccommodationDTO> result = accommodationService.findByHost(hostUserId)
                .stream().map(accommodationMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ANFITRION','ADMINISTRADOR')")
    @PostMapping("/{id}/activate")
    public ResponseEntity<AccommodationDTO> activate(@PathVariable Long id) {
        AccommodationEntity updated = accommodationService.activate(id);
        return ResponseEntity.ok(accommodationMapper.toDto(updated));
    }

    @PreAuthorize("hasAnyRole('ANFITRION','ADMINISTRADOR')")
    @PostMapping("/{id}/deactivate")
    public ResponseEntity<AccommodationDTO> deactivate(@PathVariable Long id) {
        AccommodationEntity updated = accommodationService.deactivate(id);
        return ResponseEntity.ok(accommodationMapper.toDto(updated));
    }
}
