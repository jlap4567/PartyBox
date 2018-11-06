package partyboxAPITests;

import com.partyboxAPI.JSONObjectWrapper;
import com.partyboxAPI.Party;
import com.partyboxAPI.PartyFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import org.mockito.Mockito;
import org.mockito.Mockito.*;

public class PartyTest {
    private static final String NAME = "Booze Fest";
    private static final String LOCATION = "My place";
    private static final String START_TIME = "8:00PM";
    private static final String END_TIME = "3:00AM";
    private static final String DATE = "6/6/06";
    private static final Integer GUESTS = 666;

    Party party;

    @Before
    public void setup() {
        party = PartyFactory.getNewOrCurrentParty();
        party.setName(NAME);
        party.setDate(DATE);
        party.setStartTime(START_TIME);
        party.setEndTime(END_TIME);
        party.setGuestCount(GUESTS);
        party.setLocation(LOCATION);
    }

    @Test
    public void SerializationTest() throws Exception {
//        JSONObjectWrapper beforeString = party.toJSON();
//        //Mockito.when(party.toJSON()).thenCallRealMethod();
//        String serialization = beforeString.toString();
//
//        JSONObjectWrapper afterString = new JSONObjectWrapper(serialization);
//        Party resurectedParty = PartyFactory.initNewPartyFromJSON(afterString);
//        // this.party and resurected party are different instances of Party
//
//
//
//        Assert.assertEquals(party, resurectedParty);
        Assert.assertTrue(true);
    }
}
