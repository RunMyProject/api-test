package com.esabatini.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.esabatini.Setup;
import com.esabatini.model.Order;
import com.esabatini.model.Product;
import com.esabatini.model.Status;
import com.esabatini.model.User;

@SpringBootApplication(scanBasePackages = { "com.esabatini" })
@EnableMongoRepositories ("com.esabatini.repository")
public class MongodbDumpRunner implements CommandLineRunner {

  public static final String USER1_FIRSTNAME = "Dylan";
  public static final String USER1_LASTNAME  = "Dog";
  public static final String USER2_FIRSTNAME = "James";
  public static final String USER2_LASTNAME  = "Gosling";
  public static final String USER3_FIRSTNAME = "Bob";
  public static final String USER3_LASTNAME  = "Smith";
  public static final String USER4_FIRSTNAME = "Alice";
  public static final String USER4_LASTNAME  = "Smith";

  public static final String PRODUCT1_TITLE  = "title1";
  public static final String PRODUCT2_TITLE  = "title2";
  public static final String PRODUCT3_TITLE  = "title3";

  Logger logger = LoggerFactory.getLogger(MongodbDumpRunner.class);

  @Inject
  private UserRepository userRepository;

  @Inject
  private ProductRepository productRepository;

  @Inject
  private OrderRepository orderRepository;

  public static void main(String[] args) {
    SpringApplication.run(MongodbDumpRunner.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
	
	/*
	 * 
	 * Settings to clear Mongo Collections
	 * 
	MongoOperations mongoOps = new MongoTemplate(new MongoClient(), "local");
	
	mongoOps.dropCollection("order");
	mongoOps.dropCollection("product");
	mongoOps.dropCollection("user");

	mongoOps.createCollection("user");
	mongoOps.createCollection("product");
	mongoOps.createCollection("order");
	*/
	
	// Clear all tables in Mongo DB
	//
	userRepository.deleteAll();
	productRepository.deleteAll();
	orderRepository.deleteAll();
	
    // save a couple of users
	//
	User u = userRepository.save(User.builder()
		.firstName(USER1_FIRSTNAME)
		.lastName(USER1_LASTNAME)
		.build());

	logger.info("User => " + userRepository.findById(u.getId()).get());
	
	userRepository.save(User.builder()
		.firstName(USER2_FIRSTNAME)
		.lastName(USER2_LASTNAME)
		.build());

    // fetch all users
	//
	logger.info("Users found with findAll():");
	logger.info("-------------------------------");
    for (User user : userRepository.findAll()) {
      logger.info(user.toString());
    }
	logger.info("-------------------------------");

    // fetch an individual user
    //
	logger.info("User found with findByFirstName('" + USER1_FIRSTNAME + "'):");
	logger.info("-------------------------------");
	logger.info(userRepository.findByFirstName(USER1_FIRSTNAME).toString());

	// test same lastname
	//
	userRepository.save(User.builder()
			.firstName(USER3_FIRSTNAME)
			.lastName(USER3_LASTNAME)
			.build());
	userRepository.save(User.builder()
			.firstName(USER4_FIRSTNAME)
			.lastName(USER4_LASTNAME)
			.build());

	
	logger.info("Users found with findByLastName('" + USER3_LASTNAME + "'):");
	logger.info("--------------------------------");
    for (User user : userRepository.findByLastName(USER3_LASTNAME)) {
    	logger.info(user.toString());
    }
    
    // save a couple of products
    //
	Product p1 = productRepository.save(Product.builder()
			.id("00001")
			.title(PRODUCT1_TITLE)
			.price(12.3)
			.build());
	Product p2 = productRepository.save(Product.builder()
			.id("00002")
			.title(PRODUCT2_TITLE)
			.price(2.22)
			.build());
	Product p3 = productRepository.save(Product.builder()
			.id("00003")
			.title(PRODUCT3_TITLE)
			.price(3333.3333)
			.build());

    // fetch all products
	//
	logger.info("Products found with findAll():");
    logger.info("--------------------------------");
    for (Product product : productRepository.findAll()) {
        logger.info(product.toString());
    }
    logger.info("--------------------------------");
    
    // fetch an individual product
    //
    logger.info("Product found with findByTitle('title1'):");
    logger.info("--------------------------------");
    logger.info(productRepository.findByTitle("title1").toString());

    logger.info("Product found with findByTitle('title1'):");
    logger.info("--------------------------------");
    for (Product product : productRepository.findByTitle("title1")) {
    	logger.info(product.toString());
    }
    
	List<Product> listProduct = productRepository.findByTitle("title1");
	User user = userRepository.findByFirstName(USER1_FIRSTNAME);
    
    // save an order
	//
    orderRepository.save(Order.builder()
		.title("Order 1")
		.amount(200.3)
		.date(LocalDate.now().format(Setup.DATE_TIME_FORMATTER))
		.products(listProduct)
		.id_user(user.getId())
		.status(Status.Closed)
		.closedAt(LocalDate.now().plusMonths(1).format(Setup.DATE_TIME_FORMATTER))
		.build());

    // test for delete products from order
    //
	List<Product> products2 = new ArrayList<>();
	products2.add(p1);
	products2.add(p2);
	products2.add(p3);

	Double amount = 0D;
	for(Product p: products2) {
		amount+=p.getPrice();
	}
	
    // save an order
	//    
    orderRepository.save(Order.builder()
		.title("New Order To Del Products")
		.amount(amount)
		.date(LocalDate.now().format(Setup.DATE_TIME_FORMATTER))
		.products(products2)
		.id_user(user.getId())
		.status(Status.Closed)
		.closedAt(LocalDate.now().plusMonths(1).format(Setup.DATE_TIME_FORMATTER))
		.build());

    // fetch all orders
    //
    logger.info("Orders found with findAll():");
    logger.info("--------------------------------");
    for (Order order : orderRepository.findAll()) {
    	logger.info(order.toString());
    }
    logger.info("--------------------------------");
    
    // fetch an individual order
    //
    logger.info("Order found with findByTitle('Order 1'):");
    logger.info("--------------------------------");
    logger.info(orderRepository.findByTitle("Order 1").toString());

    logger.info("Order found with findByTitle('Order 1'):");
    logger.info("--------------------------------");
    for (Order order : orderRepository.findByTitle("Order 1")) {
    	logger.info(order.toString());
    }
    
    logger.info("*** [END MONGO DB DUMP] --------------------------------");
  }
}