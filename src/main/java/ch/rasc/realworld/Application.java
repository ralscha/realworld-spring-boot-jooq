package ch.rasc.realworld;

import java.time.ZoneOffset;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static final Logger log = LoggerFactory.getLogger("app");

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneOffset.UTC));

		SpringApplication.run(Application.class, args);
	}
}
