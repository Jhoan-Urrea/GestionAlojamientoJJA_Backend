package com.uniquindio.alojamientosAPI.domain.service.accommodation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.AccommodationEntity;
import com.uniquindio.alojamientosAPI.persistence.repository.accommodation.AccommodationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccommodationServiceImpl implements AccommodationService {

    private final AccommodationRepository accommodationRepository;

    @Override
    public AccommodationEntity create(AccommodationEntity accommodation) {
        return accommodationRepository.save(accommodation);
    }

    @Override
    public AccommodationEntity update(Long id, AccommodationEntity accommodation) {
        Optional<AccommodationEntity> existing = accommodationRepository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Accommodation not found with id: " + id);
        }
        accommodation.setId(id);
        return accommodationRepository.save(accommodation);
    }

    @Override
    public void delete(Long id) {
        if (!accommodationRepository.existsById(id)) {
            throw new IllegalArgumentException("Accommodation not found with id: " + id);
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

    

}
