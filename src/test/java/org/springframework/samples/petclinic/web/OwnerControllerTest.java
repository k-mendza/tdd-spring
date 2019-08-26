package org.springframework.samples.petclinic.web;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(locations =
        {"classpath:spring/mvc-test-config.xml",
        "classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {

    @Autowired
    OwnerController controller;
    
    @Autowired
    ClinicService service;

    private MockMvc mockMvc;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() {
        reset(service);
    }

    @Test
    void testProcessFindFormWithOneResult() throws Exception {
        Owner owner = new Owner();
        owner.setId(1);
        final String lastName = "findJustOne";
        owner.setLastName(lastName);

        given(service.findOwnerByLastName(lastName)).willReturn(Lists.newArrayList(owner));
        mockMvc.perform(get("/owners")
                .param("lastName", lastName))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));
        then(service).should().findOwnerByLastName(anyString());

    }

    @Test
    void testReturnListOfOwners() throws Exception {
        given(service.findOwnerByLastName("")).willReturn(Lists.newArrayList(new Owner(), new Owner()));
        mockMvc.perform(get("/owners"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/ownersList"));
        then(service).should().findOwnerByLastName(stringArgumentCaptor.capture());

        assertThat(stringArgumentCaptor.getValue()).isEqualToIgnoringCase("");
    }

    @Test
    void testInitCreationForm() throws Exception {
        mockMvc.perform(get("/owners/new"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("owner"))
                .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }

    @Test
    void testFindByNameNotFound() throws Exception {
        mockMvc.perform(get("/owners").param("lastName","Dont find me!"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }
}
