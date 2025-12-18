package ipss.web2.examen.repository;

import ipss.web2.examen.models.Lamina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaminaRepository extends JpaRepository<Lamina, Long> {
    List<Lamina> findByAlbumId(Long albumId);
    
    List<Lamina> findByActiveTrue();
}
