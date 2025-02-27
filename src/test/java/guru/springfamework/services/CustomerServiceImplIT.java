package guru.springfamework.services;

import guru.springfamework.api.v1.mapper.CustomerMapper;
import guru.springfamework.api.v1.model.CustomerDTO;
import guru.springfamework.bootsrap.Bootstrap;
import guru.springfamework.domain.Customer;
import guru.springfamework.repositories.CategoryRepository;
import guru.springfamework.repositories.CustomerRepository;
import guru.springfamework.repositories.VendorRepository;
import lombok.Data;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CustomerServiceImplIT {

	@Autowired
	CustomerRepository customerRepository;
	@Autowired
	CategoryRepository categoryRepository;
	@Autowired
	VendorRepository vendorRepository;



	CustomerService customerService;

	@Before
	public void setUp() throws Exception {
		System.out.println("Loading Customer Data");
		System.out.println(customerRepository.findAll().size());

		//setup data for testing
		Bootstrap bootstrap = new Bootstrap(categoryRepository, customerRepository, vendorRepository);
		bootstrap.run();

		customerService = new CustomerServiceImpl(CustomerMapper.INSTANCE, customerRepository);
	}


	@Test
	public void patchCustomerUpdateFirstName() throws Exception {

		String updatedName = "UpdatedName";
		long id = getCustomerIdValue();

		Customer originalCustomer = customerRepository.getOne(id);
		assertNotNull(originalCustomer);

		String originalFirstName = originalCustomer.getFirstname();
		String originalLastName = originalCustomer.getLastname();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setFirstname(updatedName);

		customerService.patchCustomer(id, customerDTO);

		Customer updatedCustomer = customerRepository.findById(id).get();

		assertNotNull(updatedCustomer);
		assertEquals(updatedName, updatedCustomer.getFirstname());
		assertThat(originalFirstName, Matchers.not(Matchers.equalTo(updatedCustomer.getFirstname())));
		assertThat(originalLastName, Matchers.equalTo(updatedCustomer.getLastname()));



	}

	@Test
	public void patchCustomerUpdateLastName() throws Exception {

		String updatedName = "UpdatedName";
		long id = getCustomerIdValue();

		Customer originalCustomer = customerRepository.getOne(id);
		assertNotNull(originalCustomer);

		String originalFirstName = originalCustomer.getFirstname();
		String originalLastName = originalCustomer.getLastname();

		CustomerDTO customerDTO = new CustomerDTO();
		customerDTO.setLastname(updatedName);

		customerService.patchCustomer(id, customerDTO);

		Customer updatedCustomer = customerRepository.findById(id).get();

		assertNotNull(updatedCustomer);
		assertEquals(updatedName, updatedCustomer.getLastname());
		assertThat(originalFirstName, Matchers.equalTo(updatedCustomer.getFirstname()));
		assertThat(originalLastName, Matchers.not(Matchers.equalTo(updatedCustomer.getLastname())));

	}

	private Long getCustomerIdValue(){
		List<Customer> customers = customerRepository.findAll();

		System.out.println("Customers Found: " + customers.size());

		return customers.get(0).getId();
	}
}