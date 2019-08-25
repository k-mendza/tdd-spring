package org.springframework.samples.petclinic.exmpl;

import org.springframework.stereotype.Component;

@Component
public class YannyWordProducer implements WordProducer {
    @Override
    public String getWord() {
        return "Yanny";
    }
}
