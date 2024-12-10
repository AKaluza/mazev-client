package example;

import example.domain.Response;
import example.domain.game.Cave;
import example.domain.game.Item;
import example.domain.game.Location;
import example.domain.game.Player;

import java.util.Collection;
import java.util.Map;


public class PreatyPrinter {
    public static char[] render(Cave cave,
                                Collection<Response.StateLocations.PlayerLocation> playerLocation,
                                Collection<Response.StateLocations.ItemLocation>  itemLocation) {
        final var tbl = new char[cave.columns() * cave.rows()];
        for (int row = 0; row < cave.rows(); row++) {
            for (int column = 0; column < cave.columns(); column++) {
                if (cave.rock(row, column)) {
                    tbl[row * cave.columns() + column] = 'X';
                } else {
                    tbl[row * cave.columns() + column] = ' ';
                }
            }
        }

        for (final var entry : playerLocation) {
            final var location = entry.location();
            tbl[location.row() * cave.columns() + location.column()] = switch (entry.entity()) {
                case Player.HumanPlayer ignored -> 'P';
                case Player.Dragon ignored -> 'D';
            };
        }

        for (final var entry : itemLocation) {
            final var location = entry.location();
            tbl[location.row() * cave.columns() + location.column()] = switch (entry.entity()) {
                case Item.Gold ignored -> 'G';
                case Item.Health ignored -> 'H';
            };
        }

        for (int row = 0; row < cave.rows(); row++) {
            for (int column = 0; column < cave.columns(); column++) {
                System.out.print(tbl[row * cave.columns() + column]);
            }
            System.out.println();
        }
        return tbl;
    }

}
