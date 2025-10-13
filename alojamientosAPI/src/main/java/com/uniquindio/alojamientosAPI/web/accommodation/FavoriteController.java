package com.uniquindio.alojamientosAPI.web.accommodation;

import com.uniquindio.alojamientosAPI.domain.service.accommodation.FavoriteService;
import com.uniquindio.alojamientosAPI.persistence.entity.accommodation.FavoriteEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<FavoriteEntity> addFavorite(@RequestBody FavoriteEntity favorite) {
        return ResponseEntity.ok(favoriteService.addFavorite(favorite));
    }

    @GetMapping
    public ResponseEntity<List<FavoriteEntity>> getAllFavorites() {
        return ResponseEntity.ok(favoriteService.listAll());
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<FavoriteEntity>> getFavoritesByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.findByUser(userId));
    }

    @GetMapping("/accommodation/{accommodationId}")
    public ResponseEntity<List<FavoriteEntity>> getFavoritesByAccommodation(@PathVariable Long accommodationId) {
        return ResponseEntity.ok(favoriteService.findByAccommodation(accommodationId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(@PathVariable Long id) {
        favoriteService.delete(id); // ✅ usa el nombre correcto del método
        return ResponseEntity.noContent().build();
    }

}
