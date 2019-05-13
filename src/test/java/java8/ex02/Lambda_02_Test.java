package java8.ex02;

import java8.data.Account;
import java8.data.Data;
import java8.data.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Exercice 02 - Map
 */
public class Lambda_02_Test {

    // tag::PersonToAccountMapper[]
    interface PersonToAccountMapper {
        Account map(Person p);
    }
    // end::PersonToAccountMapper[]

    // tag::map[]
    private List<Account> map(List<Person> personList, PersonToAccountMapper mapper) {
        // implémenter la méthode pour transformer une liste de personnes en liste de comptes
    	List<Account> result = new ArrayList<Account>();
    	for(Person p : personList) {
    		result.add(mapper.map(p));
    	}
    	
        return result;
    }
    // end::map[]


    // tag::test_map_person_to_account[]
    @Test
    public void test_map_person_to_account() throws Exception {

        List<Person> personList = Data.buildPersonList(100);

        //transformer la liste de personnes en liste de comptes
        //tous les objets comptes ont un solde à 100 par défaut
        List<Account> result = map(personList, p -> {Account a = new Account();
        												a.setBalance(100);
        												a.setOwner(p);
        												return a;});

        assert result.size() == personList.size();
        for (Account account : result) {
            assert account.getBalance().equals(100);
            assert account.getOwner() != null;
        }
    }
    // end::test_map_person_to_account[]

    // tag::test_map_person_to_firstname[]
    @Test
    public void test_map_person_to_firstname() throws Exception {

        List<Person> personList = Data.buildPersonList(100);

        // transformer la liste de personnes en liste de prénoms
        List<String> result = personList.stream()
        		.map(p -> p.getFirstname())
        		.collect(Collectors.toList());

        assert result.size() == personList.size();
        for (String firstname : result) {
            assert firstname.startsWith("first");
        }
    }
    // end::test_map_person_to_firstname[]
}
