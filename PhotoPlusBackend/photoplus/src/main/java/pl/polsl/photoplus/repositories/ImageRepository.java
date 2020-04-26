package pl.polsl.photoplus.repositories;

import pl.polsl.photoplus.model.entities.Image;

import java.util.Optional;

public interface ImageRepository extends EntityRepository<Image, Long> {
    Optional<Image> findByName(final String name);
}
