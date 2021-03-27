package org.kurodev.discord.command.guild.standard.rps;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author kuro
 **/
public class RPSCondition {
    private final String name;
    /**
     * List containing the names of every condition this instance can win against.
     */
    private final List<String> winsAgainst = new ArrayList<>();

    public RPSCondition(String name) {

        this.name = name;
    }

    public List<String> getWinsAgainst() {
        return winsAgainst;
    }

    public String getName() {
        return name;
    }

    public boolean winsAgainst(RPSCondition other) {
        return winsAgainst(other.name);
    }

    private boolean winsAgainst(String other) {
        return this.winsAgainst.contains(other);
    }

    public Outcome check(RPSCondition other) {
        boolean thisWins = winsAgainst(other);
        boolean thatWins = other.winsAgainst(this);
        if (thisWins == thatWins) {
            return Outcome.DRAW;
        } else if (thisWins) {
            return Outcome.WIN;
        } else {
            return Outcome.LOSE;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RPSCondition that = (RPSCondition) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "RPSCondition{" +
                "name='" + name + '\'' +
                '}';
    }

    /**
     * Registers another instance as inferior in comparison meaning that this instance will beat the given one.
     * <p>example:</p>
     * {@code rock.beats(scissors); scissors.beats(paper); paper.beats(rock); }
     *
     * @param loses The condition that will lose against this instance.
     */
    public void beats(RPSCondition loses) {
        winsAgainst.add(loses.name);
    }
}
