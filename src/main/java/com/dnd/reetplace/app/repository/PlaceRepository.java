package com.dnd.reetplace.app.repository;

import com.dnd.reetplace.app.domain.place.Place;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceRepositoryCustom {
    boolean existsByKakaoPid(String kakaoPid);

    Optional<Place> findByKakaoPid(String kakaoPid);
}
