package com.greek.shop;

import org.junit.jupiter.api.Test;

//@SpringBootTest
class ShopApplicationTests {


    public static class Test1 {
        void a() {
            System.out.println("13");
            b();
        }

        void b() {
            System.out.println("14");
        }

    }

    public static class Test2 extends Test1 {
        void b() {
            System.out.println("实现父类");
            super.b();
			System.out.println("结束");

        }
    }

    @Test
    void test() {
    	Test1 test1 = new Test2();
    	test1.a();
    }


}
