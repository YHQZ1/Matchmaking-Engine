# Matchmaking Engine

A high-performance, algorithm-driven matchmaking engine inspired by systems used in competitive games such as **Valorant**, **Chess.com**.
Built entirely in Java, the engine focuses on **data structures**, **algorithmic design**, and **fair match creation**, with a responsive Swing UI to visualize the matchmaking process in real time.

This project is intentionally framework-free to highlight pure **DSA + system logic** without relying on databases or networking layers.

---

## Overview

The engine simulates a real-world matchmaking queue:

- Players join a pool with a rating and timestamp.
- The system identifies the longest-waiting player as the seed.
- A dynamic skill window expands over time to ensure fairness and avoid infinite waiting.
- The engine selects the _closest_ candidates by rating.
- Once enough players are found, balanced teams are created using optimal or greedy algorithms.
- The UI displays active players, matches formed, and internal decisions.

This project demonstrates how competitive games build fair and scalable matchmaking systems using clean algorithmic logic.

---

## Features

- **Dynamic skill-based matchmaking**
- **Search window expansion** based on wait time
- **Closest-K player selection** using rating distance
- **Advanced team balancing** via:
  - Optimal **bitmask partitioning** (for small groups)
  - Greedy load-balancing heuristic (for large groups)
- **Efficient player indexing** using:
  - `TreeMap` for sorted skill ranges
  - `PriorityQueue` for longest-wait queries
  - `HashMap` for O(1) lookup
- **Responsive Swing UI** (non-blocking using `SwingWorker`)
- **Real-time player queue visualization**
- **Match logs printed with team ratings and composition**

---

## Data Structures Used

| Purpose                | Data Structure          | Reason                                          |
| ---------------------- | ----------------------- | ----------------------------------------------- |
| Skill-based lookup     | **TreeMap**             | Efficient sorted rating ranges & submap queries |
| Longest-wait selection | **PriorityQueue**       | Min-heap ordering by join time                  |
| Direct access          | **HashMap**             | O(1) existence checks & removals                |
| Candidate selection    | **Sorting & lists**     | K-closest search and deterministic grouping     |
| Team balancing         | **Bitmask enumeration** | Optimal team splits for small N                 |
| Fallback balancing     | **Greedy heuristic**    | Fast balancing for large groups                 |

This combination mirrors real matchmaking server internals.

---

## How It Works — Full Flow

1. **Players join** the matchmaking pool (rating + timestamp).
2. The engine picks the **longest-waiting** player as the seed.
3. A **search window** (minRating to maxRating) defines acceptable skill matches.
4. If the window yields too few players, it **expands** gradually.
5. A set of **closest-K** candidates is selected.
6. Players are removed from the pool.
7. **Teams are balanced** using either:
   - Exact optimal partitioning (bitmask search)
   - Greedy difference-minimizing allocation
8. The completed **Match** object is displayed in the UI.

This flow is highly extensible and mirrors real MMR-based games.

---

## User Interface (Swing)

The Swing UI provides:

- Player entry fields (ID + Rating)
- Adjustable:
  - `Match Size`
  - `Window Expansion Amount`
- Auto-match toggle
- Live queue visualization
- Scrollable match logs
- Background match processing for smooth responsiveness

---

## Project Structure

```

src/
└── skillmatch/
├── Player.java
├── PlayerPool.java
├── MatchMaker.java
├── TeamBalancer.java
├── Match.java
├── Team.java
└── Main.java

```

---

## Goal

The goal of this project is to showcase:

- Clean algorithmic thinking
- Strong understanding of data structures
- Real-world system design principles
- Efficient Java programming
- A fully functional matchmaking simulator without relying on external systems

A great project for resumes, system design interviews, and algorithms portfolios.

---

## Future Enhancements (Optional Ideas)

- Glicko/Elo rating updates after each match
- Persistent storage using JSON or files
- Graph-based skill clustering
- Live rating histogram visualization in Swing
- Simulation mode generating thousands of players
