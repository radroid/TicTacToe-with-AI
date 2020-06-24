package tictactoegame;

import java.sql.*;

public class DBMS {

    private String filename;
    private static int nextID;

    public DBMS() {
        setFilename("Saved_Scores.db");
        createTable();
        setNextID();
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    private void setNextID() {
        String idNum = "SELECT player_id FROM Scores;";
        int maxID = 1;
        try (Connection conn = connect()) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(idNum);
            while (rs.next()) {
                maxID = rs.getInt("player_id");
            }
            nextID = maxID + 1;
        } catch (NullPointerException e) {
            nextID = 1;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private Connection connect() {
        Connection conn = null;

        try {
            // db parameters
            String url = "jdbc:sqlite:tictactoegame/" + this.filename;
            // create a connection to the database
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;

    }

    private void disconnect() {
        Connection conn = this.connect();

        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void createTable() {
        String creation = "CREATE TABLE IF NOT EXISTS Scores (\n" +
                "player_id INTEGER PRIMARY KEY,\n" +
                "playerName VARCHAR(100) NOT NULL,\n" +
                "matchesPlayed INTEGER DEFAULT 0,\n" +
                "Wins INTEGER DEFAULT 0,\n" +
                "Draws INTEGER DEFAULT 0,\n" +
                "Losses INTEGER DEFAULT 0\n" +
                ");";
        try {
            Connection conn = this.connect();
            Statement statement = conn.createStatement();
            statement.execute(creation);
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error in createTable: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    public boolean playerExists(String playerName) {

        boolean present;

        try (Connection conn = this.connect()) {
            String query = "SELECT\n" +
                    "player_id,\n" +
                    "playerName\n" +
                    "FROM\n" +
                    "Scores\n" +
                    ";";

            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                present = playerName.equals(rs.getString("playerName"));
                if (present) {
                    rs.close();
                    statement.close();
                    disconnect();
                    return true;
                }
            }
            disconnect();
            return false;
        } catch (SQLException | NullPointerException e) {
            System.out.println("Error in playerExists: " + e.getMessage());
            return false;
        } finally {
            disconnect();
        }
    }

    public void newPlayer(String playerName) {
        String sql = "INSERT INTO Scores(player_id, playerName) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pStatement = conn.prepareStatement(sql)) {
            pStatement.setInt(1, nextID);
            pStatement.setString(2, playerName);
            pStatement.executeUpdate();
            pStatement.clearParameters();
            nextID++;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            disconnect();
        }
    }

    public int getData(String playerName, String entryName) {

        String query = "SELECT\n" +
                "player_id,\n" +
                "Wins,\n" +
                "Draws,\n" +
                "Losses\n" +
                "FROM\n" +
                "Scores\n" +
                "WHERE\n" +
                "playerName = " + playerName + "\n" +
                ";";

        try (Connection conn = this.connect()) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                rs.getInt(1);
                return rs.getInt(entryName);
            } else {
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("Error in getData: " + e.getMessage());
            return -1;
        }
    }

    public void updateScore(String playerName, String result) {

        String updateStatement;
        String update1 = "UPDATE\n" +
                "Scores\n" +
                "SET ";
        String update2 = " + ?\n" +
                "WHERE\n" +
                "playerName = " + playerName + "\n" +
                ";";
        updateStatement = update1.concat(result).concat(" = ").concat(result).concat(update2);

        try (Connection conn = this.connect()) {
            PreparedStatement pStatement = conn.prepareStatement(updateStatement);
            pStatement.setInt(1, 1);
            pStatement.executeUpdate();
            pStatement.clearParameters();
            pStatement.close();
        } catch (SQLException e) {
            System.out.println("Error in updateScore: " + e.getMessage());
        } finally {
            disconnect();
            updateGamesPlayed(playerName);
        }
    }

    private void updateGamesPlayed(String playerName) {

        String updateStatement = "UPDATE\n" +
                "Scores\n" +
                "SET matchesPlayed = Wins + Losses + Draws" +
                //"WHERE\n" +
                //"playerName = ?\n" +
                ";";

        try (Connection conn = this.connect()) {
            PreparedStatement pStatement = conn.prepareStatement(updateStatement);
            //pStatement.setString(1, playerName);
            pStatement.executeUpdate();
            pStatement.clearParameters();
            pStatement.close();
        } catch (SQLException e) {
            System.out.println("Error in updateGamesPlayed: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    public void printScores() {
        String query = "SELECT\n" +
                "playerName,\n" +
                "Wins,\n" +
                "Draws,\n" +
                "Losses,\n" +
                "matchesPlayed\n" +
                "FROM\n" +
                "Scores\n" +
                "ORDER BY\n" +
                "Wins DESC,\n" +
                "Losses\n" +
                ";";

        try (Connection conn = this.connect()) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            System.out.format("\n%-30s","PLAYER NAME");
            System.out.format("%-15s","PLAYED");
            System.out.format("%-15s","WINS");
            System.out.format("%-15s","DRAWS");
            System.out.format("%-15s", "LOSSES");

//            if (!rs.next()) {
//                System.out.print("No data has been saved. Play games to save data.\n\n");
//                return;
//            }

            while (rs.next()) {
                System.out.format("\n%-30s",rs.getString("playerName"));
                System.out.format("%-15s",rs.getInt("matchesPlayed"));
                System.out.format("%-15s",rs.getInt("Wins"));
                System.out.format("%-15s",rs.getInt("Draws"));
                System.out.format("%-15s",rs.getInt("Losses"));
            }
            System.out.format("\n%50s\n\n","-- End of scoreboard --");
            rs.close();
            statement.close();
        } catch (SQLException e) {
            System.out.println("Error in printScores: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void updateDataAdmin(String playerName, String updateEntry, int changeValueTo) {

        if (changeValueTo < 0) {
            return;
        }

        int value = changeValueTo - getData(playerName, updateEntry);

        String updateStatement;
        String update1 = "UPDATE\n" +
                "Scores\n" +
                "SET ";
        String update2 = " + ?\n" +
                "SET matchesPlayed = Wins + Losses + Draws" +
                "WHERE\n" +
                "playerName = " + playerName + "\n" +
                ";";
        updateStatement = update1.concat(updateEntry).concat(" = ").concat(updateEntry).concat(update2);

        try (Connection conn = this.connect()) {
            PreparedStatement pStatement = conn.prepareStatement(updateStatement);
            pStatement.setInt(1, value);
            pStatement.executeUpdate();
            pStatement.clearParameters();
            pStatement.close();
        } catch (SQLException e) {
            System.out.println("Error in updateBalance: " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    private void deletePlayer(String playerName) {
        String updateStatement = "DELETE FROM\n" +
                "Scores\n" +
                "WHERE\n" +
                "playerName = ?\n" +
                ";";

        try (Connection conn = this.connect()) {
            PreparedStatement pStatement = conn.prepareStatement(updateStatement);
            pStatement.setString(1, playerName);
            pStatement.executeUpdate();
            pStatement.clearParameters();
            pStatement.close();
        } catch (SQLException e) {
            System.out.println("Error in updateBalance: " + e.getMessage());
        } finally {
            disconnect();
        }

    }
}
