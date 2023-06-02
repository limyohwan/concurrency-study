package com.example.stock.sync;

import com.example.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SynchronizedStockFacade {
    private final StockService stockService;

    public synchronized void decrease(Long id, Long quantity) {
        stockService.decrease(id, quantity);
    }
}
