package com.neo;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neo.controller.UserController;
import com.neo.model.User;
import com.neo.repository.UserRepository;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@WebMvcTest(value=UserController.class)

public class UserRegistrationApplicationTests {
    
    @Autowired
    MockMvc mockMvc;
	
    @Autowired
    ObjectMapper mapper;  
    //ObjectMapper provides functionality for reading and writing JSON,either to and from basic POJOs
    
    @MockBean
    UserRepository userRepository;
    
   User USER_1 = new User(1,"Pavithra", "Umesh","Bangalore", "560003",new Date(2000-1-25),new Date(2021-8-11),0);
   User USER_2 = new User(2,"Akash", "Gowda","Mandya","500067",new Date(2001-5-26),new Date(2020-6-30),0);
   User USER_3 = new User(3,"Kiran", "Sai","Sira","500093", new Date(1997-4-27),new Date(2019-3-16),0);
    
    @Test
	void contextLoads() {
	}
    
    @Test
	public void getAllUsers_success() throws Exception {
		 List<User> records = new ArrayList<>(Arrays.asList(USER_1, USER_2, USER_3));
		 
		 Mockito.when(userRepository.findAll()).thenReturn(records);
		 
		 mockMvc.perform(MockMvcRequestBuilders
	                .get("/user")
	                .contentType(MediaType.APPLICATION_JSON))
	                .andExpect(status().isOk()) //200
	                .andExpect(jsonPath("$", hasSize(3)))
	                .andExpect(jsonPath("$[1].firstName", is("Akash")));
		
	}
	
    
    @Test
	public void getUserById_success() throws Exception{
		Mockito.when(userRepository.findByUserId(USER_1.getUserId()))
    	.thenReturn(Optional.of(USER_1));
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/user/getUserById/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$.firstname", is("Pavithra")));
	}
    
    @Test
	public void getUserById_recordNotFound() throws Exception{
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/user/getUserById/25")
				.contentType(MediaType.ALL))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}
    
    @Test
	public void getByFirstName_success() throws Exception{
		List<User> records = new ArrayList<>(Arrays.asList(USER_1));
		
		Mockito.when(userRepository.findByFirstName(USER_1.getFirstName()))
			.thenReturn(records);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/user/getByFirstName/Pavithra")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[0].firstName", is("Pavithra")));
	}
    
    @Test
	public void getBySurname_success() throws Exception{
		List<User> records = new ArrayList<>(Arrays.asList(USER_1));
		
		Mockito.when(userRepository.findBySurname(USER_1.getSurname()))
			.thenReturn(records);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/user/getBySurname/Umesh")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[0].surname", is("Umesh")));
	}
	
	@Test
	public void getByPinCode_success() throws Exception{
		List<User> records = new ArrayList<>(Arrays.asList(USER_1));
		
		Mockito.when(userRepository.findByPinCode(USER_1.getPinCode()))
			.thenReturn(records);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/user/getByPinCode/560003")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", notNullValue()))
				.andExpect(jsonPath("$[0].pincode", is(560003)));
	}
	
	@Test
	public void findByOrderByDojAsc_success() throws Exception{
		List<User> records = new ArrayList<>(Arrays.asList(USER_3,USER_2, USER_1));
		
		Mockito.when(userRepository.findByOrderByDojAsc()).thenReturn(records);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/user/sortByDoj")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("[0].firstname", is("Kiran")));
	}
	
	@Test
	public void findByOrderByDobAsc_success() throws Exception{
		List<User> records = new ArrayList<>(Arrays.asList(USER_3,USER_2, USER_1));
		
		Mockito.when(userRepository.findByOrderByDobAsc()).thenReturn(records);
		
		mockMvc.perform(MockMvcRequestBuilders
				.get("/user/sortByDob")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("[0].firstname", is("Kiran")));
	}
    
    @Test
    public void createUser_success() throws Exception {
        User newUser = User.builder()
           	   .firstName("Pavithra")
                   .surname("Umesh")
                   .address("Bangalore")
                   .pinCode(560003)
                   .dob(2000-1-25)
                   .doj(2021-8-11)
                   .build();

        
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(newUser));
		
		mockMvc.perform(mockRequest)
		.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", notNullValue()))
			.andExpect(jsonPath("$.firstname", is("Akash")));
	}
    
    @Test
	public void createUser_emptyRequestBody() throws Exception{
		User newUser = null;
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(newUser));
		
		mockMvc.perform(mockRequest)
			.andDo(print())
			.andExpect(status().isBadRequest());
	}
	
    @Test
	public void updateUserRecord_success() throws Exception{
		User updatedUser = User.builder()
		 .userId(1)
         	 .firstName("Pavithra")
                 .surname("Umesh")
                 .address("Bangalore")
                 .pinCode(560003)
                 .dob(2000-1-25)
                 .doj(2021-8-11)
                 .build();
				
		Mockito.when(userRepository.findById(USER_1.getUserId()))
        .thenReturn(Optional.of(USER_1));
        
        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/user")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(updatedUser));
		
		mockMvc.perform(mockRequest)
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", notNullValue()))
			.andExpect(jsonPath("$.city", is("Bangalore")))
			.andExpect(jsonPath("$.dob", is(2000-1-25)));
	}
    
    @Test
	public void updateUserRecord_nullId() throws Exception{
		User updatedUser = User.builder()
                .firstName("Pavithra")
                .surname("Umesh")
                .address("Bangalore")
                .pinCode(560003)
                .dob(2000-1-25)
                .doj(2021-8-11)
                .build();

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedUser));

        mockMvc.perform(mockRequest)
        		.andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                    assertTrue(result.getResolvedException() 
                    		instanceof InvalidRequestException))
                .andExpect(result ->
                	assertEquals("User record or id must not be null.", 
            		result.getResolvedException().getMessage()));
	}
	
    @Test
    public void updateUserRecord_recordNotFound() throws Exception {
		User updatedUser = User.builder()
		.userId(10)
        	.firstName("Varun")
                .surname("Vamshi")
                .address("Tumkur")
                .pinCode(555555)
                .dob(1989-6-27)
                .doj(2010-12-29)
                .build();      


        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(updatedUser));

        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                    assertTrue(result.getResolvedException() 
                    		instanceof InvalidRequestException))
                .andExpect(result ->
                	assertEquals("User with ID 10 not found.", 
                			result.getResolvedException().getMessage()));
    }
	    	 
      
    @Test
    public void deleteUserById_success() throws Exception {
        
    	Mockito.when(userRepository.findById(USER_2.getUserId()))
        .thenReturn(Optional.of(USER_2));
    	
        mockMvc.perform(MockMvcRequestBuilders
                .delete("/user/deleteUser/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());              
    }

    @Test
    public void deleteUserById_notFound() throws Exception {       

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/user/deleteUser/15")
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() 
                        		instanceof InvalidRequestException))
                .andExpect(result ->
                assertEquals("User with ID 15 does not exist.", 
                		result.getResolvedException().getMessage()));
    }
}

    
