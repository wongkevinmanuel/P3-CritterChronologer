package com.udacity.jdnd.course3.critter.user.service;

import com.udacity.jdnd.course3.critter.user.domain.Customer;
import com.udacity.jdnd.course3.critter.user.reposity.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class CustomerService {
    @Autowired
    CustomerRepository clienteRepository;

    //save Customer
    public Long guardar(Customer customer){
        try{
            return clienteRepository.save(customer).getId();
        }catch (IllegalArgumentException exception){
            throw exception;
        }
    }

    public Customer buscarClienteXMascota(Long id){
        try{
            Customer customer = clienteRepository.findOwnerByPet(id);
            if(Objects.isNull(customer))
                throw new EmptyResultDataAccessException(1);

            return customer;
        }catch (IllegalArgumentException exception){
            //EntityNotFoundException
            //throw exception;
            throw new EmptyResultDataAccessException(1);
        }
    }

    public List<Customer> clientes(){
        try{
            return clienteRepository.findAll();
        }catch (IllegalArgumentException exception){
            //throw new UnsupportedOperationException();
            throw new EmptyResultDataAccessException(1);
        }
    }
}
