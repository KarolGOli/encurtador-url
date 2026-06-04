package desafio.encurtador_url;

import org.springframework.boot.SpringApplication;

public class TestEncurtadorUrlApplication {

	public static void main(String[] args) {
		SpringApplication.from(EncurtadorUrlApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
