package pgdp.searchengine.pagerepository;

import pgdp.searchengine.util.Date;

/**
 * @author Jan Wagener (wagener@in.tum.de)
 */
public class Author {
    private String firstName;
    private String lastName;
    private String address;
    private String email;
    private final Date birthday;

    // Konstruktor
    public Author(String firstName, String lastName, String address, String email, Date birthday) {
        setFirstName(firstName);
        setLastName(lastName);
        setAddress(address);
        setEmail(email);
        this.birthday = birthday;
    }

    // Getter
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        return birthday;
    }

    // Setter
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return birthday.yearsUntil(Date.today());
    }

    public boolean equals(Author other) {
        return firstName.equals(other.firstName) && lastName.equals(other.lastName) && birthday.equals(other.birthday);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String toPrintText() {
        return "<|==== " + this + " ====|>" + "\nBorn " + birthday + "\nResident in " + address + "\nE-Mail: " + email;
    }
}
