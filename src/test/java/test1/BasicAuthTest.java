package test1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes=app.start.Start.class)
@TestPropertySource(locations="classpath:test1.properties")
public class BasicAuthTest {

	@Test
	public void test() {
		
	}
}

