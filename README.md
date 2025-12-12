# Matchmaking Engine

A Java-based matchmaking engine built using data structures and algorithms. The system simulates how competitive games form fair matches by grouping players based on skill, expanding search ranges over time, and balancing teams using efficient algorithms.

## Overview

This project implements a complete matchmaking workflow entirely through in-memory data structures. Players enter a waiting pool, the engine continuously searches for compatible groups, and once enough suitable players are found, it forms a balanced match. No databases, networking, or frameworks are used; the focus is on algorithm design and performance.

## Features

- Skill-based player matching
- Dynamic search window expansion based on wait time
- Efficient player storage using TreeMap, PriorityQueue, and HashMap
- Match formation using range queries and selection algorithms
- Team balancing through partitioning and greedy strategies
- Console-based simulation for demonstration and testing

## Data Structures Used

- TreeMap for skill-sorted player indexing
- PriorityQueue for detecting longest-waiting players
- HashMap for constant-time player lookup
- Lists and sorting utilities for team formation
- Custom partitioning logic for team balancing

## How It Works

1. Players join the waiting pool with a skill rating and timestamp.
2. The engine periodically selects the longest-waiting player as the seed.
3. A search is performed for players within the seed’s skill window.
4. The window expands if a suitable group is not found.
5. When enough compatible players are available, a match group is formed.
6. The group is divided into two balanced teams using algorithmic team partitioning.
7. The match is output through the console and players are removed from the pool.

## Tech Stack

- Java
- Standard Java Collections Framework
- No external dependencies or frameworks

## Project Structure

```

src/
└── skillmatch/
Player.java
PlayerPool.java
MatchMaker.java
TeamBalancer.java
Match.java
Team.java
Main.java

```

## Goal

The purpose of this project is to demonstrate algorithmic thinking, data structure design, and performance-oriented Java programming through a real-world problem: matchmaking.
