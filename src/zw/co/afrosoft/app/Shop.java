package zw.co.afrosoft.app;

import zw.co.afrosoft.data.ProductManager;
public class Shop {

    public static void main(String[] args) {
        ProductManager pm = ProductManager.getInstance();
        pm.printProductReport(101,"en-GB");


    }
}
