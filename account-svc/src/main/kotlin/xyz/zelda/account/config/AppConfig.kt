package xyz.zelda.account.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import xyz.zelda.infra.async.ContextCopyingDecorator
import xyz.zelda.infra.config.StaffjoyRestConfig
import java.util.concurrent.Executor

@Configuration
@EnableAsync
@Import(value = [StaffjoyRestConfig::class])
class AppConfig {
    @Bean(name = ASYNC_EXECUTOR_NAME)
    fun asyncExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        // for passing in request scope context
        executor.setTaskDecorator(ContextCopyingDecorator())
        executor.setCorePoolSize(3)
        executor.setMaxPoolSize(5)
        executor.setQueueCapacity(100)
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.setThreadNamePrefix("AsyncThread-")
        executor.initialize()
        return executor
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    companion object {
        const val ASYNC_EXECUTOR_NAME = "asyncExecutor"
    }
}