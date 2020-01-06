//Akhila Vuppalapati
//Project 2 
//EECS 132 
//October 18, 2019

import java.lang.*;

public class HW2{
 //this method returns whether the string inputted is in alphabetical order or not.
 public static boolean isAlphabeticalOrder(String str){
  char a, b;
  /* the goal of the loop is to go through the string by each chcaracter and determine if the string is in alaphabetical 
   * order. if it is not, it will return false. once it exits out of the loop because nothing returned meaning that 
   * every character was in alphabetical order, true will be returned */
  for(int i = 0; i < str.length() - 1; i++){
    //this variable stores the character at the index in string
    a = str.charAt(i);
    //this variable stores the character that is at the index plus one in the string
    b = str.charAt(i + 1);
    //the helper method inOrder is used in this if statement
    if(!inOrder(a, b) && Character.isAlphabetic(a) && Character.isAlphabetic(b)){
      return false;
    }
  }
  return true;
 }

 //this is a helper method for isAlphabeticalOrder()
 //this just compares two different characters to see which one comes first in the alphabet
 private static boolean inOrder(char a, char b){
  //this stores the entered char as the lowercase version of it
  a = Character.toLowerCase(a);
  //this stores the entered char as the lowercase version of it
  b = Character.toLowerCase(b);
  //this int is the interger value of the char a which will help determine which char should come first alphabetically
  int aAsInt = (int) a;
  //this int is the interger value of the char a which will help determine which char should come first alphabetically
  int bAsInt = (int) b;
  if(b > a || b == a){
   return true;
  }
  else {
   return false;
  }
 }
 
 //this method removes an "n" amount of occurrences of a character in an inputted string
 public static String removeNchars(String string, int n, char letter){
   StringBuilder builder = new StringBuilder();
   /* this for loop will append builder if the character in the string is not the same as the character that is being 
    * removed from the string */
   for(int i = 0; i < string.length(); i++){
     //this is the character at the index
     char letter1 = string.charAt(i); 
     if (letter1 != letter || n == 0){
       builder.append(letter1);
     }
     else {
       n--;
     }
   }
   return builder.toString();
 }
 
 //this method will remove all occurrences of a smaller inputted string from a larger inputted string
 public static String removeString(String one, String two){
   StringBuilder remove = new StringBuilder();
   StringBuilder finalSoln = new StringBuilder();
   /* this for loop will run through and make a temp string if the characters of the main string at any point match up 
    * with the full set of characters from the string that needs to be removed. then it adds all of the characters in
    * the temp string to a string called remove */
   for(int i = 0; i < one.length(); i++){
     StringBuilder temp = new StringBuilder();
     if( i + two.length() <= one.length()){
       temp.append(one.charAt(i));
       for(int j = i+1; j < two.length() + i; j++){
         temp.append(one.charAt(j));
       }
     }
     if(temp.toString().equals(two)){
       remove.append(i);
     }
   }
   int j = 0;
   /* this for loop goes through the main string and skips over the remove string (that string that we do not want in 
    * the final string and then appends the other characters to it*/
   for(int i = 0; i < one.length(); i++){
     if(j < remove.length() && i ==(int)remove.charAt(j) - 48){
       i += two.length() - 1;
       j++;
     }
     else{
       finalSoln.append(one.charAt(i));
     }
   }
   return finalSoln.toString();
 }
       
 //this method will move all the Xs in the inputted string to one character to the right
 public static String moveAllXsRight(char charInput, String stringInput){
   StringBuilder builder = new StringBuilder();
   StringBuilder temp = new StringBuilder();
   /** this for loop will go through the first string inputted and will append a temp string when the character in the 
    * stringInput matches up with the charInput. then it will place the next character in the string and after it will
    * add the temp string to the builder.*/
   for(int i = 0; i < stringInput.length(); i++){
     if (Character.toUpperCase(stringInput.charAt(i)) == Character.toUpperCase(charInput)){
       temp.append(stringInput.charAt(i));
     }
     else {
       builder.append(stringInput.charAt(i));
       builder.append(temp.toString());
       temp = new StringBuilder();
     }
   }
   builder.append(temp.toString());
   return builder.toString();
 }
 
 //this moves all of the designated character inputs down in an array
 public static void moveAllXsdown(char charInput, char[][] array){
  //this is where we will store the value when we encounter the charInput
  char v1; 
  /** this will go through the string and looks for the character input. once it finds it checks to see if it can be 
    * moved and if it can, it moves it */
  for(int i = 0; i < array.length; i++){
    for(int j = 0; j < array[i].length; j++){
      if(array[i][j] == charInput) {
        //k will be modified inside the while loop, this is b/c we don't want to modify i but we do want to use it's value and manipulate it
        int k = i; 
        v1 = array[k][j];
        //this will let us know that we have this is a free space
        array[k][j] = 0; 
        //this make sure that we're within bounds
        boolean inBounds = k < array.length && j < array[k].length; 
        //makes sure we're inbounds and the character in the array matches up with the charInput
        while (inBounds && v1 == charInput){ 
          k++;
          if(!(k < array.length && j < array[k].length)){
            inBounds = false;
          }
          else{
           v1 = array[k][j];
          }
        } 
        inBounds = k < array.length && j < array[k].length;
        if(!inBounds){
          k--;
        }
        else{
         char temp = array[k][j];
         array[k][j] = '1';
         v1 = temp;
        }
        //this just replaces the 0 with v1
        for (int y = 0; y < array.length; y++){
          for(int z = 0; z < array[y].length; z++){
            if(array[y][z] == 0){
              array[y][z] = v1;
            }
          }
        }
      }
     }
   }
  //this just replaces every 1 that was placed from the above for loop with the character input
  for(int i = 0; i < array.length; i++){
    for(int j = 0; j < array[i].length; j++){
     if(array[i][j] == '1')
       array[i][j] = charInput;
    }
  }
 }
 
 //this method moves the character input in an array into the most down and left position possible in a diagonal
 public static void moveXDownLeft(char charInput, char[][] array){
   boolean found = false;
   //This finds the first occurance of the charInput
   for(int i = 0; i < array.length; i++){
     for(int j = 0; j < array[i].length; j++){
       if(array[i][j] == charInput && !found){
         found = true;
         int k = i;
         int z = j;
         char temp;
         //This keeps moving the charInput down and to the left until its no longer possible
         while(z != 0 && k+1 != array.length){
           if(k <= array[k+1].length && z < array[k+1].length){
            temp = array[k+1][z-1];
            if(z <= array[k].length)
             array[k][z] = temp;
            else{
              boolean placed = false;
              //will put the temp in that spot in the array if the character input and the value at that spot are the same
              for(int v = 0; v < array.length; v++){
               for(int r = 0; r < array[v].length; r++){
                 if(array[v][r] == charInput){
                  array[v][r] = temp; 
                 }
               }
              }
            }
           }
           k++;
           z--;
         }
         array[k][z] = charInput;
       }
     }
   }
 }
 
 
 //this would have been my extra credit method if I had finished but I could not get it to work for the life of me so this is just here
 /* public static String moveXDownRight(char charInput, String stringInput){
   for(int i = 0; i <= stringInput.length(); i++){
     if(stringInput.charAt(i)== ){
       
     }
   }
 } */ 

}

