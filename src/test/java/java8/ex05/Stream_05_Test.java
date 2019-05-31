package java8.ex05;

import org.junit.Test;

import java8.data.Data;
import java8.data.domain.Pizza;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Exercice 5 - Files
 */
public class Stream_05_Test {

    // Chemin vers un fichier de données des naissances
    private static final String NAISSANCES_DEPUIS_1900_CSV = "./naissances_depuis_1900.csv";

    private static final String DATA_DIR = "./pizza-data";


    // Structure modélisant les informations d'une ligne du fichier
    class Naissance {
        String annee;
        String jour;
        Integer nombre;

        public Naissance(String annee, String jour, Integer nombre) {
            this.annee = annee;
            this.jour = jour;
            this.nombre = nombre;
        }
        
        public Naissance(String line) {
        	String[] data = line.split(";");
        	boolean splitSuccess = data.length == 4 && !data[1].equals("ANNEE");
        	
        	if(splitSuccess) {
            	setAnnee(data[1]);
            	setJour(data[2]);
            	try {
            		setNombre(Integer.valueOf(data[3]));
            	}catch(Exception e){
            		System.out.println(e.getMessage());
            		splitSuccess = false;
            	}
        	}else if(!splitSuccess){
        		setAnnee("1900");
            	setJour("19000101");
            	setNombre(0);
        	}
        }
        
        public String toString() {
        	return "A: "+annee+" J:"+jour+" nb:"+nombre;
        }

        public String getAnnee() {
            return annee;
        }

        public void setAnnee(String annee) {
            this.annee = annee;
        }

        public String getJour() {
            return jour;
        }

        public void setJour(String jour) {
            this.jour = jour;
        }

        public Integer getNombre() {
            return nombre;
        }

        public void setNombre(Integer nombre) {
            this.nombre = nombre;
        }
    }

    @Test
    public void test_group() throws IOException {

        // TODO utiliser la méthode java.nio.file.Files.lines pour créer un stream de lignes du fichier naissances_depuis_1900.csv
        // Le bloc try(...) permet de fermer (close()) le stream après utilisation
        try (Stream<String> lines = Files.lines(FileSystems.getDefault().getPath("", NAISSANCES_DEPUIS_1900_CSV))) {

            // TODO construire une MAP (clé = année de naissance, valeur = somme des nombres de naissance de l'année)
            Map<String, Integer> result = lines
            		.map(Naissance::new)
            		.collect(Collectors.groupingBy(Naissance::getAnnee,
            				Collectors.summingInt(Naissance::getNombre)));

            lines.close();
            assertThat(result.get("2015"), is(8097));
            assertThat(result.get("1900"), is(5130));
        }
    }

    @Test
    public void test_max() throws IOException {
        // TODO utiliser la méthode java.nio.file.Files.lines pour créer un stream de lignes du fichier naissances_depuis_1900.csv
        // Le bloc try(...) permet de fermer (close()) le stream après utilisation
        try (Stream<String> lines = Files.lines(FileSystems.getDefault().getPath("", NAISSANCES_DEPUIS_1900_CSV))) {

            // TODO trouver l'année où il va eu le plus de nombre de naissance
            Optional<Naissance> result = lines
            		.map(Naissance::new)
            		.collect(Collectors.maxBy(Comparator.comparingInt(Naissance::getNombre)));


            assertThat(result.get().getNombre(), is(48));
            assertThat(result.get().getJour(), is("19640228"));
            assertThat(result.get().getAnnee(), is("1964"));
        }
    }

    @Test
    public void test_collectingAndThen() throws IOException {
        // TODO utiliser la méthode java.nio.file.Files.lines pour créer un stream de lignes du fichier naissances_depuis_1900.csv
        // Le bloc try(...) permet de fermer (close()) le stream après utilisation
        try (Stream<String> lines = Files.lines(FileSystems.getDefault().getPath("", NAISSANCES_DEPUIS_1900_CSV))) {

            // TODO construire une MAP (clé = année de naissance, valeur = maximum de nombre de naissances)
            // TODO utiliser la méthode "collectingAndThen" à la suite d'un "grouping"
            Map<String, Naissance> result = lines
            		.map(Naissance::new)
            		.collect(Collectors.groupingBy(
            				Naissance::getAnnee,
            				Collectors.collectingAndThen(
            						Collectors.maxBy(Comparator.comparingInt(Naissance::getNombre)),
            						o -> o.orElse(new Naissance("1900","19000101",0))))
            				);
            				
            		

            assertThat(result.get("2015").getNombre(), is(38));
            assertThat(result.get("2015").getJour(), is("20150909"));
            assertThat(result.get("2015").getAnnee(), is("2015"));

            assertThat(result.get("1900").getNombre(), is(31));
            assertThat(result.get("1900").getJour(), is("19000123"));
            assertThat(result.get("1900").getAnnee(), is("1900"));
        }
    }

    //Créations des pizzas pour le test suivant
    public void create_pizzas() throws IOException {
    	List<Pizza> listPizzas = new Data().getPizzas();
    	String extension = ".txt";
    	final Path PATH_DATA = FileSystems.getDefault().getPath(DATA_DIR);
    	
    	//Si le dossier pour les pizzas n'existe pas, on le crée
    	if(Files.notExists(PATH_DATA)) {
    		Files.createDirectory(PATH_DATA);
    	}
    	
    	listPizzas.stream().forEach(
    			p -> {
    					FileWriter writer = null;
						try {
							writer = new FileWriter(DATA_DIR+"/"+p.getId()+extension, false);
							writer.write(p.getName()+":"+p.getPrice());
	    					writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
    			});
    }
    
    // Des données figurent dans le répertoire pizza-data
    // TODO explorer les fichiers pour voir leur forme
    // TODO compléter le test

    @Test
    public void test_pizzaData() throws IOException {
    	create_pizzas();
        // TODO utiliser la méthode java.nio.file.Files.list pour parcourir un répertoire

        // TODO trouver la pizza la moins chère
        String pizzaNamePriceMin = Files.list(FileSystems.getDefault().getPath(DATA_DIR))
        		//On récupère une ligne dans chaque fichier
        		.map(path -> {
					try {
						return Files.lines(path).findFirst().orElse("NaP:"+Integer.MAX_VALUE);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return "NaP:"+Integer.MAX_VALUE;
				})
        		//On parse les lignes
        		.map(s -> {
        			String nom = "";
        			Integer prix = Integer.MAX_VALUE;
        			String[] split = s.split(":");
        			nom = split[0];
        			prix = Integer.parseInt(split[1]);
        			return new Pizza(0, nom, prix);
        		})
        		.min(Comparator.comparingInt(Pizza::getPrice))
        		.orElse(new Pizza(0, "NaP", Integer.MAX_VALUE))
        		.getName();

        assertThat(pizzaNamePriceMin, is("L'indienne"));

    }

    // TODO Optionel
    // TODO Créer un test qui exporte des données new Data().getPizzas() dans des fichiers
    // TODO 1 fichier par pizza
    // TODO le nom du fichier est de la forme ID.txt (ex. 1.txt, 2.txt)

}