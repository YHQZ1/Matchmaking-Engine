package skillmatch;

import java.util.*;

public class TeamBalancer {

  public static Match balance(List<Player> players) {
    int n = players.size();
    if (n == 0)
      return new Match(players, new Team(Collections.emptyList()), new Team(Collections.emptyList()));

    List<Player> sorted = new ArrayList<>(players);
    sorted.sort(Comparator.comparingInt(Player::getRating).reversed());

    if (n <= 16) {
      return exactPartition(sorted);
    } else {
      return greedyBalance(sorted);
    }
  }

  private static Match exactPartition(List<Player> players) {
    int n = players.size();
    int[] vals = new int[n];
    int total = 0;
    for (int i = 0; i < n; i++) {
      vals[i] = players.get(i).getRating();
      total += vals[i];
    }

    int bestMask = 0;
    int bestDiff = Integer.MAX_VALUE;
    int halfSize = n / 2;

    int maxMask = 1 << n;
    for (int mask = 0; mask < maxMask; mask++) {
      int cnt = Integer.bitCount(mask);
      if (cnt != halfSize && cnt != (n - halfSize))
        continue;
      int sum = 0;
      for (int i = 0; i < n; i++)
        if ((mask & (1 << i)) != 0)
          sum += vals[i];
      int other = total - sum;
      int diff = Math.abs(sum - other);
      if (diff < bestDiff) {
        bestDiff = diff;
        bestMask = mask;
        if (bestDiff == 0)
          break;
      }
    }

    List<Player> a = new ArrayList<>();
    List<Player> b = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      if ((bestMask & (1 << i)) != 0)
        a.add(players.get(i));
      else
        b.add(players.get(i));
    }
    return new Match(players, new Team(a), new Team(b));
  }

  private static Match greedyBalance(List<Player> players) {
    List<Player> a = new ArrayList<>();
    List<Player> b = new ArrayList<>();
    int sumA = 0, sumB = 0;
    for (Player p : players) {
      if (sumA <= sumB) {
        a.add(p);
        sumA += p.getRating();
      } else {
        b.add(p);
        sumB += p.getRating();
      }
    }
    return new Match(players, new Team(a), new Team(b));
  }
}
