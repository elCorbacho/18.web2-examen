package ipss.web2.examen.config;

import ipss.web2.examen.models.Album;
import ipss.web2.examen.models.Lamina;
import ipss.web2.examen.repository.AlbumRepository;
import ipss.web2.examen.repository.LaminaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AlbumRepository albumRepository;
    private final LaminaRepository laminaRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verificar si ya existen datos
        if (albumRepository.count() > 0) {
            System.out.println("⚠️ Base de datos ya contiene datos. Saltando inicialización.");
            return;
        }

        System.out.println("🚀 Iniciando población de base de datos con anime populares...");

        // ==================== ALBUM 1: CABALLEROS DEL ZODIACO ====================
        Album album1 = new Album();
        album1.setNombre("Caballeros del Zodiaco");
        album1.setYear(1986);
        album1.setDescripcion("Manga clásico sobre guerreros que protegen a Atenea");
        album1.setActive(true);
        album1.setLaminas(new ArrayList<>());
        Album savedAlbum1 = albumRepository.save(album1);

        crearLaminas(savedAlbum1, new String[][] {
                {"Seiya - Caballero de Pegaso", "https://images.unsplash.com/photo-1617637881555-eae2ba6ee46f?w=400", "PORTADA"},
                {"Armadura Dorada de Aries", "https://images.unsplash.com/photo-1578322154310-cc4ecca2f5d5?w=400", "CONTENIDO"},
                {"Batalla contra Hades", "https://images.unsplash.com/photo-1607084591413-25427a3d4d12?w=400", "CONTENIDO"}
        }, 1986);

        // ==================== ALBUM 2: DRAGON BALL Z ====================
        Album album2 = new Album();
        album2.setNombre("Dragon Ball Z");
        album2.setYear(1989);
        album2.setDescripcion("Saga de acción y aventura que cambió el anime para siempre");
        album2.setActive(true);
        album2.setLaminas(new ArrayList<>());
        Album savedAlbum2 = albumRepository.save(album2);

        crearLaminas(savedAlbum2, new String[][] {
                {"Goku - Super Saiyajin", "https://images.unsplash.com/photo-1632779686507-fe4db93d0f93?w=400", "PORTADA"},
                {"Vegeta - El Príncipe Saiyajin", "https://images.unsplash.com/photo-1633356122544-f134324ef6db?w=400", "CONTENIDO"},
                {"Batalla contra Freezer", "https://images.unsplash.com/photo-1626814026595-cac13b1db48d?w=400", "CONTENIDO"}
        }, 1989);

        // ==================== ALBUM 3: NARUTO ====================
        Album album3 = new Album();
        album3.setNombre("Naruto");
        album3.setYear(2002);
        album3.setDescripcion("Historia épica de un ninja que busca ser reconocido");
        album3.setActive(true);
        album3.setLaminas(new ArrayList<>());
        Album savedAlbum3 = albumRepository.save(album3);

        crearLaminas(savedAlbum3, new String[][] {
                {"Naruto Uzumaki - El Hokage", "https://images.unsplash.com/photo-1633356122544-f134324ef6db?w=400", "PORTADA"},
                {"Sasuke Uchiha - El Último de su Clan", "https://images.unsplash.com/photo-1619983081563-430f63602d4b?w=400", "CONTENIDO"},
                {"Sakura Haruno - Kunoichi Poderosa", "https://images.unsplash.com/photo-1625948515291-89613c66ba51?w=400", "CONTENIDO"}
        }, 2002);

        // ==================== ALBUM 4: DEMON SLAYER ====================
        Album album4 = new Album();
        album4.setNombre("Demon Slayer");
        album4.setYear(2018);
        album4.setDescripcion("La batalla entre cazadores de demonios y fuerzas oscuras");
        album4.setActive(true);
        album4.setLaminas(new ArrayList<>());
        Album savedAlbum4 = albumRepository.save(album4);

        crearLaminas(savedAlbum4, new String[][] {
                {"Tanjiro Kamado - Cazador de Demonios", "https://images.unsplash.com/photo-1611339555312-e607c25352ca?w=400", "PORTADA"},
                {"Nezuko - El Demonio Humano", "https://images.unsplash.com/photo-1618519764d82b19d648d1aac2e2b63500cf471a?w=400", "CONTENIDO"},
                {"Hashira - Los Pilares de Fuego", "https://images.unsplash.com/photo-1619983081563-430f63602d4b?w=400", "CONTENIDO"}
        }, 2018);

        // ==================== ALBUM 5: BERSERK ====================
        Album album5 = new Album();
        album5.setNombre("Berserk");
        album5.setYear(1997);
        album5.setDescripcion("Oscura epopeya de un guerrero en busca de venganza");
        album5.setActive(true);
        album5.setLaminas(new ArrayList<>());
        Album savedAlbum5 = albumRepository.save(album5);

        crearLaminas(savedAlbum5, new String[][] {
                {"Guts - El Guerrero Negro", "https://images.unsplash.com/photo-1618519764d82b19d648d1aac2e2b63500cf471a?w=400", "PORTADA"},
                {"La Mano del Dios", "https://images.unsplash.com/photo-1607084591413-25427a3d4d12?w=400", "CONTENIDO"},
                {"Griffith - El Falcón Blanco", "https://images.unsplash.com/photo-1633356122544-f134324ef6db?w=400", "CONTENIDO"}
        }, 1997);

        System.out.println("✅ Base de datos poblada exitosamente");
        System.out.println("   📚 5 Álbumes creados");
        System.out.println("   🖼️  15 Láminas creadas");
    }

    private void crearLaminas(Album album, String[][] laminasData, int year) {
        for (String[] datos : laminasData) {
            Lamina lamina = new Lamina();
            lamina.setNombre(datos[0]);
            lamina.setImagen(datos[1]);
            lamina.setTipoLamina(datos[2]);
            lamina.setFechaLanzamiento(LocalDate.of(year, 1, 1));
            lamina.setAlbum(album);
            lamina.setActive(true);

            Lamina savedLamina = laminaRepository.save(lamina);
            album.getLaminas().add(savedLamina);
        }

        albumRepository.save(album);
    }
}
