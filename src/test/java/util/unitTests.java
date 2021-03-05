package util;

import org.junit.Test;
import org.kurodev.command.guild.standard.rockpaperscissors.Outcome;
import org.kurodev.command.guild.standard.rockpaperscissors.RPSCondition;

import static org.junit.Assert.assertEquals;

/**
 * @author kuro
 **/
public class unitTests {
    @Test
    public void rpsCheckWorks() {
        RPSCondition rock = new RPSCondition("rock");
        RPSCondition paper = new RPSCondition("paper");
        RPSCondition scissors = new RPSCondition("scissors");
        rock.beats(scissors);
        scissors.beats(paper);
        paper.beats(rock);
        assertEquals(Outcome.LOSE, scissors.check(rock));
        assertEquals(Outcome.WIN, scissors.check(paper));
        assertEquals(Outcome.DRAW, scissors.check(scissors));
    }
}
