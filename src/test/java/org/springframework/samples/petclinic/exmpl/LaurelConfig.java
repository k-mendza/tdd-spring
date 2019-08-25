package org.springframework.samples.petclinic.exmpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LaurelConfig {

    @Bean
    LaurelWordProducer laurelWordProducer() {
        return new LaurelWordProducer();
    }
}
