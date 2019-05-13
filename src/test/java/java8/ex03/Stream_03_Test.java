package java8.ex03;

import java8.data.Data;
import java8.data.domain.Customer;
import java8.data.domain.Order;
import java8.data.domain.Pizza;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Exercice 03 - Collectors
 */
public class Stream_03_Test {

    @Test
    public void test_joining() throws Exception {

        List<Customer> customers = new Data().getCustomers();

        // TODO construire une chaîne contenant les prénoms des clients triés et séparé par le caractère "|"
        String result = customers.stream()
        		.sorted((c1,c2) -> c1.getFirstname().compareTo(c2.getFirstname()))
        		.map(c -> c.getFirstname())
        		.collect(Collectors.joining("|"));
        		

        assertThat(result, is("Alexandra|Cyril|Johnny|Marion|Sophie"));
    }

    @Test
    public void test_grouping() throws Exception {

        List<Order> orders = new Data().getOrders();

        // TODO construire une Map <Client, Commandes effectuées par le client
        Map<Customer, List<Order>> result = orders.stream()
        		.collect(Collectors.groupingBy(Order::getCustomer));

        assertThat(result.size(), is(2));
        assertThat(result.get(new Customer(1)), hasSize(4));
        assertThat(result.get(new Customer(2)), hasSize(4));
    }

    @Test
    public void test_partitionning() throws Exception {
        List<Pizza> pizzas = new Data().getPizzas();

        // TODO Séparer la liste des pizzas en 2 ensembles :
        // TODO true -> les pizzas dont le nom commence par "L"
        // TODO false -> les autres
        Map<Boolean, List<Pizza>> result = pizzas.stream()
        		.collect(Collectors.partitioningBy(p -> p.getName().charAt(0) == 'L'));

        assertThat(result.get(true), hasSize(6));
        assertThat(result.get(false), hasSize(2));
    }
}
