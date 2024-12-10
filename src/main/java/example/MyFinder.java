package example;

import example.domain.Response;
import example.domain.game.Cave;
import example.domain.game.Item;
import example.domain.game.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class MyFinder {
    public void printCost(int[] cost, Cave cave) {
        for (int row = 0; row < cave.rows(); row++) {
            for (int column = 0; column < cave.columns(); column++) {
                System.out.print(cost[row * cave.columns() + column]);
            }
            System.out.println();
        }
    }

    public ArrayList<Tree> generatePosibleMove(Location start, Cave cave) {
        ArrayList<Tree> moves = new ArrayList<Tree>();
        if (!cave.rock(start.row() - 1, start.column()))
            moves.add(new Tree(new Location(start.row() - 1, start.column()), start));
        if (!cave.rock(start.row(), start.column() - 1))
            moves.add(new Tree(new Location(start.row(), start.column() - 1), start));
        if (!cave.rock(start.row(), start.column() + 1))
            moves.add(new Tree(new Location(start.row(), start.column() + 1), start));
        if (!cave.rock(start.row() + 1, start.column()))
            moves.add(new Tree(new Location(start.row() + 1, start.column()), start));
        return moves;
    }

    public void findPath(Cave cave,
                         Collection<Response.StateLocations.PlayerLocation> playerLocation,
                         Collection<Response.StateLocations.ItemLocation> itemLocation,
                         Response.StateLocations.PlayerLocation player) {
        final var cost = new int[cave.columns() * cave.rows()];
        for (int i = 0; i < cost.length; i++) {
            cost[i] = 0;
        }
        cost[player.location().row() * cave.columns() + player.location().column()] = 0;
        this.printCost(cost, cave);
        final var usedLocation = new ArrayList<Location>();
        usedLocation.add(player.location());
        final var usedItem = new ArrayList<Tree>();
        usedItem.add(new Tree(player.location(), player.location()));

        ArrayList<Tree> posibleMoves = this.generatePosibleMove(player.location(), cave);
        for (var moves : posibleMoves) {
            var parent = moves.parent;
            var move = moves.move;
            if (usedLocation.contains(move)) {
                continue;
            } else {
                usedLocation.add(move);
                usedItem.add(moves);
                cost[move.row() * cave.columns() + move.column()] = cost[parent.row() * cave.columns() + parent.column()] + 1;
            }
        }
        for (var moves : posibleMoves) {
            rekurencja(moves.move, cave, cost, usedLocation, usedItem, 12);
        }
        final var gold = itemLocation.stream().filter(itemLocation1 -> itemLocation1.entity() instanceof Item.Gold).findAny().stream().collect(Collectors.toCollection(ArrayList::new));
        final var locationWithGold = usedLocation.stream().filter(location->gold.contains(location)).findAny().get();
        System.out.println(locationWithGold);


    }
    public void rekurencja(Location start, Cave cave, int[] cost, ArrayList<Location> usedLocation, ArrayList<Tree> usedItem, int depth) {
        ArrayList<Tree> posibleMoves = this.generatePosibleMove(start, cave);
        for (var moves : posibleMoves) {
            var parent = moves.parent;
            var move = moves.move;
            if (usedLocation.contains(move)) {
                continue;
            } else {
                usedLocation.add(move);
                usedItem.add(moves);
                cost[move.row() * cave.columns() + move.column()] = cost[start.row() * cave.columns() + start.column()] + 1;
            }
        }
        System.out.println(depth);
        if (depth > 0)
            for (var moves : posibleMoves) {
                rekurencja(moves.move, cave, cost, usedLocation, usedItem, depth - 1);
            }
    }

    record Tree(Location move, Location parent) {
    }
}
