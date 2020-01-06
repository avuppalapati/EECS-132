import java.util.*;
import java.util.List;
import java.util.LinkedList;

/** 
 * A class that represents a railyard with a set number of classification yards.
 * The train goes through these collection yards sorting them in the appropriate way necessary.
 * 
 * @author Akhila Vuppalapati
 * 
 * @param <T> The generic type extends comparable
 */
public class RailYard<T extends Comparable<T>>{
  /**
   * railYard is an ArrayList<ArrayList<LinkedList<T>>> where each inputted value will make a new 
   * classificationYard that will have the inputted value amount of tracks.
   * */
  private ArrayList<ArrayList<LinkedList<T>>> railYard;
  /**
   * This is the constructor that sets the sizes ArrayList. 
   * This creates the whole railyard where the following methods can take place.
   * 
   * 
   * @param sizes The size of the array indicated the number of collection yards in the railyard
   * while the actual numerical value in the array is the number of tracks that are in each 
   * respective classification yard. 
   * */
  public RailYard(int[] sizes) {
    //this creates a railyard
    railYard = new ArrayList<ArrayList<LinkedList<T>>>(sizes.length);
    /* for as many elements there are in the array sizes, this for loop will create that many
    classification yards */
    for (int i = 0; i < sizes.length; i++) {
      createOneClassificationYard(sizes[i]);
    }
    //this throws an exception when there are no inputs for RailYard
    if (sizes.length == 0){
      throw new IllegalArgumentException("There are no collection yards currently. Please retry.");
    }
    //this throws an exception when a classification yard has 0 tracks inputted
    for (int j = 0; j < sizes.length; j++) {
      if (sizes[j] == 0){
        throw new IllegalArgumentException("One or more of your collection yards does not have any tracks. Please retry.");
      }
    }
  }
  
  /**
   * This is a helper method that is used to create a single classification yard with the appropriate amount of tracks 
   * This is called in the constructor.
   * 
   * @param numOfTracks This is the number of tracks that will be created in the classification yard
   * 
   * @return returns a single classificationYard that has the appropriate number of tracks
   * */
  public ArrayList<LinkedList<T>> createOneClassificationYard (int numOfTracks) {
    //this creates a classificationYard
    ArrayList<LinkedList<T>> classificationYard = new ArrayList<LinkedList<T>>();
    //this for loop adds the appropriate amount of tracks to this classificationYard
    for (int i = 0; i < numOfTracks; i++) {
      classificationYard.add(i, new LinkedList<T>());
    }
    return classificationYard;
  } 
  
  /**
   * This follows the cycleSort algorithm, just with a seperate input. This has the train's cars 
   * sorted in a way that the first car is always places on the first track, and from there the
   * next car in the train will go on a seperate track if it's smaller than the car that was 
   * placed on the previous track or will be added to the same track if it is bigger. 
   * 
   * @param train This is the cars that need to be sorted in the form of elements in an array
   * */
  public void cycleSort(T[] train) {
    /* this just has the inputted train converted from an array into a list and then has that List<T> train 
    inputted into the cycleSort method that requires a List<T> train input*/
    cycleSort(Arrays.asList(train));
  }
  
  /**   
   * This follows the cycleSort algorithm, just with a seperate input. This has the train's cars 
   * sorted in a way that the first car is always places on the first track, and from there the
   * next car in the train will go on a seperate track if it's smaller than the car that was 
   * placed on the previous track or will be added to the same track if it is bigger. 
   * 
   * @param train This is the cars that need to be sorted in the form of elements in a list
   * */
  public void cycleSort(List<T> train) {
    //this creates a linkedTrain
    LinkedList<T> linkedTrain = new LinkedList<T>(train);
    /*for every classification yard there is in  the railyard, cycleYard is being run on each 
    yard and linked */
    for(ArrayList<LinkedList<T>> yard : railYard) {
      linkedTrain = cycleYard(yard, linkedTrain);
    }
    //clears the train
    while(!linkedTrain.isEmpty()) {
      System.out.println(linkedTrain.pop());
    } 
  }

