package com.udacity.jdnd.course3.critter.user.service;

import com.udacity.jdnd.course3.critter.user.domain.Customer;
import com.udacity.jdnd.course3.critter.user.reposity.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    //findCustomertoPet
    public Customer buscarClienteXMascota(Long id){
        try{
            return clienteRepository.findOwnerByPet(id);
        }catch (IllegalArgumentException exception){
            throw exception;
        }
    }

}
