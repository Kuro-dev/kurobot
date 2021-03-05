package org.kurodev.command.guild.standard.rockpaperscissors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author kuro
 **/
public class RPSCondition {
    private final List<RPSCondition> winsAgainst = new ArrayList<>();
    private final String name;

    public RPSCondition(String name) {

        this.name = name;
    }

    public List<RPSCondition> getWinsAgainst() {
        return winsAgainst;
    }

    public String getName() {
        return name;
    }

    public Outcome check(RPSCondition other) {
        if (this.winsAgainst.contains(other)) {
            return Outcome.WIN;
        } else if (other.winsAgainst.contains(this)) {
            return Outcome.LOSE;
        }
        return Outcome.DRAW;
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

    public void beats(RPSCondition loses) {
        winsAgainst.add(loses);
    }
}
