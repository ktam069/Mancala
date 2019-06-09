package kalah.View;

import kalah.Settings;

import java.util.ArrayList;
import java.util.List;

public class HorizontalASCIIBoard implements ASCIIBoardInterface {
    @Override
    public List<String> formatBoardAsLines(List<Integer> storeSeeds, List<List<Integer>> houseSeedsList) {
        List<String> asciiLines = new ArrayList<String>();

        asciiLines.add("+----+-------+-------+-------+-------+-------+-------+----+");
        asciiLines.add( formatLine1(storeSeeds, houseSeedsList) );
        asciiLines.add("|    |-------+-------+-------+-------+-------+-------|    |");
        asciiLines.add( formatLine2(storeSeeds, houseSeedsList) );
        asciiLines.add("+----+-------+-------+-------+-------+-------+-------+----+");

        return asciiLines;
    }

    private String formatLine1(List<Integer> storeSeeds, List<List<Integer>> houseSeedsList) {
        String asciiLine;
        List<Integer> houseSeeds;
        int playerI = 0;

        // Format P2 Houses
        playerI = nextPlayerIndex(playerI);
        asciiLine = "| P"+(playerI+1)+" |";
        houseSeeds = houseSeedsList.get(playerI);
        for (int i = houseSeeds.size()-1; i >= 0 ; i--) {
            asciiLine += String.format("%2d[%2d] |", i+1, houseSeeds.get(i));
        }

        // Format P1 Store
        playerI = nextPlayerIndex(playerI);
        asciiLine += String.format(" %2d |", storeSeeds.get(playerI));

        return asciiLine;
    }

    private String formatLine2(List<Integer> storeSeeds, List<List<Integer>> houseSeedsList) {
        String asciiLine;
        List<Integer> houseSeeds;
        int playerI = 0;

        // Format P2 Store
        playerI = nextPlayerIndex(playerI);
        asciiLine = String.format("| %2d |", storeSeeds.get(1));

        // Format P1 Houses
        playerI = nextPlayerIndex(playerI);
        houseSeeds = houseSeedsList.get(playerI);
        for (int i = 0; i < houseSeeds.size(); i++) {
            asciiLine += String.format("%2d[%2d] |", i+1, houseSeeds.get(i));
        }
        asciiLine += " P"+(playerI+1)+" |";

        return asciiLine;
    }

    private int nextPlayerIndex(int playerI) {
        return (playerI+1) % Settings.NUM_PLAYERS;
    }
}
