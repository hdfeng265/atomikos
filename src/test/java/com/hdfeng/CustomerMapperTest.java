package com.hdfeng;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hdfeng.dao.customer.CustomerMapper;
import com.hdfeng.domain.customer.Customer;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CustomerMapperTest {

	@Autowired
	private CustomerMapper customerMapper;

	@Test
	public void testCustomerConfig() {

	}

	@Test
	public void save() {
		Customer c = new Customer();
		c.setName("test-name");
		c.setAge(30);
		customerMapper.insertCustomer(c);
		Assert.assertNotNull(c.getId());
	}

}
