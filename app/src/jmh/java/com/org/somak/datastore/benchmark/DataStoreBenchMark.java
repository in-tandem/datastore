package com.org.somak.datastore.benchmark;

import com.org.somak.datastore.client.LSMClient;
import com.org.somak.datastore.client.dto.Product;
import com.org.somak.datastore.client.dto.ProductKey;
import datastore.Runner;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(value = {Mode.Throughput})
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@SuppressWarnings("unused")
public class DataStoreBenchMark {

    int n = 0;
    @Setup(value = Level.Iteration)
    public void setup() throws IOException {
        new Runner().preCleanUp();

        System.out.println("inside setup method");
        n = 1;
        System.out.println("inside setup method"+ n );
    }
    @Setup(value = Level.Iteration)
    public void tearDown() throws IOException {
        new Runner().preCleanUp();
        n=0;
    }
    @Fork(3)
    @Warmup(iterations = 10, time = 1)
    @Measurement(iterations = 30, time = 1)
    @Benchmark
    public void execute_fork3_warmup10_iterations30() {
        LSMClient<ProductKey, Product> client = new LSMClient();
        n = n +1;
        ProductKey key = ProductKey.builder().productName("prdName"+Integer.toString(n)).build();
        Product product = Product
                            .builder()
                            .productDetails("details"+Integer.toString(n))
                            .units(n)
                            .price(100)
                            .build();
        client.saveData("T_PRODUCT", key, product);
    }
    @Fork(3)
    @Warmup(iterations = 10, time = 1)
    @Measurement(iterations = 60, time = 1)
    @Benchmark
    public void execute_fork3_warmup10_iterations60() {
        LSMClient<ProductKey, Product> client = new LSMClient();
        n = n +1;
        ProductKey key = ProductKey.builder().productName("prdName"+Integer.toString(n)).build();
        Product product = Product
                .builder()
                .productDetails("details"+Integer.toString(n))
                .units(n)
                .price(100)
                .build();
        client.saveData("T_PRODUCT", key, product);
    }
    @Fork(3)
    @Warmup(iterations = 10, time = 1)
    @Measurement(iterations = 90, time = 1)
    @Benchmark
    public void execute_fork3_warmup10_iterations90() {
        LSMClient<ProductKey, Product> client = new LSMClient();
        n = n +1;
        ProductKey key = ProductKey.builder().productName("prdName"+Integer.toString(n)).build();
        Product product = Product
                .builder()
                .productDetails("details"+Integer.toString(n))
                .units(n)
                .price(100)
                .build();
        client.saveData("T_PRODUCT", key, product);
    }
}
