package skillmatch;

import java.util.*;

public class PlayerPool {
  private final TreeMap<Integer, LinkedList<Player>> skillMap = new TreeMap<>();
  private final PriorityQueue<Player> waitQueue = new PriorityQueue<>(Comparator.comparingLong(Player::getJoinTime));
  private final HashMap<String, Player> lookup = new HashMap<>();

  public PlayerPool() {
  }

  public synchronized boolean addPlayer(Player p) {
    if (p == null)
      return false;
    if (lookup.containsKey(p.getId()))
      return false;
    lookup.put(p.getId(), p);
    skillMap.computeIfAbsent(p.getRating(), k -> new LinkedList<>()).add(p);
    waitQueue.add(p);
    return true;
  }

  public synchronized boolean removePlayer(String playerId) {
    Player p = lookup.remove(playerId);
    if (p == null)
      return false;
    LinkedList<Player> list = skillMap.get(p.getRating());
    if (list != null) {
      list.remove(p);
      if (list.isEmpty())
        skillMap.remove(p.getRating());
    }
    return true;
  }

  public synchronized List<Player> getPlayersInRange(int minRating, int maxRating) {
    SortedMap<Integer, LinkedList<Player>> range = skillMap.subMap(minRating, true, maxRating, true);
    List<Player> res = new ArrayList<>();
    for (List<Player> l : range.values())
      if (l != null)
        res.addAll(l);
    return res;
  }

  public synchronized Player getLongestWaitingPlayer() {
    while (!waitQueue.isEmpty()) {
      Player p = waitQueue.peek();
      if (p == null)
        return null;
      if (lookup.containsKey(p.getId()))
        return p;
      waitQueue.poll();
    }
    return null;
  }

  public synchronized int size() {
    return lookup.size();
  }

  public synchronized Collection<Player> allPlayers() {
    return new ArrayList<>(lookup.values());
  }

  public synchronized List<Player> allSortedByRating() {
    List<Player> out = new ArrayList<>();
    for (Map.Entry<Integer, LinkedList<Player>> e : skillMap.entrySet()) {
      out.addAll(e.getValue());
    }
    out.sort(Comparator.comparingInt(Player::getRating));
    return out;
  }
}
