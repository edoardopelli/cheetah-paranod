package org.cheetah.paranod;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class,DataSourceAutoConfiguration.class})
@EnableNeo4jRepositories
public class CheetahParanodApplication {

	public static void main(String[] args) {
		SpringApplication.run(CheetahParanodApplication.class, args);
	}

}
