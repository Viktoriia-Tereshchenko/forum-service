package ait.cohort5860.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
// где находятся все репозитории
@EnableJpaRepositories(basePackages = "ait.cohort5860.post.dao")
//настройка транзакций
@EnableTransactionManagement
public class JpaConfig {
}
