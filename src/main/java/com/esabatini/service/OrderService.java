package com.esabatini.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.esabatini.ApiMessage;
import com.esabatini.Setup;
import com.esabatini.model.ControlCode;
import com.esabatini.model.Order;
import com.esabatini.model.OrderParams;
import com.esabatini.model.OrderProductParams;
import com.esabatini.model.OrderResponse;
import com.esabatini.model.Product;
import com.esabatini.model.Status;
import com.esabatini.repository.OrderRepository;

@Service
public class OrderService {
	
	@Inject
	private OrderRepository orderRepository;
	
	public List<Order> findAll() {
        return orderRepository.findAll();
    }
	
    public List<Order> findByTitle(String title) {
        return orderRepository.findByTitle(title);
    }

    public List<Order> findById_user(String id_user) {
        return orderRepository.findById_user(id_user);
    }

    public Optional<Order> findById(String id) {
        return orderRepository.findById(id);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public OrderResponse saveByOrderParams(OrderParams orderParams) {
    	
    	OrderResponse orderResponseError = OrderResponse.builder()
   			.kook(ControlCode.KO)
   			.build();
    	
    	// Preconditions and Constraints to satisfy
    	//
    	if(orderParams==null)
    		return orderResponseError.changeMessage(ApiMessage.MISS_PARAMS);
    	if(orderParams.getProducts()==null || orderParams.getProducts().isEmpty()) 
    		return orderResponseError.changeMessage(ApiMessage.MISS_PRODUCT);
    	if(orderParams.getTitle()==null || orderParams.getTitle().isEmpty()) 
    		return orderResponseError.changeMessage(ApiMessage.MISS_TITLE);
    	if(orderParams.getUser()==null)
    		return orderResponseError.changeMessage(ApiMessage.MISS_USER);
    	
    	// Look-up for existing opened orders...
    	//
    	List<Order> orders = orderRepository.findByStatus(Status.Open);

    	if(!orders.isEmpty()) 
    		return orderResponseError.changeMessage(ApiMessage.ORDER_OPENED);
    	
    	// Check unique ID product in product list passed in parameters
    	//
    	List<Product> products = orderParams.getProducts();
    	
    	if(products.size()>1) {
	
	    	boolean found = false;
	    	String idProduct = "";
	    	
	    	for(Product product : products) {
	        	if(checkUniqueIdProduct(product, products)) {
	        		found = true;
	        		idProduct = product.getId();
	        		break;
	        	}
	    	}
	    	
	    	if(found) {
	    	    String message = String.format(ApiMessage.PRODUCT_UNIQUE, idProduct);
	    		return orderResponseError.changeMessage(message);
	    	}
    	}
    	
    	// I can save the Order
    	//
    	BigDecimal amount = new BigDecimal(0);
    	
    	for(Product product : products) 
    		amount.add(BigDecimal.valueOf(product.getPrice()));
    	
    	Order order = Order.builder()
    		.title(orderParams.getTitle())
    		.amount(amount.doubleValue())
    		.date(LocalDate.now().format(Setup.DATE_TIME_FORMATTER))
    		.products(products)
    		.id_user(orderParams.getUser().getId())
    		.status(Status.Open)
    		.build();
    	
    	Order newOrder = save(order);
    	
    	OrderResponse orderResponse = OrderResponse.builder()
			.kook(ControlCode.OK)
			.message(ApiMessage.ORDER_SUCCESS)
			.order(newOrder)
			.build();
    	
        return orderResponse;
    }
    
    public Order close(Order order) {
    	order.setClosedAt(LocalDate.now().format(Setup.DATE_TIME_FORMATTER));
    	order.setStatus(Status.Closed);
        return save(order);
    }

    private boolean checkUniqueIdProduct(Product product, List<Product> products) {
    	
    	boolean found = false;
    	
    	for(Product p : products) {
    		if(product.getId().equals(p.getId())) {
    			found = true;
    			break;
    		}
    	}
    	
    	return found;    	
    }
    
    public OrderResponse addProduct(OrderProductParams orderProductParams) {

    	OrderResponse orderResponseError = OrderResponse.builder()
       			.kook(ControlCode.KO)
       			.build();
        	
    	// Preconditions and Constraints to satisfy
    	//
    	if(orderProductParams==null)
    		return orderResponseError.changeMessage(ApiMessage.MISS_PARAMS);
    	if(orderProductParams.getProducts()==null || orderProductParams.getProducts().isEmpty())
    		return orderResponseError.changeMessage(ApiMessage.MISS_PRODUCT);
    	if(orderProductParams.getOrder()==null)
    		return orderResponseError.changeMessage(ApiMessage.MISS_ORDER);
    	    	
    	// Check if Order is open
    	//
    	if(!orderProductParams.getOrder().getStatus().equals(Status.Open))
    		return orderResponseError.changeMessage(ApiMessage.ORDER_NOT_OPENED);
    	
    	// Check unique ID product 
    	//
    	List<Product> products = orderProductParams.getProducts();

    	boolean found = false;
    	String idProduct = "";
    	
    	for(Product product : products) {
        	if(checkUniqueIdProduct(product, orderProductParams.getOrder().getProducts())) {
        		found = true;
        		idProduct = product.getId();
        		break;
        	}
    	}
    	
    	if(found) {
    	    String message = String.format(ApiMessage.PRODUCT_UNIQUE, idProduct);
    		return orderResponseError.changeMessage(message);
    	}
    		
    	// I can add the Products to Order
    	//
    	for(Product product : products) orderProductParams
			.getOrder()
			.getProducts()
			.add(product);

    	Order newOrder = save(orderProductParams.getOrder());
    	
    	OrderResponse orderResponse = OrderResponse.builder()
			.kook(ControlCode.OK)
			.message(ApiMessage.PRODUCT_ADDED)
			.order(newOrder)
			.build();
    	
        return orderResponse;
    }

    @SuppressWarnings("unlikely-arg-type")
	public OrderResponse delProduct(OrderProductParams orderProductParams) {

    	OrderResponse orderResponseError = OrderResponse.builder()
       			.kook(ControlCode.KO)
       			.build();
        	
    	// Preconditions and Constraints to satisfy
    	//
    	if(orderProductParams==null)
    		return orderResponseError.changeMessage(ApiMessage.MISS_PARAMS);
    	if(orderProductParams.getProducts()==null || orderProductParams.getProducts().isEmpty())
    		return orderResponseError.changeMessage(ApiMessage.MISS_PRODUCT);
    	if(orderProductParams.getOrder()==null)
    		return orderResponseError.changeMessage(ApiMessage.MISS_ORDER);
    	    	
    	// Check if Order is open
    	//
    	if(!orderProductParams.getOrder().getStatus().equals(Status.Open))
    		return orderResponseError.changeMessage(ApiMessage.ORDER_NOT_OPENED);

    	// Check if the product exists
    	//
    	List<Product> products = orderProductParams.getProducts();
    	List<Product> productsOfOrder = orderProductParams.getOrder().getProducts();

    	List<String> productsToDelete = new ArrayList<>();
    	
    	int originSize = productsOfOrder.size();
    	
    	for(Product product : products) {
        	if(checkUniqueIdProduct(product, orderProductParams.getOrder().getProducts())) {
        		productsToDelete.add(product.getId());
        	}
    	}
    	
    	if(productsToDelete.isEmpty())
    		return orderResponseError.changeMessage(ApiMessage.PRODUCT_NOT_FOUND);
    	
    	// I can delete the Products to Order
    	//
    	productsOfOrder.removeIf(product -> productsToDelete.contains(product.getId()));
    	
    	Order newOrder = save(orderProductParams.getOrder());
    	
    	int nowSize = productsOfOrder.size();

    	String message = String.format(ApiMessage.PRODUCT_DELETED, originSize - nowSize);

    	OrderResponse orderResponse = OrderResponse.builder()
			.kook(ControlCode.OK)
			.message(message)
			.order(newOrder)
			.build();
    	
        return orderResponse;
    }
    
    public void deleteById(String id) {
    	orderRepository.deleteById(id);
    }
        
    public void deleteAll() {
    	orderRepository.deleteAll();
    }
    
    public Order nextStepOrder(String id_order) {
    	
    	Optional<Order> optionalOrder = orderRepository.findById(id_order);
    	
    	switch(optionalOrder.get().getStatus()) {
	    	case Open: optionalOrder.get().setStatus(Status.Confirmed); break;
	    	case Confirmed: {
	    		return close(optionalOrder.get());
	    	}
		default:
			optionalOrder.get().setStatus(Status.Open);
			break;
    	}
    	
        return save(optionalOrder.get());
    }

    public Order refundOrder(String id_order) {
    	
    	Optional<Order> optionalOrder = orderRepository.findById(id_order);

    	LocalDate localDate = LocalDate.parse(optionalOrder.get().getClosedAt(), Setup.DATE_TIME_FORMATTER);
    	
    	if(LocalDate.now().isBefore(localDate.plusMonths(1)) &&     	
    	   optionalOrder.get().getStatus().equals(Status.Closed)) 
    		optionalOrder.get().setStatus(Status.Refunded);
    	
        return save(optionalOrder.get());
    }
        
}
