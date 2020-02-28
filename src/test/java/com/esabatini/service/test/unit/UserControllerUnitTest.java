package com.esabatini.service.test.unit;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.esabatini.Setup;
import com.esabatini.model.User;
import com.esabatini.repository.MongodbDumpRunner;
import com.esabatini.repository.UserRepository;
import com.esabatini.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerUnitTest {

	private Logger logger = LoggerFactory.getLogger(UserControllerUnitTest.class);

    private MockMvc mockMvc;

    @Inject 
    private WebApplicationContext wac;

    @Mock
    private UserRepository mockUserRepository;
    
    @InjectMocks
    private UserService userService;

    @Inject
    private UserRepository injectUserRepository;

    @Before
    public void initializeMocks() {
        // Mocking Service
        //
    	mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        // MockitoAnnotations.initMocks(this);
    }
    
    private Optional<User> getFirstOptionalUser() {

    	List<User> users = injectUserRepository.findAll();
    	User user = !users.isEmpty() ? users.get(0) : null;
    	
    	logger.info("FIRST User ID = " + user.getId());
    	// Optional<User> userOptional = Optional.of(user);

    	Optional<User> optionalUser = injectUserRepository.findById(user.getId());
    	optionalUser.ifPresent(userValue -> {
    	    logger.info("First User's firstName = " + userValue.getFirstName());
    	});

    	return optionalUser;
    }
    
    @Test
    @Order(1)
    public void test1_FindByFirstName() throws Exception {

    	String firstName = MongodbDumpRunner.USER1_FIRSTNAME;
    	
    	User user = injectUserRepository.findByFirstName(firstName);
    	
        // Mocking Service
        //
        when(userService.findByFirstName(firstName)).thenReturn(user);

        mockMvc.perform(get("/api/user/findByFirstName/"+firstName)
        	.contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isFound())
            .andDo(print())
            .andExpect(jsonPath("$.firstName", is(firstName)));
    }
    
    @Test
    @Order(2)
    public void test2_FindByLastName() throws Exception {

    	String lastName = MongodbDumpRunner.USER3_LASTNAME;
    	
    	List<User> users = injectUserRepository.findByLastName(lastName);
        
        // Mocking Service
        //    	
        when(userService.findByLastName(lastName)).thenReturn(users);

        mockMvc.perform(get("/api/user/findByLastName/"+lastName)
        	.contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$[0].lastName", is(lastName)))
            .andExpect(jsonPath("$[1].lastName", is(lastName)));
    }
    
    @Test
    @Order(3)
    public void test3_FindByAll() throws Exception {

    	List<User> users = injectUserRepository.findAll();

        // Mocking Service
        //
        when(userService.findAll()).thenReturn(users);

        mockMvc.perform(get("/api/user/findAll")
        	.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$",is(not(empty()))))
        	.andDo(print());
    }
    
    @Test
    @Order(4)
    public void test4_FindById() throws Exception {

    	String firstName = MongodbDumpRunner.USER1_FIRSTNAME;

    	Optional<User> optionalUser = getFirstOptionalUser();
    	String userId = optionalUser.get().getId();
    	
        // Mocking Service
        //
        when(userService.findById(userId)).thenReturn(optionalUser);

        mockMvc.perform(get("/api/user/findById/"+userId)
        	.contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isFound())
            .andExpect(jsonPath("$.firstName", is(firstName)))
            .andDo(print());
    }

    @Test
    @Order(5)
    public void test5_Save() throws Exception {

    	String firstName = MongodbDumpRunner.USER1_FIRSTNAME;
    	String lastName  = MongodbDumpRunner.USER1_LASTNAME;

        User user1 = User.builder()
    		.firstName(firstName)
    		.lastName(lastName)
    		.build();
        
        // Mocking Service
        //
        when(userService.save(any(User.class))).thenReturn(any(User.class));
        
        mockMvc.perform(post("/api/user/save")
                .content(Setup.OM.writeValueAsString(user1))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(user1.getFirstName())))
                .andExpect(jsonPath("$.lastName",  is(user1.getLastName())));

        // Mocking Service (another way)
        //
    	String firstName2 = MongodbDumpRunner.USER2_FIRSTNAME;
    	String lastName2  = MongodbDumpRunner.USER2_LASTNAME;

        User user2 = User.builder()
    		.firstName(firstName2)
    		.lastName(lastName2)
    		.build();
        
        // when
        mockUserRepository.save(user2);

        mockMvc.perform(post("/api/user/save")
            .content(Setup.OM.writeValueAsString(user2))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.firstName", is(user2.getFirstName())))
            .andExpect(jsonPath("$.lastName",  is(user2.getLastName())));
        
        // then
        verify(mockUserRepository, times(1)).save(user2);
    }
    
    @Test
    @Order(6)
    public void test6_DeleteById() throws Exception {

    	Optional<User> optionalUser = getFirstOptionalUser();
    	String userId = optionalUser.get().getId();
    	
        // Mocking Service
        //
        when(userService.findById(userId))
	        .thenReturn(optionalUser)
	        .thenReturn(null);

        // when
        mockUserRepository.deleteById(userId);
        
        mockMvc.perform(get("/api/user/deleteById/"+userId)
        	.contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
        
        // then
        verify(mockUserRepository, times(1)).deleteById(userId);
    }
    
    @Test
    @Order(7)
    public void test7_DeleteAll() throws Exception {
    	
        // when
        mockUserRepository.deleteAll();
        
        mockMvc.perform(get("/api/user/deleteAll")
        	.contentType(MediaType.APPLICATION_JSON))
        	.andDo(print())
            .andExpect(status()
            .isOk());
        
        // then
        verify(mockUserRepository, times(1)).deleteAll();
    }
    
}