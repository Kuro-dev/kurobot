package discord.util;

import org.junit.Test;
import org.kurodev.discord.message.command.generic.rps.Outcome;
import org.kurodev.discord.message.command.generic.rps.RPSCondition;

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

    @Test
    public void rpsCheckShouldBeDrawIfTwoConditionsBeatEachOther() {
        RPSCondition rock = new RPSCondition("rock");
        RPSCondition paper = new RPSCondition("paper");
        assertEquals(Outcome.DRAW, rock.check(paper));
        assertEquals(Outcome.DRAW, paper.check(rock));
        rock.beats(paper);
        paper.beats(rock);
        assertEquals(Outcome.DRAW, rock.check(paper));
        assertEquals(Outcome.DRAW, paper.check(rock));
    }
}
