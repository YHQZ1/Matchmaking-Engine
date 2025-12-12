package skillmatch;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutionException;

public class Main {

  private final PlayerPool pool = new PlayerPool();
  private final MatchMaker maker = new MatchMaker(pool);
  private int timeCounter = 1;

  private final DefaultListModel<String> queueModel = new DefaultListModel<>();
  private final JTextArea logArea = new JTextArea();

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Main().buildAndShow());
  }

  private void buildAndShow() {
    JFrame frame = new JFrame("Matchmaking Engine");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(700, 600);
    frame.setLayout(new BorderLayout(8, 8));

    JPanel top = new JPanel(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    c.insets = new Insets(6, 6, 6, 6);
    c.fill = GridBagConstraints.HORIZONTAL;

    JTextField idField = new JTextField();
    JTextField ratingField = new JTextField();

    JSpinner matchSizeSpinner = new JSpinner(new SpinnerNumberModel(6, 2, 20, 1));
    JSpinner expandSpinner = new JSpinner(new SpinnerNumberModel(100, 0, 5000, 50));
    JCheckBox autoMatch = new JCheckBox("Auto-match on Add", true);

    JButton addBtn = new JButton("Add Player");
    JButton tryBtn = new JButton("Form Match");

    c.gridx = 0;
    c.gridy = 0;
    top.add(new JLabel("Player ID:"), c);
    c.gridx = 1;
    c.weightx = 1.0;
    top.add(idField, c);
    c.gridx = 0;
    c.gridy = 1;
    c.weightx = 0;
    top.add(new JLabel("Rating:"), c);
    c.gridx = 1;
    top.add(ratingField, c);

    c.gridx = 0;
    c.gridy = 2;
    top.add(new JLabel("Match Size:"), c);
    c.gridx = 1;
    top.add(matchSizeSpinner, c);

    c.gridx = 0;
    c.gridy = 3;
    top.add(new JLabel("Expand Amt:"), c);
    c.gridx = 1;
    top.add(expandSpinner, c);

    c.gridx = 0;
    c.gridy = 4;
    top.add(autoMatch, c);
    c.gridx = 1;
    top.add(addBtn, c);
    c.gridx = 2;
    top.add(tryBtn, c);

    frame.add(top, BorderLayout.NORTH);

    JPanel center = new JPanel(new BorderLayout());
    JList<String> queueList = new JList<>(queueModel);
    center.add(new JScrollPane(queueList), BorderLayout.WEST);

    logArea.setEditable(false);
    center.add(new JScrollPane(logArea), BorderLayout.CENTER);

    frame.add(center, BorderLayout.CENTER);

    addBtn.addActionListener(e -> {
      String id = idField.getText().trim();
      String r = ratingField.getText().trim();
      if (id.isEmpty() || r.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Fill both fields");
        return;
      }
      int rating;
      try {
        rating = Integer.parseInt(r);
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(frame, "Rating must be integer");
        return;
      }

      Player p = new Player(id, rating, timeCounter++);
      boolean ok = pool.addPlayer(p);
      if (!ok) {
        log("Player exists or failed");
        return;
      }
      log("Added: " + p);
      refreshQueue();

      maker.setMatchSize((Integer) matchSizeSpinner.getValue());
      maker.setWindowExpandAmount((Integer) expandSpinner.getValue());

      if (autoMatch.isSelected())
        runMatchWorker();
    });

    tryBtn.addActionListener(e -> runMatchWorker());

    frame.setVisible(true);
  }

  private void runMatchWorker() {
    SwingWorker<Match, String> worker = new SwingWorker<>() {
      @Override
      protected Match doInBackground() {
        Match m = maker.tryFormMatch();
        return m;
      }

      @Override
      protected void done() {
        try {
          Match m = get();
          if (m == null) {
            log("Not enough players for a match.");
          } else {
            displayMatch(m);
            refreshQueue();
          }
        } catch (InterruptedException | ExecutionException ex) {
          log("Match worker error: " + ex.getMessage());
        }
      }
    };
    worker.execute();
  }

  private void displayMatch(Match m) {
    log("\nMatch Formed:");
    log("Team A (total " + m.getTeamA().totalRating() + "):");
    for (Player p : m.getTeamA().getPlayers())
      log("  " + p);
    log("Team B (total " + m.getTeamB().totalRating() + "):");
    for (Player p : m.getTeamB().getPlayers())
      log("  " + p);
    log("-------------------------------");
  }

  private void refreshQueue() {
    SwingUtilities.invokeLater(() -> {
      queueModel.clear();
      for (Player p : pool.allSortedByRating())
        queueModel.addElement(p.toString());
    });
  }

  private void log(String s) {
    SwingUtilities.invokeLater(() -> {
      logArea.append(s + "\n");
      logArea.setCaretPosition(logArea.getDocument().getLength());
    });
  }
}
