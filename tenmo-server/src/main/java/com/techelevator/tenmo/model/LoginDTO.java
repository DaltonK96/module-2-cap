package com.techelevator.tenmo.model;

/**
 * DTO for storing a user's credentials.
 */
public class LoginDTO {

   //Instance Variables
   private String username;
   private String password;

   //Getters and Setters
   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }


   //Method
   @Override
   public String toString() {
      return "LoginDTO{" +
              "username='" + username + '\'' +
              ", password='" + password + '\'' +
              '}';
   }
}
