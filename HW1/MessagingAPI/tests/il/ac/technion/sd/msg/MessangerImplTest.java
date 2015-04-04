package il.ac.technion.sd.msg;

import static org.junit.Assert.*;

import java.util.Optional;

import il.ac.technion.sd.msg.MessangerFactory;
import il.ac.technion.sd.msg.Messenger;

import org.junit.After;
import org.junit.Test;

public class MessangerImplTest {

	private final MessangerFactory factory = new MessangerFactory();

	@After
	public void teardown() throws Exception {
		for (Messenger m : factory.ms)
			try {
				m.kill();
			} catch (Exception e) {

			}
	}

	@Test(expected = RuntimeException.class)
	public void shouldThrowAnExceptionOnNullAddress() throws Exception {
		factory.start(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowAnExceptionOnEmptyAddress() throws Exception {
		factory.start("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowAnExceptionOnWhitespaceAddress() throws Exception {
		factory.start("    ");
	}

	@Test(expected = MessageException.class)
	public void shouldThrowAnExceptionOnTwoIdentitcalNames() throws Exception {
		factory.start("a");
		factory.start("a");
	}
	
	@Test
	public void canCreateNewObjectWithSameAddressAfterKilled() throws Exception {
		factory.start("a").kill();
		factory.start("a");
	}


	@Test
	public void shouldReceiveASendMessage() throws Exception {
		Messenger m1 = factory.startAndAddToList();
		Messenger m2 = factory.startAndAddToList();
		m1.send(m2.getAddress(), "hello".getBytes());
		assertArrayEquals(m2.listen(), "hello".getBytes());
		m2.send(m1.getAddress(), "hi back".getBytes());
		assertArrayEquals(m1.listen(), "hi back".getBytes());

	}

	@Test
	public void tryListenShouldReturnEmptyWhenNoMessageExists()
			throws Exception {
		Messenger m1 = factory.startAndAddToList();
		assertEquals(m1.tryListen(), Optional.empty());
	}

	@Test
	public void tryListenShouldReturnMessageWhenExists() throws Exception {
		Messenger m1 = factory.startAndAddToList();
		Messenger m2 = factory.startAndAddToList();
		m2.send(m1.getAddress(), "test".getBytes());
		assertArrayEquals(m1.tryListen().get(), "test".getBytes());
	}

	@Test(expected = MessageException.class)
	public void cannotBeKilledTwice() throws Exception {
		Messenger $ = factory.startAndAddToList();
		$.kill();
		$.kill();
	}
}
