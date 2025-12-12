package skillmatch;

import java.util.*;

public class MatchMaker {
  private final PlayerPool pool;
  private int matchSize = 6;
  private int windowExpandAmount = 100;
  private int maxExpansionRounds = 30;

  public MatchMaker(PlayerPool pool) {
    this.pool = pool;
  }

  public void setMatchSize(int s) {
    this.matchSize = Math.max(2, s);
  }

  public void setWindowExpandAmount(int w) {
    this.windowExpandAmount = Math.max(0, w);
  }

  public Match tryFormMatch() {
    if (pool.size() < matchSize)
      return null;

    Player seed = pool.getLongestWaitingPlayer();
    if (seed == null)
      return null;

    int rounds = 0;
    List<Player> candidates = Collections.emptyList();
    while (rounds <= maxExpansionRounds) {
      seed.expandWindow(windowExpandAmount);
      candidates = pool.getPlayersInRange(seed.getMinRating(), seed.getMaxRating());
      List<Player> filtered = new ArrayList<>();
      for (Player p : candidates)
        if (p != null && pool.allPlayers().stream().anyMatch(x -> x.getId().equals(p.getId())))
          filtered.add(p);
      candidates = filtered;

      if (candidates.size() >= matchSize)
        break;
      rounds++;
    }
    if (candidates.size() < matchSize)
      return null;

    List<Player> chosen = pickClosestK(seed, candidates, matchSize);

    for (Player p : chosen)
      pool.removePlayer(p.getId());

    return TeamBalancer.balance(chosen);
  }

  private List<Player> pickClosestK(Player seed, List<Player> candidates, int k) {
    candidates.sort(Comparator.<Player>comparingInt(p -> Math.abs(p.getRating() - seed.getRating()))
        .thenComparingLong(Player::getJoinTime));
    List<Player> out = new ArrayList<>();
    for (int i = 0; i < k; i++)
      out.add(candidates.get(i));
    return out;
  }
}
