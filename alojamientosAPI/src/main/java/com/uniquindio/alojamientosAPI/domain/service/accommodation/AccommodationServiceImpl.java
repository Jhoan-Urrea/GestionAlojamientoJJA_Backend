package com.uniquindio.alojamientosAPI.domain.service.accommodation;

import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.AccommodationEntity;
import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.StateAccommodationEntity;
import com.uniquindio.alojamientosAPI.persistence.entity.user.RoleEntity;
import com.uniquindio.alojamientosAPI.persistence.entity.user.RoleEnum;
import com.uniquindio.alojamientosAPI.persistence.entity.user.UserEntity;
import com.uniquindio.alojamientosAPI.persistence.repository.UserRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.accommodation.AccommodationRepository;
import com.uniquindio.alojamientosAPI.persistence.repository.accommodation.StateAccommodationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;
    private final StateAccommodationRepository stateAccommodationRepository;

    @Override
    public AccommodationEntity create(AccommodationEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entidad de alojamiento requerida");
        }
        // Validar host
        UserEntity host = entity.getHostUser();
        if (host == null || host.getId() == null) {
            throw new IllegalArgumentException("host_user_id es obligatorio");
        }
        UserEntity hostDb = userRepository.findById(host.getId())
                .orElseThrow(() -> new IllegalArgumentException("Anfitrión no encontrado"));
        boolean isHost = hostDb.getRoles() != null && hostDb.getRoles().stream()
                .map(RoleEntity::getName)
                .anyMatch(r -> r == RoleEnum.ANFITRION);
        if (!isHost) {
            throw new IllegalStateException("El usuario indicado no tiene rol ANFITRION");
        }
        // Persistir
        entity.setId(null);
        return accommodationRepository.save(entity);
    }

    @Override
    public AccommodationEntity update(Long id, AccommodationEntity updated) {
        if (!accommodationRepository.existsById(id)) {
            throw new EntityNotFoundException("Accommodation not found with id: " + id);
        }
        updated.setId(id);
        return accommodationRepository.save(updated);
    }

    @Override
    public void delete(Long id) {
        if (!accommodationRepository.existsById(id)) {
            throw new EntityNotFoundException("Accommodation not found with id: " + id);
        }
        accommodationRepository.deleteById(id);
    }

    @Override
    public List<AccommodationEntity> listAll() {
        return accommodationRepository.findAll();
    }

    @Override
    public Optional<AccommodationEntity> findById(Long id) {
        return accommodationRepository.findById(id);
    }

    @Override
    public List<AccommodationEntity> findByCity(Long cityId) {
        return accommodationRepository.findByCityId(cityId);
    }

    @Override
    public List<AccommodationEntity> findByHost(Long hostUserId) {
        return accommodationRepository.findByHostUserId(hostUserId);
    }

    @Override
    public List<AccommodationEntity> findByState(Long stateId) {
        return accommodationRepository.findByStateAccommodationId(stateId);
    }

    @Override
    public AccommodationEntity updateState(Long id, String stateName) {
        AccommodationEntity acc = accommodationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Accommodation not found with id: " + id));
        if (stateName == null || stateName.isBlank()) {
            throw new IllegalArgumentException("Nombre de estado requerido");
        }
        StateAccommodationEntity state = stateAccommodationRepository.findByName(stateName)
                .orElseThrow(() -> new IllegalArgumentException("Estado no encontrado: " + stateName));
        acc.setStateAccommodation(state);
        return accommodationRepository.save(acc);
    }
}
