package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.service.BidListService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BidTests {

	@Autowired
	private BidListRepository bidListRepository;

	@Autowired
	private BidListService bidListService;

	@MockBean
	private BidListRepository bidListRepositoryMock;

	@Test
	public void bidListTest() {
		BidList bid = new BidList("Account Test", "Type Test", 10d);

		// Save
		bid = bidListRepository.save(bid);
		Assert.assertNotNull(bid.getBidListId());
		Assert.assertEquals(bid.getBidQuantity(), 10d, 10d);

		// Update
		bid.setBidQuantity(20d);
		bid = bidListRepository.save(bid);
		Assert.assertEquals(bid.getBidQuantity(), 20d, 20d);

		// Find
		List<BidList> listResult = bidListRepository.findAll();
		Assert.assertTrue(listResult.size() > 0);

		// Delete
		Integer id = bid.getBidListId();
		bidListRepository.delete(bid);
		Optional<BidList> bidList = bidListRepository.findById(id);
		Assert.assertFalse(bidList.isPresent());
	}

	@Test
	public void bidListTest_serviceSave() {
		BidList bid = new BidList("Account Test", "Type Test", 10d);

		Mockito.when(bidListRepositoryMock.save(eq(bid))).thenReturn(bid);

		BidList saved = bidListService.create(bid);

		Assert.assertNotNull(saved);
		Mockito.verify(bidListRepositoryMock).save(eq(bid));
	}

	@Test
	public void bidListTest_serviceFindAll() {
		List<BidList> data = List.of(
				new BidList("A1", "T1", 1d),
				new BidList("A2", "T2", 2d)
		);

		Mockito.when(bidListRepositoryMock.findAll()).thenReturn(data);

		List<BidList> result = bidListService.findAll();

		Assert.assertEquals(2, result.size());
		Mockito.verify(bidListRepositoryMock).findAll();
	}

	@Test
	public void bidListTest_serviceFindById() {
		Mockito.when(bidListRepositoryMock.findById(999)).thenReturn(Optional.empty());

		try {
			bidListService.findById(999);
			Assert.fail("Expected NoSuchElementException");
		} catch (NoSuchElementException ex) {
			// ok
		}
	}

	@Test
	public void bidListTest_serviceUpdate() {
		Integer id = 1;

		BidList existing = new BidList("OldAcc", "OldType", 10d);
		existing.setBidListId(id);

		BidList newData = new BidList("NewAcc", "NewType", 20d);
		newData.setAskQuantity(5d);
		newData.setBid(1.1d);
		newData.setAsk(2.2d);
		newData.setBenchmark("bench");
		newData.setCommentary("comment");

		Mockito.when(bidListRepositoryMock.findById(id)).thenReturn(Optional.of(existing));
		Mockito.when(bidListRepositoryMock.save(any(BidList.class)))
				.thenAnswer(inv -> inv.getArgument(0));

		BidList updated = bidListService.update(id, newData);

		Assert.assertEquals("NewAcc", updated.getAccount());
		Assert.assertEquals("NewType", updated.getType());
		Assert.assertEquals(20d, updated.getBidQuantity(), 0.0001);
		Assert.assertEquals(5d, updated.getAskQuantity(), 0.0001);
		Assert.assertEquals(1.1d, updated.getBid(), 0.0001);
		Assert.assertEquals(2.2d, updated.getAsk(), 0.0001);
		Assert.assertEquals("bench", updated.getBenchmark());
		Assert.assertEquals("comment", updated.getCommentary());

		Mockito.verify(bidListRepositoryMock).save(eq(existing));
	}

	@Test
	public void bidListTest_serviceDelete() {
		Integer id = 1;

		BidList existing = new BidList("Acc", "Type", 10d);
		existing.setBidListId(id);

		Mockito.when(bidListRepositoryMock.findById(id)).thenReturn(Optional.of(existing));

		bidListService.delete(id);

		Mockito.verify(bidListRepositoryMock).delete(eq(existing));
	}
}
