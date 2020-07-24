package com.example.demo;

import java.util.HashMap;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@RestController
public class DemoController {
	private static final Logger LOG = Logger.getLogger(DemoController.class.getName());
	private static Map<String, Product> productRepo = new HashMap<>();
	   static {
	      Product honey = new Product();
	      honey.setId("1");
	      honey.setName("Honey");
	      productRepo.put(honey.getId(), honey);
	      
	      Product almond = new Product();
	      almond.setId("2");
	      almond.setName("Almond");
	      productRepo.put(almond.getId(), almond);
	   }
	   
	   
	   
	   @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
	   public ResponseEntity<Object> delete(@PathVariable("id") String id) { 
	      productRepo.remove(id);
	      return new ResponseEntity<>("Product is deleted successsfully", HttpStatus.OK);
	   }
	   
	   @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
	   public ResponseEntity<Object> updateProduct(@PathVariable("id") String id, @RequestBody Product product) { 
	      productRepo.remove(id);
	      product.setId(id);
	      productRepo.put(id, product);
	      return new ResponseEntity<>("Product is updated successsfully", HttpStatus.OK);
	   }
	   
	   @RequestMapping(value = "/products", method = RequestMethod.POST)
	   public ResponseEntity<Object> createProduct(@RequestBody Product product) {
	      productRepo.put(product.getId(), product);
	      return new ResponseEntity<>("Product is created successfully", HttpStatus.CREATED);
	   }
	   
	   @RequestMapping(value = "/dockerapp")
	   public String getDockerApp() throws InterruptedException {
		   
		   String appmsg="Welcome docker app demo";
		   //hold
		   
		   //Thread.sleep(3000);
	      return appmsg;
	   }
	   
	   
	   @RequestMapping(value = "/products")
	   @HystrixCommand(fallbackMethod = "fallback_sunil", commandProperties = {
			   @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "1000")
			})
	   private String fallback_sunil() {
		   System.out.println("Fallback called");
		   
		   return "Request fails. It takes long time to response";
		}
	   
	   
	   public ResponseEntity<Object> getProduct() throws InterruptedException {
		   System.out.println("get product called");
		   LOG.log(Level.INFO, "get product called");
		   
		   //hold
		   
		   Thread.sleep(3000);
	      return new ResponseEntity<>(productRepo.values(), HttpStatus.OK);
	   }
}
