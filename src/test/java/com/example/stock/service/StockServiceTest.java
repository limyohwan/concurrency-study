package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class StockServiceTest {
    @Autowired
    private StockService stockService;
    @Autowired
    private StockRepository stockRepository;

    @BeforeEach
    void setUp() {
        Stock stock = new Stock(1L, 100L);

        stockRepository.saveAndFlush(stock);
    }

    @AfterEach
    void tearDown() {
        stockRepository.deleteAll();
    }

    @Test
    @DisplayName("재고 감소")
    void decreaseStock() {
        stockService.decrease(1L, 1L);

        Stock stock = stockRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("can't find stock"));

        assertThat(99L).isEqualTo(stock.getQuantity());
    }

    @Test
    @DisplayName("동시에 100번의 재고 감소")
    void decreaseStockWithMultipleThread() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    stockService.decrease(1L, 1L);
                }finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Stock stock = stockRepository.findById(1L)
                .orElseThrow(() -> new NoSuchElementException("can't find stock"));

        //Race condition 발생 -> 둘 이상의 쓰레드가 공유 데이터에 엑세스할 수 있고 동시에 변경을 하려고 할 때 발생하는 문제
        assertThat(0L).isEqualTo(stock.getQuantity());
    }
}