  /** 
   * This is the cycleSort algorithm. This has the train's cars sorted in a way that the 
   * first car is always places on the first track, and from there the next car in the train 
   * will go on a seperate track if it's smaller than the car that was placed on the previous track 
   * or will be added to the same track if it is bigger. 
   * 
   * @param yard this is the classification yard 
   * 
   * @param train this is the train that has the elements that need to be sorted. the cars in
   * this trainwill go through the classification yard, be sorted into the tracks, and will be 
   * merged back into a train
   * 
   * @return the merged train (the train that has its elements sorted according to the 
   * cycleSort algorithm) is returned
   * */
  public LinkedList<T> cycleYard(ArrayList<LinkedList<T>> yard, LinkedList<T> train) {
    //this keeps track of which track the code is on
    int currentTrack = 0;
    //compares the last car on the current track to the car in the train 
    for (T car : yard.get(currentTrack)) {
      if (yard.get(currentTrack).getLast().compareTo(car) > 0) {
        /* makes sure that it loops back to the first track if you run out of tracks 
         * while still increasing the currentTrack */
        currentTrack = (currentTrack + 1) % railYard.size();
      }
      yard.get(currentTrack).add(car);
    }
    return merge(yard, train.size());
  }
  
  
  /* 
  public void closestSort(T[] train) {
    
  }
  
  
  public void closestYard(ArrayList<LinkedList<T>> yard, LinkedList<T> train) {
    int 
  }
  
  public void largestBackCar(ArrayList<LinkedList<T>> yard) {
    int currentTrack = 0;
    T largestBackCar = null;
    if(yard.get(currentTrack).getLast().compareTo(yard.get(currentTrack + 1).getLast()) > 0) {
      
    }
  } */
  
  /**
   * This is the helper method that takes the elements on the track and adds them to the newly 
   * created sorted train.
   * 
   * @param yard this is the classification yard that needs to be put into the helper method
   * getSmallestCar() to ensure that only the smallest element at the front of each of the tracks
   * is being added to the train.
   * 
   * @param totalCars this is the amount of elements (cars) in the train. this is to ensure 
   * that the code does not need to keep checking to see if there's more elements to compare.
   * 
   * @return returns the merged train (the train that has its elements sorted according to the 
   * cycleSort algorithm)
   * */
  public LinkedList<T> merge(ArrayList<LinkedList<T>> yard, int totalCars) {
    //creates the new train that will have its elements sorted
    LinkedList<T> train = new LinkedList<T>();
    //adds the smallestcar to the train
    for (int i = 0; i < totalCars; i++) {
      //calls on the helper method getSmallestCar()
      train.add(getSmallestCar(yard));
    }
    //returns the new sorted train
    return train;
  }
  
  /**
   * This helper method will identify the smallest element out of all the elements that are at the front
   * of each of the tracks.
   * 
   * @param yard this is the classification yard that has all of the tracks that needs its
   * front elements compared.
   * 
   * @return returns the smallest element out of all the elements that are at the front of each 
   * of the tracks.
   * */
  public T getSmallestCar(ArrayList<LinkedList<T>> yard) {
    //creates a type T smallestCar that is null (to start)
    T smallestCar = null;
    /* continuously keeps updating the smallestCar by comparing the first element on each track to the 
     * smallestCar at that time and which ever one ends up being the smallest, will set it to the smallestCar. */
    for (LinkedList<T> track : yard) {
      if(!track.isEmpty() && (smallestCar == null || track.getFirst().compareTo(smallestCar) < 0)) {
        smallestCar = track.getFirst();
      }
    }
    //returns the car with the lowest value 
    return smallestCar;
  }
  
  /**
   * This is the main method. It is used to create a railyard and once the railyard, classification
   * yards with their respective tracks, and the train has been created, the main method will 
   * sort the train.
   * 
   * @param args represents the railyard to be created and the train to be sorted 
   * */
  public static void main(String[] args) {
    //makes the int array
    int[] numTracks = new int[Integer.parseInt(args[1])];
    //fills the int array
    for(int i = 0; i < numTracks.length; i++) {
      numTracks[i] = Integer.parseInt(args[i + 2]);
    }
    /* I am aware that these should be of generic type but I am not sure how to typecast so that I
     * do not have to specify a specific type */
    //construct a RailYard with int array
    RailYard<String> yard = new RailYard<String>(numTracks);
    //take all of the elements and stick into an array
    LinkedList<String> train = new LinkedList<String>();
    //turn args elements into a T[] or List<T>
    for(int j = 0; j < args.length; j++) {
      train.add(args[j + numTracks.length + 2]);
    }
    //run the appropriate sort
    if (args[0].equals("cycle")) {
      yard.cycleSort(train);
    }
    //this would have worked if my closestSort was working, but alas it does not
    /*
    else (args[0] equals("closest")){
      yard.closestSort(train);
    } */
  }
}
