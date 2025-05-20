package br.com.projCarro.CarroProjeto;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

@SpringBootTest
@ActiveProfiles("test")                       // usa application-test.properties
@AutoConfigureTestDatabase(replace = Replace.ANY) // instrui o Spring a usar o banco de teste (H2)
class CarroProjetoApplicationTests {

	@Test
	void contextLoads() {
	}

}
