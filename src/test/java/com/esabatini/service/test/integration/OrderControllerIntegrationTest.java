package com.esabatini.service.test.integration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.time.LocalDate;
import java.util.ArrayList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.esabatini.ApiMessage;
import com.esabatini.Setup;
import com.esabatini.model.ControlCode;
import com.esabatini.model.Order;
import com.esabatini.model.OrderParams;
import com.esabatini.model.OrderProductParams;
import com.esabatini.model.OrderResponse;
import com.esabatini.model.Product;
import com.esabatini.model.Status;
import com.esabatini.model.User;
import com.esabatini.repository.MongodbDumpRunner;
import com.esabatini.repository.OrderRepository;
import com.esabatini.repository.ProductRepository;
import com.esabatini.repository.UserRepository;
import com.esabatini.service.OrderService;

@SpringBootTest
@RunWith(SpringRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class OrderControllerIntegrationTest {

    Logger logger = LoggerFactory.getLogger(OrderControllerIntegrationTest.class);

    private MockMvc mockMvc;

    @Inject
    private WebApplicationContext wac;

    @Mock
    private OrderRepository mockOrderRepository;

    @InjectMocks
    private OrderService orderService;

    @Inject
    private OrderService injectOrderService;
    
    @Inject
    private OrderRepository orderRepository;
    
    @Mock
    private List<Order> mockOrders;

    @Mock
    private Order mockOrder;

    @Mock
    private OrderResponse mockOrderResponse;

    @Mock
    private OrderParams mockOrderParams;
    
    @Mock
    private OrderProductParams mockOrderProductParams;

    @Mock
    private Product mockProduct;

    @Inject
    private ProductRepository productRepository;
    
    @Inject
    private UserRepository userRepository;

    @Before
    public void setup() throws Exception {
    
       // Standalone context
       //
       mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    private Optional<Order> getFirstOptionalOrderOpened() {

    	List<Order> orders = orderRepository.findByStatus(Status.Open);
    	Order order = !orders.isEmpty() ? orders.get(0) : null;
    	
    	if(order==null) return null;
    	
    	logger.info("FIRST Order ID = " + order.getId());
    	// Optional<Order> orderOptional = Optional.of(order);

    	Optional<Order> optionalOrder = orderRepository.findById(order.getId());
    	optionalOrder.ifPresent(orderValue -> {
    	    logger.info("First Order's title = " + orderValue.getTitle());
    	});

    	return optionalOrder;
    }

    private Optional<Order> getFirstOptionalOrderConfirmed() {

    	List<Order> orders = orderRepository.findByStatus(Status.Confirmed);
    	Order order = !orders.isEmpty() ? orders.get(0) : null;
    	
    	if(order==null) return null;
    	
    	logger.info("FIRST Order ID = " + order.getId());
    	// Optional<Order> orderOptional = Optional.of(order);

    	Optional<Order> optionalOrder = orderRepository.findById(order.getId());
    	optionalOrder.ifPresent(orderValue -> {
    	    logger.info("First Order's title = " + orderValue.getTitle());
    	});

    	return optionalOrder;
    }

    private Optional<Order> getFirstOptionalOrderClosed() {

    	List<Order> orders = orderRepository.findByStatus(Status.Closed);
    	Order order = !orders.isEmpty() ? orders.get(0) : null;
    	
    	if(order==null) return null;
    	
    	logger.info("FIRST Order ID = " + order.getId());
    	// Optional<Order> orderOptional = Optional.of(order);

    	Optional<Order> optionalOrder = orderRepository.findById(order.getId());
    	optionalOrder.ifPresent(orderValue -> {
    	    logger.info("First Order's title = " + orderValue.getTitle());
    	});

    	return optionalOrder;
    }

    private Optional<Product> getFirstOptionalProduct() {

    	List<Product> products = productRepository.findAll();
    	Product product = !products.isEmpty() ? products.get(0) : null;
    	
    	if(product==null) {
    		product = Product.builder()
    		.id("00001")	
    		.title("product 1")
			.price(7.77)
			.build();
        	productRepository.save(product);
    	}
    	
    	logger.info("FIRST Product ID = " + product.getId());
    	// Optional<Product> productOptional = Optional.of(product);

    	Optional<Product> optionalProduct = productRepository.findById(product.getId());
    	optionalProduct.ifPresent(productValue -> {
    	    logger.info("First Product's title = " + productValue.getTitle());
    	});

    	return optionalProduct;
    }

    private Optional<Product> getSecondOptionalProduct() {

    	List<Product> products = productRepository.findAll();
    	Product product = (!products.isEmpty() && products.size()>1) ? products.get(1) : null;

    	if(product==null) {
    		product = Product.builder()
    		.id("00002")	
    		.title("product 2")
			.price(2.22)
			.build();
        	productRepository.save(product);
    	}

    	logger.info("SECOND Product ID = " + product.getId());

    	Optional<Product> optionalProduct = productRepository.findById(product.getId());
    	optionalProduct.ifPresent(productValue -> {
    	    logger.info("Second Product's title = " + productValue.getTitle());
    	});

    	return optionalProduct;
    }

    private Optional<Product> getThirdOptionalProduct() {

    	List<Product> products = productRepository.findAll();
    	Product product = (!products.isEmpty() && products.size()>2) ? products.get(2) : null;

    	if(product==null) {
    		product = Product.builder()
    		.id("00003")	
    		.title("product 3")
			.price(3.33)
			.build();
        	productRepository.save(product);
    	}

    	logger.info("THIRD Product ID = " + product.getId());

    	Optional<Product> optionalProduct = productRepository.findById(product.getId());
    	optionalProduct.ifPresent(productValue -> {
    	    logger.info("Third Product's title = " + productValue.getTitle());
    	});

    	return optionalProduct;
    }

    private Optional<User> getFirstOptionalUser() {

    	List<User> users = userRepository.findAll();
    	User user = !users.isEmpty() ? users.get(0) : null;

    	if(user==null) {
        	String firstName = MongodbDumpRunner.USER1_FIRSTNAME;
        	String lastName  = MongodbDumpRunner.USER1_LASTNAME;
            user = User.builder()
            	.id("00000001")
        		.firstName(firstName)
        		.lastName(lastName)
        		.build();
        	userRepository.save(user);
    	}

    	
    	logger.info("FIRST User ID = " + user.getId());
    	// Optional<User> userOptional = Optional.of(user);

    	Optional<User> optionalUser = userRepository.findById(user.getId());
    	optionalUser.ifPresent(userValue -> {
    	    logger.info("First User's firstName = " + userValue.getFirstName());
    	});

    	return optionalUser;
    }
    
    @Test
    public void test1_FindByTitle() throws Exception {

    	String title = "Order 1";
    	
        // Mocking service
        //
        when(orderService.findByTitle(any(String.class))).thenReturn(mockOrders);

        MvcResult result = mockMvc.perform(get("/api/order/findByTitle/"+title)
        	.contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(request().asyncStarted())
            .andDo(print())
            .andExpect(status().is2xxSuccessful()).andReturn();

        result.getRequest().getAsyncContext().setTimeout(100000);
        
        mockMvc.perform(asyncDispatch(result))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].title", is(title)));
    }
    
    @Test
    public void test2_CreateOrderSync() throws Exception {

    	String title = "New Order Sync";
    		
    	List<Product> products = new ArrayList<>();
    	Product product = getFirstOptionalProduct().get();
    	products.add(product);
    	
    	User user = getFirstOptionalUser().get();
    	    	
    	OrderParams orderParams = OrderParams.builder()
    			.title(title)
    			.products(products)
    			.user(user)
    			.build();
    	
    	// Double Mocking Service of 'when event condition'
        // any() and mock() are inverted!
    	//
        when(orderService.save(any(Order.class))).thenReturn(mock(Order.class));
        when(orderService.saveByOrderParams(mock(OrderParams.class))).thenReturn(any(OrderResponse.class));

        mockMvc.perform(post("/api/order/createOrderSync")
                .content(Setup.OM.writeValueAsString(orderParams))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.kook", is(ControlCode.OK.name())))
                .andExpect(jsonPath("$.order.title", is(title)))
                .andExpect(jsonPath("$.order.products[0].price", is(product.getPrice())))
                .andExpect(jsonPath("$.order.id_user", is(user.getId())));

        test7_CloseOrder();
        
        // Mocking Service (another way)
        // through a mock of an order saving
        //        
        String title2 = "Order 2° way test Sync";

    	OrderParams orderParams2 = OrderParams.builder()
				.title(title2)
				.products(products)
				.user(user)
				.build();
    	
        // when
        mockOrderRepository.save(mockOrder);
        
        mockMvc.perform(post("/api/order/createOrderSync")
                .content(Setup.OM.writeValueAsString(orderParams2))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.kook", is(ControlCode.OK.name())))
                .andExpect(jsonPath("$.order.title", is(title2)))
                .andExpect(jsonPath("$.order.products[0].price", is(product.getPrice())))
                .andExpect(jsonPath("$.order.id_user", is(user.getId())));
        
        // then
        verify(mockOrderRepository, times(1)).save(any(Order.class));

        test7_CloseOrder();
    }
    
    @Test
    public void test3_CreateOrder() throws Exception {

    	User user = getFirstOptionalUser().get();

    	String title3 = "New Order Async";

    	List<Product> products = new ArrayList<>();
    	Product product = getFirstOptionalProduct().get();
    	products.add(product);
    	    	
    	OrderParams orderParams3 = OrderParams.builder()
				.title(title3)
				.products(products)
				.user(user)
				.build();
    	
        mockOrderRepository.save(mock(Order.class));
        
    	// Double Mocking Service of 'when event condition'
        // any() and mock() are inverted!
    	//
        when(orderService.save(any(Order.class))).thenReturn(mock(Order.class));
        when(orderService.saveByOrderParams(orderParams3)).thenReturn(mockOrderResponse);

        MvcResult mvcResult = mockMvc.perform(post("/api/order/createOrder")
                .content(Setup.OM.writeValueAsString(orderParams3))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(request().asyncStarted())
                .andDo(print())
                .andExpect(status().is2xxSuccessful()).andReturn();
                
        mvcResult.getRequest().getAsyncContext().setTimeout(100000);
        
        mockMvc.perform(asyncDispatch(mvcResult))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.kook", is(ControlCode.OK.name())))
            .andExpect(jsonPath("$.order.title", is(title3)))
            .andExpect(jsonPath("$.order.products[0].price", is(product.getPrice())))
            .andExpect(jsonPath("$.order.id_user", is(user.getId())));
    }

    @Test
    public void test4_CreateOrderWithoutAnyProduct() throws Exception {

    	User user = getFirstOptionalUser().get();

    	String title4 = "Order Without Any Product";

    	List<Product> products4 = new ArrayList<>();
    	// products3.add(product4);

        OrderParams orderParams4 = OrderParams.builder()
				.title(title4)
				.products(products4)
				.user(user)
				.build();
    	
        mockOrderRepository.save(mock(Order.class));
        
    	// Double Mocking Service of 'when event condition'
        // any() and mock() are inverted!
    	//
        when(orderService.save(any(Order.class))).thenReturn(mock(Order.class));
        when(orderService.saveByOrderParams(mockOrderParams)).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(post("/api/order/createOrder")
                .content(Setup.OM.writeValueAsString(orderParams4))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(request().asyncStarted())
                .andDo(print())
                .andExpect(status().is2xxSuccessful()).andReturn();
                
        mvcResult.getRequest().getAsyncContext().setTimeout(100000);
        
        mockMvc.perform(asyncDispatch(mvcResult))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.kook", is(ControlCode.KO.name())))
            .andExpect(jsonPath("$.message", is("missing product")));
    }
    
    @Test
    public void test5_CreateOrderWithoutTitle() throws Exception {

    	User user = getFirstOptionalUser().get();

    	String title5 = null;

    	List<Product> products5 = new ArrayList<>();
        Product product5 = Product.builder()
        		.id("id FAKE")
	    		.title("Fake product")
	    		.price(0D)
	    		.build();
    	products5.add(product5);
    	
        OrderParams orderParams3 = OrderParams.builder()
				.title(title5)
				.products(products5)
				.user(user)
				.build();
    	
        mockOrderRepository.save(mock(Order.class));
        
    	// Double Mocking Service of 'when event condition'
        // any() and mock() are inverted!
    	//
        when(orderService.save(any(Order.class))).thenReturn(mock(Order.class));
        when(orderService.saveByOrderParams(mockOrderParams)).thenReturn(null);

        MvcResult mvcResult = mockMvc.perform(post("/api/order/createOrder")
                .content(Setup.OM.writeValueAsString(orderParams3))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(request().asyncStarted())
                .andDo(print())
                .andExpect(status().is2xxSuccessful()).andReturn();
                
        mvcResult.getRequest().getAsyncContext().setTimeout(100000);
        
        mockMvc.perform(asyncDispatch(mvcResult))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.kook", is(ControlCode.KO.name())))
            .andExpect(jsonPath("$.message", is("missing title")));
    }
    
    @Test
    public void test6_FindById_user() throws Exception {

    	User user = getFirstOptionalUser().get();
    	
        // Mocking service
        //
        when(orderService.findById_user(any(String.class))).thenReturn(mockOrders);

        MvcResult result = mockMvc.perform(get("/api/order/findById_user/"+user.getId())
        	.contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(request().asyncStarted())
            .andDo(print())
            .andExpect(status().is2xxSuccessful()).andReturn();

        result.getRequest().getAsyncContext().setTimeout(100000);
        
        mockMvc.perform(asyncDispatch(result))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id_user", is(user.getId())));
    }

	@Test
	public void test7_CloseOrder() throws Exception {
	
		Optional<Order> optionalOrder = getFirstOptionalOrderOpened();
		if(optionalOrder==null) return;
		
		// Mocking Service
		//
	    when(orderService.close(optionalOrder.get())).thenReturn(mock(Order.class));
	
	    MvcResult mvcResult = mockMvc.perform(post("/api/order/closeOrder")
	            .content(Setup.OM.writeValueAsString(optionalOrder.get()))
	            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
	            .andExpect(request().asyncStarted())
	            .andExpect(status().is2xxSuccessful()).andReturn();
	            
	    mvcResult.getRequest().getAsyncContext().setTimeout(100000);
	    
	    mockMvc.perform(asyncDispatch(mvcResult))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.status", is(Status.Closed.name())));
	}    

    @Test
    public void test8_AddProduct() throws Exception {
    	
    	User user = getFirstOptionalUser().get();

    	String title = "New Order To Add Products";

    	List<Product> products = new ArrayList<>();
    	Product product = getFirstOptionalProduct().get();
    	products.add(product);
    	    	
    	OrderParams orderParams = OrderParams.builder()
				.title(title)
				.products(products)
				.user(user)
				.build();
    	
        OrderResponse orderResponse = injectOrderService.saveByOrderParams(orderParams);
        Order order = orderResponse.getOrder();

    	List<Product> products2 = new ArrayList<>();
    	Product product2 = getSecondOptionalProduct().get();
    	products2.add(product2);
        
        OrderProductParams orderProductParams = OrderProductParams.builder()
       		.order(order)
    		.products(products2)
    		.build();
        
    	// Mocking Service
    	//
        when(orderService.addProduct(orderProductParams)).thenReturn(mock(OrderResponse.class));
        
    	List<Product> products3 = new ArrayList<>();
    	Product product3 = getThirdOptionalProduct().get();
    	products3.add(product3);
    	orderProductParams.setProducts(products3);

        MvcResult mvcResult = mockMvc.perform(post("/api/order/addProduct")
                .content(Setup.OM.writeValueAsString(orderProductParams))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andExpect(status().is2xxSuccessful()).andReturn();
                
        mvcResult.getRequest().getAsyncContext().setTimeout(100000);

        mockMvc.perform(asyncDispatch(mvcResult))
	        .andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.kook", is(ControlCode.OK.name())))
	        .andExpect(jsonPath("$.message", is(ApiMessage.PRODUCT_ADDED)))
	        .andExpect(jsonPath("$.order.title", is(order.getTitle())))
	        .andExpect(jsonPath("$.order.status", is(order.getStatus().name())))
	        .andExpect(jsonPath("$.order.products[2].price", is(product3.getPrice())))
	        .andExpect(jsonPath("$.order.id_user", notNullValue()));
        
        injectOrderService.close(order);
    }
    
    @Test
    public void test9_FailAddProduct() throws Exception {
    	
    	User user = getFirstOptionalUser().get();

    	String title = "New Order To Add Products";

    	List<Product> products = new ArrayList<>();
    	Product product = getFirstOptionalProduct().get();
    	products.add(product);
    	    	
    	OrderParams orderParams = OrderParams.builder()
				.title(title)
				.products(products)
				.user(user)
				.build();
    	
        OrderResponse orderResponse = injectOrderService.saveByOrderParams(orderParams);
        Order order = orderResponse.getOrder();

    	List<Product> products2 = new ArrayList<>();
    	Product product2 = getSecondOptionalProduct().get();
    	products2.add(product2);
        
        OrderProductParams orderProductParams = OrderProductParams.builder()
       		.order(order)
    		.products(products2)
    		.build();
        
    	// Mocking Service
    	//
        when(orderService.addProduct(orderProductParams)).thenReturn(mock(OrderResponse.class));
        
        MvcResult mvcResult = mockMvc.perform(post("/api/order/addProduct")
                .content(Setup.OM.writeValueAsString(orderProductParams))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andExpect(status().is2xxSuccessful()).andReturn();
                
        mvcResult.getRequest().getAsyncContext().setTimeout(100000);

        mockMvc.perform(asyncDispatch(mvcResult))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.kook", is(ControlCode.KO.name())))
        .andExpect(jsonPath("$.message", notNullValue()));
        
        injectOrderService.close(order);
    }
    
    @Test
    public void testA1_DelProduct() throws Exception {
    	
    	String title = "New Order To Del Products";
    	
     	List<Product> productsToDelete = new ArrayList<>();
     	
     	Product product1 = getFirstOptionalProduct().get();
     	Product product2 = getSecondOptionalProduct().get();
    	Product product3 = getThirdOptionalProduct().get();

    	productsToDelete.add(product1);
    	productsToDelete.add(product3);

    	List<Order> orders = orderRepository.findByTitle(title);
    	
    	Order order = !orders.isEmpty() ? orders.get(0) : null;
    	order.setStatus(Status.Open);
    	        
        OrderProductParams orderProductParams = OrderProductParams.builder()
       		.order(order)
    		.products(productsToDelete)
    		.build();
        
    	// Mocking Service
    	//
        when(orderService.delProduct(mockOrderProductParams)).thenReturn(null);
        
        MvcResult mvcResult = mockMvc.perform(post("/api/order/delProduct")
                .content(Setup.OM.writeValueAsString(orderProductParams))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(request().asyncStarted())
                .andExpect(status().is2xxSuccessful()).andReturn();
                
        mvcResult.getRequest().getAsyncContext().setTimeout(100000);

        mockMvc.perform(asyncDispatch(mvcResult))
	        .andDo(print())
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.kook", is(ControlCode.OK.name())))
	        .andExpect(jsonPath("$.message", notNullValue()))
	        .andExpect(jsonPath("$.order.title", is(order.getTitle())))
	        .andExpect(jsonPath("$.order.status", is(order.getStatus().name())))
	        .andExpect(jsonPath("$.order.products", hasSize(1)))
	        .andExpect(jsonPath("$.order.products[0].price", is(product2.getPrice())))
	        .andExpect(jsonPath("$.order.id_user", notNullValue()));
        
        injectOrderService.close(order);
    }

    @Test
    public void testA2_NextStepOrder_OpenToConfirmed() throws Exception {

    	User user = getFirstOptionalUser().get();

    	String title = "New Order Step";

    	List<Product> products = new ArrayList<>();
    	Product product = getFirstOptionalProduct().get();
    	products.add(product);
    	    	
    	OrderParams orderParams = OrderParams.builder()
				.title(title)
				.products(products)
				.user(user)
				.build();
    	
    	injectOrderService.saveByOrderParams(orderParams);

		Optional<Order> optionalOrder = getFirstOptionalOrderOpened();

        MvcResult result = mockMvc.perform(get("/api/order/nextStepOrder/"+optionalOrder.get().getId())
        	.contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(request().asyncStarted())
            .andDo(print())
            .andExpect(status().is2xxSuccessful()).andReturn();

        result.getRequest().getAsyncContext().setTimeout(100000);
        
        mockMvc.perform(asyncDispatch(result))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(Status.Confirmed.name())));
    }
    
    @Test
    public void testA3_NextStepOrder_ConfirmedToClosed() throws Exception {

		Optional<Order> optionalOrder = getFirstOptionalOrderConfirmed();
		
		if(optionalOrder==null) {
	    	User user = getFirstOptionalUser().get();

	    	String title = "New Order Step";

	    	List<Product> products = new ArrayList<>();
	    	Product product = getFirstOptionalProduct().get();
	    	products.add(product);
	    	    	
	    	OrderParams orderParams = OrderParams.builder()
					.title(title)
					.products(products)
					.user(user)
					.build();
	    	
	    	injectOrderService.saveByOrderParams(orderParams);

			Optional<Order> optionalOrder2 = getFirstOptionalOrderOpened();
			
	    	injectOrderService.nextStepOrder(optionalOrder2.get().getId());			
			
			optionalOrder = getFirstOptionalOrderConfirmed();
		}
    	
        MvcResult result = mockMvc.perform(get("/api/order/nextStepOrder/"+optionalOrder.get().getId())
        	.contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(request().asyncStarted())
            .andDo(print())
            .andExpect(status().is2xxSuccessful()).andReturn();

        result.getRequest().getAsyncContext().setTimeout(100000);
        
        mockMvc.perform(asyncDispatch(result))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(Status.Closed.name())));
    }

    @Test
    public void testA4_RefundOrder() throws Exception {

		Optional<Order> optionalOrder = getFirstOptionalOrderClosed();
		
		if(optionalOrder==null) {
	    	User user = getFirstOptionalUser().get();

	    	String title = "New Order Refund";

	    	List<Product> products = new ArrayList<>();
	    	Product product = getFirstOptionalProduct().get();
	    	products.add(product);
	    	    	
	    	OrderParams orderParams = OrderParams.builder()
					.title(title)
					.products(products)
					.user(user)
					.build();
	    	
	    	injectOrderService.saveByOrderParams(orderParams);
			Optional<Order> optionalOrder2 = getFirstOptionalOrderOpened();
			
	    	injectOrderService.nextStepOrder(optionalOrder2.get().getId());						
	    	Optional<Order> optionalOrder3 = getFirstOptionalOrderConfirmed();

	    	injectOrderService.nextStepOrder(optionalOrder3.get().getId());
			optionalOrder = getFirstOptionalOrderClosed();
		}
    	
        MvcResult result = mockMvc.perform(get("/api/order/refundOrder/"+optionalOrder.get().getId())
        	.contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(request().asyncStarted())
            .andDo(print())
            .andExpect(status().is2xxSuccessful()).andReturn();

        result.getRequest().getAsyncContext().setTimeout(100000);
        
        mockMvc.perform(asyncDispatch(result))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(Status.Refunded.name())));
    }
    
    @Test
    public void testA5_Fail_RefundOrder() throws Exception {

		Optional<Order> optionalOrder = getFirstOptionalOrderClosed();
		
		if(optionalOrder==null) {
	    	User user = getFirstOptionalUser().get();

	    	String title = "New Order Refund Fail";

	    	List<Product> products = new ArrayList<>();
	    	Product product = getFirstOptionalProduct().get();
	    	products.add(product);
	    	    	
	    	OrderParams orderParams = OrderParams.builder()
					.title(title)
					.products(products)
					.user(user)
					.build();
	    	
	    	injectOrderService.saveByOrderParams(orderParams);
			Optional<Order> optionalOrder2 = getFirstOptionalOrderOpened();
			
	    	injectOrderService.nextStepOrder(optionalOrder2.get().getId());						
	    	Optional<Order> optionalOrder3 = getFirstOptionalOrderConfirmed();

	    	injectOrderService.nextStepOrder(optionalOrder3.get().getId());
			optionalOrder = getFirstOptionalOrderClosed();
		}
		
		optionalOrder.get().setClosedAt(
			LocalDate.now()
			.minusMonths(1)
			.format(Setup.DATE_TIME_FORMATTER));
		
		orderRepository.save(optionalOrder.get());
    	
        MvcResult result = mockMvc.perform(get("/api/order/refundOrder/"+optionalOrder.get().getId())
        	.contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(request().asyncStarted())
            .andDo(print())
            .andExpect(status().is2xxSuccessful()).andReturn();

        result.getRequest().getAsyncContext().setTimeout(100000);
        
        mockMvc.perform(asyncDispatch(result))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(Status.Closed.name())));
    }
    
}