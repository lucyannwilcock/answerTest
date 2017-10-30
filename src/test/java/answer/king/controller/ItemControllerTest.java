package answer.king.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;

import answer.king.Application;
import answer.king.model.Item;

import java.io.IOException;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ItemControllerTest {

	private MockMvc mockMvc;
	
    @Autowired
    private WebApplicationContext webApplicationContext;
	
   @Before
    public void setup() throws Exception {
    	mockMvc = webAppContextSetup(webApplicationContext).dispatchOptions(true).build();
    }

	@Test
	public void testGetAll() {
		try {
			this.mockMvc.perform(get("/item"))
			.andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[0].id", is(1)))
            .andExpect(jsonPath("$.[0].price", is(45.00)))
			.andExpect(jsonPath("$.[0].name", is("A pre-populated item")));
		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	@Test
	public void testCreate() {
		Item newItem = new Item("A New Item", BigDecimal.TEN);
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(newItem);
			
			this.mockMvc.perform(post("/item")
			        .contentType(MediaType.APPLICATION_JSON_UTF8)
			        .content(jsonInString))
			        .andExpect(status().isOk())
			        .andExpect(jsonPath("$.name", is("A New Item")))
			        .andExpect(jsonPath("$.price", is(10)));
			
		} catch (IOException e) {
			fail("IOException: ");
			e.printStackTrace();
		} 
		catch (Exception e) {
			fail("Exception: ");
			e.getMessage();
			e.getStackTrace();
		}
	}

	@Test
	public void testCreate_NoName() {
		Item newItem = new Item();
		newItem.setPrice(new BigDecimal(45));

		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(newItem);

			this.mockMvc.perform(post("/item")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(jsonInString));
			
			fail("Item should not be created without a name");
		}
		catch (NestedServletException e) {
			assertTrue(e.getCause().getLocalizedMessage().contains("ConstraintViolationImpl{interpolatedMessage='may not be null', propertyPath=name"));
		}
		catch (Exception e) {
			fail("Exception: ");
			e.getMessage();
			e.getStackTrace();
		}
	}
	
	@Test
	public void testCreate_NoPrice() {
		Item newItem = new Item();
		newItem.setName("Should not be saved");

		try {
			ObjectMapper mapper = new ObjectMapper();
			String jsonInString = mapper.writeValueAsString(newItem);

			this.mockMvc.perform(post("/item")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(jsonInString));
			
			fail("Item should not be created without a price");
		}
		catch (NestedServletException e) {
			assertTrue(e.getCause().getLocalizedMessage().contains("ConstraintViolationImpl{interpolatedMessage='may not be null', propertyPath=price"));
		}
		catch (Exception e) {
			fail("Exception: ");
			e.getMessage();
			e.getStackTrace();
		}
	}
	
	@Test
	public void testChangePrice() {
		try {
			this.mockMvc.perform(put("/item/1/changePrice/0"))
			        .andExpect(status().isOk())
			        .andExpect(jsonPath("$.price", is(0)));
			
		} catch (IOException e) {
			fail("IOException: ");
			e.printStackTrace();
		} 
		catch (Exception e) {
			fail("Exception: ");
			e.getMessage();
			e.getStackTrace();
		}
	}

}
