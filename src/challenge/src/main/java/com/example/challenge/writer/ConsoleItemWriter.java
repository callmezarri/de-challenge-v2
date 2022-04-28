package com.example.challenge.writer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class ConsoleItemWriter<T> implements ItemWriter<T> {

    @Override
    public void write(List<? extends T> items) {
        for (T item : items) {
            log.info(item.toString());
        }
    }

}