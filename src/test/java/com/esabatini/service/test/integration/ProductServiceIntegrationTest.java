package com.esabatini.service.test.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.esabatini.model.Product;
import com.esabatini.repository.MongodbDumpRunner;
import com.esabatini.repository.ProductRepository;
import com.esabatini.service.ProductService;
import com.esabatini.utils.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceIntegrationTest {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @SuppressWarnings("unused")
	private MockMvc mockMvc;

    @Before
    public void initializeMocks() throws IOException {

    	// Standalone context
    	//
    	mockMvc = standaloneSetup(productService).build();

        Product product = JsonUtils.jsonFile2Object("product.json", Product.class);
    	productRepository.save(product);
    }

    @Test
    public void testGetProductsByTitle() {
    	
	    List<Product> productsByTitle = productService.findByTitle(MongodbDumpRunner.PRODUCT1_TITLE);
	    
	    assertThat(productsByTitle).isNotNull().isNotEmpty();
    }

}
