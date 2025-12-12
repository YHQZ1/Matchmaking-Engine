package skillmatch;

import java.util.List;

public class Match {
  private final List<Player> players;
  private final Team teamA;
  private final Team teamB;

  public Match(List<Player> players, Team teamA, Team teamB) {
    this.players = players;
    this.teamA = teamA;
    this.teamB = teamB;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public Team getTeamA() {
    return teamA;
  }

  public Team getTeamB() {
    return teamB;
  }
}
