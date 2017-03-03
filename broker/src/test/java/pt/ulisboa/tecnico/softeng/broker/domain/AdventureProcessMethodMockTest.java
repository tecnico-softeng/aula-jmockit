package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

@RunWith(JMockit.class)
public class AdventureProcessMethodMockTest {
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	@Injectable
	private Broker broker;

	@Test
	public void processWithNoExceptions(@Mocked BankInterface bankInterface, @Mocked HotelInterface hotelInterface,
			@Mocked ActivityInterface activityInterface) {
		new Expectations() {
			{
				BankInterface.processPayment("BK01987654321", 300);
				this.result = "paymentConfirmation";

				HotelInterface.reserveHotel(Type.SINGLE, AdventureProcessMethodMockTest.this.begin,
						AdventureProcessMethodMockTest.this.end);
				this.result = "hotelReference";

				ActivityInterface.reserveActivity(AdventureProcessMethodMockTest.this.begin,
						AdventureProcessMethodMockTest.this.end, 20);
				this.result = "activityReference";
			}
		};

		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, "BK01987654321", 300);

		adventure.process();

		Assert.assertEquals("paymentConfirmation", adventure.getBankPayment());
		Assert.assertEquals("hotelReference", adventure.getRoomBooking());
		Assert.assertEquals("activityReference", adventure.getActivityBooking());
	}

}
