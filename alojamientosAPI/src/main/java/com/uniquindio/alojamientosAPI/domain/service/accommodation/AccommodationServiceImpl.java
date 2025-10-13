package com.uniquindio.alojamientosAPI.domain.service.accommodation;

import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.AccommodationEntity;
import com.uniquindio.alojamientosAPI.persistence.repository.accommodation.AccommodationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationRepository accommodationRepository;

    @Override
    public AccommodationEntity create(AccommodationEntity entity) {
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
}
