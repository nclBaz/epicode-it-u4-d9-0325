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
		Map<Integer, List<User>> usersByAge = users.stream().collect(Collectors.groupingBy(user -> user.getAge()));
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
	}
}
