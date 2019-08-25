package org.springframework.samples.petclinic.exmpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BaseConfig {

    @Bean
    HearingInterpreter hearingInterpreter(WordProducer wordProducer){
        return new HearingInterpreter(wordProducer);
    }
}