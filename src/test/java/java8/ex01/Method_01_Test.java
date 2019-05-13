package java8.ex01;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.Test;

import java8.data.Data;
import java8.data.Person;


/**
 * Exercice 01 - Méthode par défaut
 */
public class Method_01_Test {

    // tag::IDao[]
    interface IDao {
        List<Person> findAll();

        // créer une méthode int sumAge()
        // Cette méthode retourne le résultat de l'addition des ages des personnes
        default int sumAge() {
        	Optional<Integer> sum = findAll().stream()
        			.map(p -> p.getAge())
        			.reduce((a1,a2) -> a1+a2);
        	return sum.orElse(0);
        }
    }
    // end::IDao[]

    class DaoA implements IDao {

        List<Person> people = Data.buildPersonList(20);

        @Override
        public List<Person> findAll() {
            return people;
        }
    }

    class DaoB implements IDao {

        List<Person> people = Data.buildPersonList(100);

        @Override
        public List<Person> findAll() {
            return people;
        }
    }

    @Test
    public void test_daoA_sumAge() throws Exception {

        DaoA daoA = new DaoA();

        // invoquer la méthode sumAge pour que le test soit passant
        int result = 0;
        result = daoA.sumAge();

        assert result == 210;
    }

    @Test
    public void test_daoB_sumAge() throws Exception {

        DaoB daoB = new DaoB();

        // invoquer la méthode sumAge pour que le test soit passant
        int result = 0;
        result = daoB.sumAge();

        assert result == 5050;

    }
}
