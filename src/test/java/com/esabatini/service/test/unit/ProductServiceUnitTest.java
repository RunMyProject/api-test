package com.esabatini.service.test.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;
import static org.assertj.core.api.Assertions.assertThat;

import com.esabatini.model.Product;
import com.esabatini.repository.MongodbDumpRunner;
import com.esabatini.service.ProductService;
import com.esabatini.utils.JsonUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceUnitTest {

    @Autowired
    private ProductService productService;

    // MockBean is the annotation provided by Spring that wraps mockito one
    // Annotation that can be used to add mocks to a Spring ApplicationContext.
    // If any existing single bean of the same type defined 
    // in the context will be replaced by the mock, 
    // if no existing bean is defined a new one will be added.
    //
    @MockBean
    private RestTemplate template;

    @SuppressWarnings("unchecked")
	@Test
    public void testGetProductsByTitle() throws IOException {

    	// Parsing mock file
    	//
        Product product = JsonUtils.jsonFile2Object("product.json", Product.class);

        // Mocking remote service
        //
        when(template.getForEntity(any(String.class), any(Class.class)))
        .thenReturn(new ResponseEntity(product, HttpStatus.OK));

        // I search for a product with title 'title1' 
        // but system will use mocked response containing only the title 'title1'
        // so I can check that mock is used.
        //
        List<Product> productsByTitle = productService.findByTitle(MongodbDumpRunner.PRODUCT1_TITLE);
        
        assertThat(productsByTitle).isNotNull()
            .isNotEmpty()
            .allMatch(p -> p.getTitle().contains(product.getTitle()));
    }

}
