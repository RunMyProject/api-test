package com.esabatini.service.test.integration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.hasItem;

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
import com.esabatini.model.Product;
import com.esabatini.repository.ProductRepository;
import com.esabatini.service.ProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProductControllerIntegrationTest {

	private Logger logger = LoggerFactory.getLogger(ProductControllerIntegrationTest.class);

    private MockMvc mockMvc;

    @Inject 
    private WebApplicationContext wac;

    @Mock
    private ProductRepository mockProductRepository;
    
    @InjectMocks
    private ProductService productService;

    @Inject
    private ProductRepository injectProductRepository;

    @Before
    public void initializeMocks() {
        // Mocking Service
        //
    	mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
        // MockitoAnnotations.initMocks(this);
    	
    	// Standalone context
    	// this.mockMvc = standaloneSetup(this.productController).build();
    }
    
    private Optional<Product> getFirstOptionalProduct() {

    	List<Product> products = injectProductRepository.findAll();
    	Product product = !products.isEmpty() ? products.get(0) : null;
    	
    	logger.info("FIRST Product ID = " +product.getId());
    	// Optional<Product> productOptional = Optional.of(product);

    	Optional<Product> optionalProduct = injectProductRepository.findById(product.getId());
    	optionalProduct.ifPresent(productValue -> {
    	    logger.info("First Product's title = " + productValue.getTitle());
    	});

    	return optionalProduct;
    }
    
    @Test
    @Order(1)
    public void test1_FindByTitle() throws Exception {

    	String title = "title1";
    	
    	List<Product> products = injectProductRepository.findByTitle(title);
    	
        // Mocking Service
        //
        when(productService.findByTitle(title)).thenReturn(products);

        mockMvc.perform(get("/api/product/findByTitle/"+title)
        	.contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isFound())
            .andDo(print())
            .andExpect(jsonPath("$.*.title",  hasItem(is(title))))
            .andExpect(jsonPath("$[0].title", is(title)));
    }

    @Test
    @Order(2)
    public void test2_Save() throws Exception {

        Product product1 = Product.builder()
    		.title("product 1")
    		.price(7.77)
    		.build();
        
        // Mocking Service
        //
        when(productService.save(any(Product.class))).thenReturn(any(Product.class));
        
        mockMvc.perform(post("/api/product/save")
                .content(Setup.OM.writeValueAsString(product1))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is(product1.getTitle())))
                .andExpect(jsonPath("$.price", is(product1.getPrice())));

        // Mocking Service (another way)
        //
        Product product2 = Product.builder()
    		.title("product 2")
    		.price(20.20)
    		.build();
        
        // when
        mockProductRepository.save(product2);

        mockMvc.perform(post("/api/product/save")
            .content(Setup.OM.writeValueAsString(product2))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title", is(product2.getTitle())))
            .andExpect(jsonPath("$.price", is(product2.getPrice())));
        
        // then
        verify(mockProductRepository, times(1)).save(product2);
    }
    
    @Test
    @Order(3)
    public void test3_FindById() throws Exception {

    	Optional<Product> optionalProduct = getFirstOptionalProduct();
    	String productId = optionalProduct.get().getId();
    	
        // Mocking Service
        //
        when(productService.findById(productId)).thenReturn(optionalProduct);

        mockMvc.perform(get("/api/product/findById/"+productId)
        	.contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isFound())
            .andExpect(jsonPath("$.title", is("title1")))
            .andDo(print());
    }

    @Test
    @Order(4)
    public void test4_FindByAll() throws Exception {

    	List<Product> products = injectProductRepository.findAll();

        // Mocking Service
        //
        when(productService.findAll()).thenReturn(products);

        mockMvc.perform(get("/api/product/findAll")
        	.contentType(MediaType.APPLICATION_JSON))
        	.andExpect(content().contentType("application/json;charset=UTF-8"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
            .andExpect(MockMvcResultMatchers.jsonPath("$",is(not(empty()))))
        	.andDo(print());
    }
    
    @Test
    @Order(5)
    public void test5_DeleteById() throws Exception {

    	Optional<Product> optionalProduct = getFirstOptionalProduct();
    	String productId = optionalProduct.get().getId();
    	
        // Mocking Service
        //
        when(productService.findById(productId))
	        .thenReturn(optionalProduct)
	        .thenReturn(null);

        // when
        mockProductRepository.deleteById(productId);
        
        mockMvc.perform(get("/api/product/deleteById/"+productId)
        	.contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
        
        // then
        verify(mockProductRepository, times(1)).deleteById(productId);
    }    
    
    @Test
    @Order(6)
    public void test6_DeleteAll() throws Exception {
    	
        // when
        mockProductRepository.deleteAll();
        
        mockMvc.perform(get("/api/product/deleteAll")
        	.contentType(MediaType.APPLICATION_JSON))
        	.andDo(print())
            .andExpect(status().isOk());
        
        // then
        verify(mockProductRepository, times(1)).deleteAll();
    }
    
}