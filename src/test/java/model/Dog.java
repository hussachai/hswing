package model;

import java.util.Date;

public class Dog {
	
	Owner owner;
	Date birthdate;
	Owner owners[];
	
	
	public Owner[] getOwners() {
		return owners;
	}

	public void setOwners(Owner owners[]) {
		this.owners = owners;
	}

	public Owner getOwner() {
		return owner;
	}

	public void setOwner(Owner owner) {
		this.owner = owner;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	
	
}