package com.pethome;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Prueba de INTEGRACIÓN (smoke test): levanta el contexto completo de Spring
 * (controllers, seguridad, JPA, beans) usando el perfil "test" (H2 en memoria).
 * Si algo está mal cableado —un bean que no se puede crear, una entidad que no
 * mapea— este test falla al arrancar. Es la forma más barata de detectar que la
 * aplicación entera "enciende" correctamente.
 */
@SpringBootTest
@ActiveProfiles("test")
class PethomeApplicationTests {

	@Test
	void contextLoads() {
	}

}
