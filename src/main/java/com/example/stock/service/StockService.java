package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class StockService {
    private final StockRepository stockRepository;

    @Transactional
    public void decrease(Long id, Long quantity){
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("can't find stock"));

        stock.decrease(quantity);
    }

//    @Transactional //@Transactional 애노테이션 메서드에 synchronized 키워드를 사용하면 Race condition 발생 -> 둘 이상의 쓰레드가 공유 데이터에 엑세스할 수 있고 동시에 변경을 하려고 할 때 발생하는 문제
//    public synchronized void decrease(Long id, Long quantity){
//        Stock stock = stockRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("can't find stock"));
//
//        stock.decrease(quantity);
//
//        stockRepository.saveAndFlush(stock);
//    }
}
