package riccardogulin;

import com.github.javafaker.Faker;
import riccardogulin.entities.User;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Application {

	public static void main(String[] args) {

		Supplier<List<User>> usersListSupplier = () -> {
			Random random = new Random();
			Faker faker = new Faker(Locale.ITALY);
			List<User> usersList = new ArrayList<>();

			for (int i = 0; i < 100; i++) {
				usersList.add(new User(faker.lordOfTheRings().character(),
						faker.name().lastName(),
						random.nextInt(0, 100),
						faker.lordOfTheRings().location()));
			}
			return usersList;
		};

		List<User> users = usersListSupplier.get();

		users.forEach(System.out::println);

		// ************************************************* COLLECTORS ******************************************
		// 1. Raggruppiamo gli utenti minorenni per città
		Map<String, List<User>> usersByCity = users.stream().filter(user -> user.getAge() < 18).collect(Collectors.groupingBy(user -> user.getCity()));
		usersByCity.forEach((city, usersList) -> System.out.println("Città: " + city + ", " + usersList));

		// 2. Raggruppiamo gli utenti per età
		Map<Integer, List<User>> usersByAge = users.stream()
				.collect(Collectors.groupingBy(user -> user.getAge()));
		usersByAge.forEach((age, usersList) -> System.out.println("Età: " + age + ", " + usersList));

		// 3. Concateniamo tutti i nomi e cognomi degli utenti: "Aldo Baglio. Giovanni Storti. Giacomo Poretti. ...."
		String namesAndSurnames = users.stream().map(user -> user.getName() + " " + user.getSurname()).collect(Collectors.joining(". "));
		System.out.println(namesAndSurnames);

		// 4. Concateniamo tutte le età
		String ages = users.stream().map(user -> "" + user.getAge()).collect(Collectors.joining(". "));
		System.out.println(ages);

		// 5. Calcolo la somma delle età
		int sum = users.stream().collect(Collectors.summingInt(user -> user.getAge())); // esiste anche summingDouble per i decimali
		System.out.println("La somma delle età è: " + sum);

		// 6. Calcolo la media delle età
		double average = users.stream().collect(Collectors.averagingInt(user -> user.getAge()));
		System.out.println("La media delle età è: " + average);

		// 7. Raggruppiamo per città e calcoliamo la media delle età per ognuna di esse
		Map<String, Double> averageAgePerCity = users.stream()
				.collect(
						Collectors.groupingBy(
								user -> user.getCity(),
								Collectors.averagingInt(user -> user.getAge())
						)
				);
		averageAgePerCity.forEach((city, averageAge) -> System.out.println("Città: " + city + ", " + averageAge));

		// 8. Raggruppiamo per città e calcoliamo tutta una serie di statistiche su quegli utenti, come media età, età massima, età minima...
		Map<String, IntSummaryStatistics> statsPerCity = users.stream()
				.collect(
						Collectors.groupingBy(user -> user.getCity(),
								Collectors.summarizingInt(user -> user.getAge()))
				);
		statsPerCity.forEach((city, stats) -> System.out.println("Città: " + city + ", " + stats));

		// ********************************************************* COMPARATORS *********************************************************************
		System.out.println("********************************************************* COMPARATORS *********************************************************************");
		// I Comparators servono per stabilire un ordine di ordinamento dei dati quando si utilizza il metodo .sorted()

		// 1. Ordiniamo gli utenti per età (Ordine Crescente)
		List<User> sortedUsers = users.stream().sorted(Comparator.comparing(User::getAge)).toList();
		sortedUsers.forEach(user -> System.out.println(user));

		// 2. Ordiniamo gli utenti per età (Ordine Decrescente)
		List<User> sortedUsersReverse = users.stream().sorted(Comparator.comparing(User::getAge).reversed()).toList();
		// Se metto reversed non posso usare la lambda ma devo usare la dicitura con ::
		sortedUsersReverse.forEach(user -> System.out.println(user));

		// 3. Ordiniamo gli utenti per cognome
		List<User> sortedUsersBySurname = users.stream().sorted(Comparator.comparing(user -> user.getSurname())).toList();
		sortedUsersBySurname.forEach(user -> System.out.println(user));

		// ********************************************************* LIMIT *********************************************************************
		System.out.println("********************************************************* LIMIT *********************************************************************");

		// 1. Ottengo la lista dei 5 users più vecchi
		List<User> top5OldUsers = users.stream().sorted(Comparator.comparing(User::getAge).reversed()).limit(5).toList();
		top5OldUsers.forEach(user -> System.out.println(user));

		// 1. Ottengo la lista dei secondi 5 users più vecchi (dal sesto al decimo)
		List<User> other5OldUsers = users.stream().sorted(Comparator.comparing(User::getAge).reversed()).skip(5).limit(5).toList();
		other5OldUsers.forEach(user -> System.out.println(user));

		// ********************************************************* MAP TO *********************************************************************
		System.out.println("********************************************************* MAP TO *********************************************************************");

		// 1. Calcolo della somma delle età tramite map+reduce
		int total = users.stream().map(User::getAge).reduce(0, (partialSum, currentValue) -> partialSum + currentValue);
		System.out.println("Somma delle età ottenuta tramite map+reduce: " + total);

		// 2. Calcolo della somma delle età tramite mapToInt
		int total2 = users.stream().mapToInt(User::getAge).sum();
		System.out.println("Somma delle età ottenuta tramite mapTo: " + total2);

		// 3. Calcolo della media delle età tramite mapToInt
		OptionalDouble average2 = users.stream().mapToInt(User::getAge).average();
		if (average2.isPresent())
			System.out.println("La media è: " + average2.getAsDouble()); // Se è stata calcolata una media (.isPresent()) allora restituiscimela in forma di double
		else System.out.println("Non è stato possibile calcolare la media perché lista vuota"); // Altrimenti la media non c'è

		// 4. Calcolo dell'età massima tramite mapToInt
		OptionalInt maxAge = users.stream().mapToInt(User::getAge).max();
		if (maxAge.isPresent()) System.out.println("L'età maggiore è: " + maxAge.getAsInt());
		else System.out.println("Non è stato possibile calcolare l'età maggiore perché lista vuota");

		// 5. Calcolo delle statistiche sull'età tramite mapToInt
		IntSummaryStatistics stats = users.stream().mapToInt(User::getAge).summaryStatistics();
		System.out.println(stats);
	}
}
