package com.example.map_socialnetworkvt.Domain;


import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private String username;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;
    private ArrayList<User> friends;

    /**
     * creater of user
     */
    public User(String firstName, String lastName,String username,String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username=username;
        friends=new ArrayList<>();
        this.password=password;
    }

    /**
     *
     * @return firstname of user
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     *sets firstname
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }
//SETTERS

    /**
     * Sets the last Name
     * @param lastName New Last Name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    //GETS LIST OF FRIENDS
    public ArrayList<User> getFriends() {
        return friends;
    }

    public void setFriends(ArrayList<User> friends) {
        this.friends = friends;
    }

    @Override
    public String toString() {
        String toPrint="User{" +
                "firstName='" + firstName + "'\n" +
                "lastName='" + lastName + "'\n"+
        "username= '"+ username+ "'\n"
                ;

        return toPrint+"}";

    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        return Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                Objects.equals(getUsername(), user.getUsername());
    }

    /**
     *
     * @return hash code generated by username
     */
    @Override

    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getUsername());
    }

    /**
     *
     * @return the username of User
     */
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
