package com.techelevator.tenmo.model;

import java.util.Objects;

public class Authority {

   //Instance Variable
   private String name;

   //Getters and Setters
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   //Constructor
   public Authority(String name) {
      this.name = name;
   }

   //Methods
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Authority authority = (Authority) o;
      return name.equals(authority.name);
   }

   @Override
   public int hashCode() {
      return Objects.hash(name);
   }

   @Override
   public String toString() {
      return "Authority{" +
         "name=" + name +
         '}';
   }
}
