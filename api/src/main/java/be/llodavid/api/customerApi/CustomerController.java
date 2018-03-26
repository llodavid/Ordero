package be.llodavid.api.customerApi;

import be.llodavid.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private CustomerService customerService;
    private CustomerMapper customerMapper;

    @Inject
    public CustomerController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
        customerService.injectDefaultData();
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer (@RequestBody CustomerDTO customer) {
        return customerMapper.customerToDTO(
                customerService.addCustomer(
                        customerMapper.dtoToCustomer(customer)));
    }

    @GetMapping(path = "/{customerId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public CustomerDTO getCustomer(@PathVariable int customerId) {
        return customerMapper.customerToDTO(customerService.getCustomer(customerId));
    }

    @GetMapping(produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public List<CustomerDTO> getCustomers(){
        return customerService.getAllCustomers()
                .stream()
                .map(customer->customerMapper.customerToDTO(customer))
                .collect(Collectors.toList());
    }
}
