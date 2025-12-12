package skillmatch;

public class Player {
  private final String id;
  private final int rating;
  private final long joinTime;

  private int minRating;
  private int maxRating;

  public Player(String id, int rating, long joinTime) {
    this.id = id;
    this.rating = rating;
    this.joinTime = joinTime;
    this.minRating = rating - 50;
    this.maxRating = rating + 50;
  }

  public String getId() {
    return id;
  }

  public int getRating() {
    return rating;
  }

  public long getJoinTime() {
    return joinTime;
  }

  public int getMinRating() {
    return minRating;
  }

  public int getMaxRating() {
    return maxRating;
  }

  public void expandWindow(int amount) {
    minRating -= amount;
    maxRating += amount;
  }

  @Override
  public String toString() {
    return id + " (" + rating + ")";
  }
}
