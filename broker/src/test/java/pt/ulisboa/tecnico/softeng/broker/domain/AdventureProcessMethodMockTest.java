package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

@RunWith(JMockit.class)
public class AdventureProcessMethodMockTest {
	private static final String ACTIVITY_REFERENCE = "activityReference";
	private static final String HOTEL_REFERENCE = "hotelReference";
	private static final String PAYMENT_CONFIRMATION = "paymentConfirmation";
	private static final String IBAN = "BK01987654321";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Broker broker;

	@Before
	public void setUp() {
		this.broker = new Broker("BR98", "Travel Light");
	}

	@Test
	public void processWithNoExceptions(@Mocked final BankInterface bankInterface,
			@Mocked final HotelInterface hotelInterface, @Mocked final ActivityInterface activityInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, 300);
				this.result = PAYMENT_CONFIRMATION;

				HotelInterface.reserveHotel(Type.SINGLE, AdventureProcessMethodMockTest.this.begin,
						AdventureProcessMethodMockTest.this.end);
				this.result = HOTEL_REFERENCE;

				ActivityInterface.reserveActivity(AdventureProcessMethodMockTest.this.begin,
						AdventureProcessMethodMockTest.this.end, 20);
				this.result = ACTIVITY_REFERENCE;
			}
		};

		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);

		adventure.process();

		Assert.assertEquals(PAYMENT_CONFIRMATION, adventure.getBankPayment());
		Assert.assertEquals(HOTEL_REFERENCE, adventure.getRoomBooking());
		Assert.assertEquals(ACTIVITY_REFERENCE, adventure.getActivityBooking());
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
	}

}
