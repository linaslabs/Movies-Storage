package stores;

import structures.CustomArrayList;

public class Collection {
    private int id;
    private CustomArrayList<Integer> moveIDs;
    private String name;
    private String posterPath;
    private String backdropPath;
    
    public Collection(int id, String name, String posterPath, String backdropPath){
        this.id = id;
        this.name = name;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        moveIDs = new CustomArrayList<>();
    }

    public int getID(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getPosterPath(){
        return this.posterPath;
    }

    public String getBackdropPath(){
        return this.backdropPath;
    }

    public int[] getMovies(){
        return this.moveIDs.getAsArrayInt();
    }

    public void addMovieID(Integer id){
        moveIDs.add(id);
    }

    public void removeMovieID(Integer id){
        moveIDs.remove(id);
    }


}
