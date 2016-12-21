package com.johnremboo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.Assert.assertEquals;

import com.johnremboo.entities.Contact;
import com.johnremboo.repositories.ContactsRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigInteger;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class HelloBackEndApplicationTests {
    private static boolean setupIsDone = false;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ContactsRepository contactsRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        if (setupIsDone) return;

        Contact c = new Contact();
        c.setId(new BigInteger("1"));
        c.setName("igor");
        contactsRepository.addContact(c);

        Contact tural = new Contact();
        tural.setId(new BigInteger("2"));
        tural.setName("tural");
        contactsRepository.addContact(tural);

        Contact farid = new Contact();
        farid.setId(new BigInteger("3"));
        farid.setName("farid");
        contactsRepository.addContact(farid);

        Contact emin = new Contact();
        emin.setId(new BigInteger("4"));
        emin.setName("emin");
        contactsRepository.addContact(emin);

        setupIsDone = true;
    }

    @Test
    public void findByRegexTural() {
        List contacts = contactsRepository.findByNameRegex("^.*[o].*$");
        Contact contact = (Contact) contacts.get(0);

        assertEquals("tural", contact.getName());
    }

    @Test
    public void findByRegexIgorEmin() {
        List contacts = contactsRepository.findByNameRegex("^.*[a].*$");
        Contact c1 = (Contact) contacts.get(0);
        Contact c2 = (Contact) contacts.get(1);

        assertEquals("igor", c1.getName());
        assertEquals(new BigInteger("1"), c1.getId());
        assertEquals("emin", c2.getName());
        assertEquals(new BigInteger("4"), c2.getId());
    }

    @Test
    public void contactsControllerOkResponseTest() throws Exception {
        String urlContactNamesByRegex = "/contacts?nameFilter=^.*[i].*$";

        mockMvc.perform(get(urlContactNamesByRegex))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(content().json("[{\"id\":2,\"name\":\"tural\"}]", true))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void contactsControllerNotFoundResponseTest() throws Exception {
        String urlContactNamesByRegex = "/contacts?nameFilter=^.*[aei].*$";

        mockMvc.perform(get(urlContactNamesByRegex))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
    }
}
