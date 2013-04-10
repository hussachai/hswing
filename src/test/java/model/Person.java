package model;

import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Person {
	
	private Person spouse;
	private String firstName;
	private String lastName;
	private String email;
	private Integer salary;
	private Date birthdate;
	
	public String toString(){
		return ReflectionToStringBuilder.toString(this);
	}
	
	public Person getSpouse() {
		return spouse;
	}
	public void setSpouse(Person spouse) {
		this.spouse = spouse;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getSalary() {
		return salary;
	}
	public void setSalary(Integer salary) {
		this.salary = salary;
	}
	public Date getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	
}
