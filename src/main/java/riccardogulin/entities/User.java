package riccardogulin.entities;

import java.util.Objects;

public class User {
	private String name;
	private String surname;
	private int age;
	private String city;

	public User(String name, String surname, int age, String city) {
		this.name = name;
		this.surname = surname;
		this.age = age;
		this.city = city;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	@Override
	public String toString() {
		return "User{" +
				"name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", age=" + age +
				", city='" + city + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return age == user.age && Objects.equals(name, user.name) && Objects.equals(surname, user.surname);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, surname, age);
	}
}
