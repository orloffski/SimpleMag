package view.stockviews;

import dbhelpers.InvoicesLineDBHelper;
import dbhelpers.ProductsInStockDBHelper;
import entity.InvoicesLinesEntity;
import entity.ProductsInStockEntity;
import org.hibernate.SessionFactory;
import utils.MessagesUtils;

import java.util.List;

public class ProductsInStockController {

    public static void receiveAndInitialInStock(SessionFactory sessFact, String invoiceNum, String invoiceDate, int counterpartyId, boolean setFlag){
        List<InvoicesLinesEntity> linesEntities = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, invoiceNum);
        for(InvoicesLinesEntity line : linesEntities){
            ProductsInStockEntity productsInStockEntity = ProductsInStockEntity.createProductsInStockEntityFromInvoiceLineEntity(line, invoiceDate, counterpartyId);
            if(setFlag)
                ProductsInStockDBHelper.saveEntity(sessFact, productsInStockEntity);
            else
                ProductsInStockDBHelper.deleteByInvoiceNumber(sessFact, invoiceNum);
        }
    }

    public static void returnAndDeliveryInStock(SessionFactory sessFact, String invoiceNum, String invoiceDate, int counterpartyId, boolean setFlag){
        if(!checkStock(sessFact, invoiceNum, counterpartyId))
            MessagesUtils.showAlert("Ошибка проведения операции",
                    "Проведение операции невозможно, обратитесь к разработчику для проверки целостности базы данных.");

        List<InvoicesLinesEntity> linesEntities = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, invoiceNum);
        for(InvoicesLinesEntity line : linesEntities){
            int itemId = line.getItemId();
            String expireDate = line.getExpireDate();

            ProductsInStockEntity lineInStock = ProductsInStockDBHelper.fullFindLines(sessFact, itemId, counterpartyId, expireDate);

            if(setFlag){
                lineInStock.setItemsCount(lineInStock.getItemsCount() + Double.valueOf(line.getCount()));
                ProductsInStockDBHelper.updateEntity(sessFact, lineInStock);
            }else{
                int count = line.getCount();

                if(lineInStock.getItemsCount().intValue() > count){
                    lineInStock.setItemsCount(lineInStock.getItemsCount() - Double.valueOf(count));
                    ProductsInStockDBHelper.updateEntity(sessFact, lineInStock);
                }else{
                    while(count > 0){
                        if(lineInStock.getItemsCount().intValue() < count){
                            count -= lineInStock.getItemsCount().intValue();
                            ProductsInStockDBHelper.deleteEntity(sessFact, lineInStock);

                            lineInStock = ProductsInStockDBHelper.fullFindLines(sessFact, itemId, counterpartyId, expireDate);
                        }else{
                            lineInStock.setItemsCount(lineInStock.getItemsCount() - Double.valueOf(count));
                            ProductsInStockDBHelper.updateEntity(sessFact, lineInStock);

                            count = 0;
                        }
                    }
                }
            }
        }
    }

    private static boolean checkStock(SessionFactory sessFact, String invoiceNum, int counterpartyId){
        boolean isChecked = true;

        List<InvoicesLinesEntity> linesEntities = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, invoiceNum);
        for(InvoicesLinesEntity line : linesEntities){
            int itemId = line.getItemId();
            String expireDate = line.getExpireDate();

            ProductsInStockEntity lineInStock = ProductsInStockDBHelper.fullFindLines(sessFact, itemId, counterpartyId, expireDate);

            if(lineInStock == null) {
                isChecked = false;
                break;
            }
        }

        return isChecked;
    }
}
