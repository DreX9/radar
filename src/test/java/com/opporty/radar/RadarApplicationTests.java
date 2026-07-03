package com.opporty.radar;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RadarApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testUsernameGenerationLogic() {
		String prefix = "u";
		String dni = "74589632";
		int currentYear = java.time.LocalDate.now().getYear();
		String lastFourDni = dni.length() >= 4 ? dni.substring(dni.length() - 4) : dni;
		int suffixVal = currentYear - 2026 + 1;
		String suffix = suffixVal > 0 ? String.format("%02d", suffixVal) : "01";
		String generated = prefix + currentYear + lastFourDni + suffix;
		
		org.junit.jupiter.api.Assertions.assertEquals("u2026963201", generated);
	}

}
