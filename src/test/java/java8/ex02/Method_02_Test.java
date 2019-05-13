package java8.ex02;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import java8.data.Data;
import java8.data.Person;

/**
 * Exercice 02 - Redéfinition
 */
public class Method_02_Test {

    // tag::IDao[]
    interface IDao {
        List<Person> findAll();

        // créer une méthode String format()
        // la méthode retourne une chaîne de la forme [<nb_personnes> persons]
        // exemple de résultat : "[14 persons]", "[30 persons]"
        public String format();
    }
    // end::IDao[]

    // tag::DaoA[]
    class DaoA implements IDao {

        List<Person> people = Data.buildPersonList(20);

        @Override
        public List<Person> findAll() {
            return people;
        }

        public String format() {
        	String res = "["+findAll().stream().collect(Collectors.counting())+" persons]";
        	return res;
        }

    }
    // end::DaoA[]

    @Test
    public void test_daoA_format() throws Exception {

        DaoA daoA = new DaoA();

        // invoquer la méthode format() pour que le test soit passant
        String result = daoA.format();

        "DaoA[20 persons]".equals(result);
    }
}
