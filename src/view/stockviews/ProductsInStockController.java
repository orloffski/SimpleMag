package view.stockviews;

import dbhelpers.InvoicesHeaderDBHelper;
import dbhelpers.InvoicesLineDBHelper;
import dbhelpers.ProductsInStockDBHelper;
import entity.InvoicesHeadersEntity;
import entity.InvoicesLinesEntity;
import entity.ProductsInStockEntity;
import model.InvoicesTypes;
import model.StatusTypes;
import org.hibernate.SessionFactory;
import utils.MessagesUtils;

import java.util.List;

public class ProductsInStockController {

    public static boolean checkItemsInStock(String invoiceType, String status, SessionFactory sessFact, String invoiceNum){
        // при проведении попадает статус "проведен", при отмене - "не проведен"
        if(status.equalsIgnoreCase(StatusTypes.ENTERED.toString())){
            if (invoiceType.equalsIgnoreCase(InvoicesTypes.RECEIPT.toString()) ||
                    invoiceType.equalsIgnoreCase(InvoicesTypes.INITIAL.toString())){
                // записываем в остатки и даем добро на проведение документа
                receiveAndInitialToStock(sessFact, invoiceNum);
                return true;
            } else if(invoiceType.equalsIgnoreCase(InvoicesTypes.RETURN.toString()) ||
                    invoiceType.equalsIgnoreCase(InvoicesTypes.DELIVERY.toString())){
                // проверка наличия товара от поставщика в остатках
                if(checkCount(sessFact, invoiceNum)){
                    // снимаем товар из остатков и даем добро на проведение документа
                    returnAndDeliveryFromStock(sessFact, invoiceNum);
                    return true;
                }else
                    return false;
            }
        } else if (status.equalsIgnoreCase(StatusTypes.NOENTERED.toString())){
            if (invoiceType.equalsIgnoreCase(InvoicesTypes.RECEIPT.toString()) ||
                    invoiceType.equalsIgnoreCase(InvoicesTypes.INITIAL.toString())){
                // проверяем количество товара по накладной и в остатках
                if(checkStock(sessFact, invoiceNum)){
                    // снимаем товар из остатков и даем добро на проведение документа
                    receiveAndInitialFromStock(sessFact, invoiceNum);
                    return true;
                }else{
                    return false;
                }
            } else if(invoiceType.equalsIgnoreCase(InvoicesTypes.RETURN.toString()) ||
                    invoiceType.equalsIgnoreCase(InvoicesTypes.DELIVERY.toString())){
                // возвращаем товар в остатки и проводим документ
                returnAndDeliveryToStock(sessFact, invoiceNum);
                return true;
            }
        }

        // непредвиденная ошибка
        MessagesUtils.showAlert("Ошибка проведения операции",
                "Проведение операции невозможно, обратитесь к разработчику для проверки целостности базы данных.");
        return false;
    }

    // запись товара в остатки на склад - поступление и ввод начальных остатков
    private static void receiveAndInitialToStock(SessionFactory sessFact, String invoiceNum){
        InvoicesHeadersEntity invoice = InvoicesHeaderDBHelper.getInvoiceHeaderEntityByNum(sessFact, invoiceNum);

        List<InvoicesLinesEntity> linesEntities = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, invoiceNum);
        for(InvoicesLinesEntity line : linesEntities){
            ProductsInStockEntity productsInStockEntity =
                    ProductsInStockEntity.createProductsInStockEntityFromInvoiceLineEntity(
                            line, invoice.getTtnDate(), invoice.getCounterpartyId());
            ProductsInStockDBHelper.saveEntity(sessFact, productsInStockEntity);
        }
    }

    // удаление товара из остатков склада - поступление и ввод начальных остатков
    private static void receiveAndInitialFromStock(SessionFactory sessFact, String invoiceNum){
        ProductsInStockDBHelper.deleteByInvoiceNumber(sessFact, invoiceNum);
    }

    // удаление товара из остатков склада - возврат и перемещение
    private static void returnAndDeliveryFromStock(SessionFactory sessFact, String invoiceNum){
        InvoicesHeadersEntity invoice = InvoicesHeaderDBHelper.getInvoiceHeaderEntityByNum(sessFact, invoiceNum);
        int counterpartyId = invoice.getType().equalsIgnoreCase(InvoicesTypes.DELIVERY.toString())
                ? -1 : invoice.getCounterpartyId();

        List<InvoicesLinesEntity> linesEntities = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, invoiceNum);
        for(InvoicesLinesEntity line : linesEntities){
            int itemId = line.getItemId();
            String expireDate = line.getExpireDate();

            ProductsInStockEntity lineInStock = ProductsInStockDBHelper.fullFindLines(sessFact, itemId, counterpartyId, expireDate);
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
                        if(lineInStock.getItemsCount() > 0)
                            ProductsInStockDBHelper.updateEntity(sessFact, lineInStock);
                        else
                            ProductsInStockDBHelper.deleteEntity(sessFact, lineInStock);

                        count = 0;
                    }
                }
            }
        }
    }

    // запись товара в остатки на склад - возврат и перемещение
    private static void returnAndDeliveryToStock(SessionFactory sessFact, String invoiceNum){
        InvoicesHeadersEntity invoice = InvoicesHeaderDBHelper.getInvoiceHeaderEntityByNum(sessFact, invoiceNum);

        List<InvoicesLinesEntity> linesEntities = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, invoiceNum);
        for(InvoicesLinesEntity line : linesEntities){
            // поиск аналогичного товара от поставщика самой старой поставки
            ProductsInStockEntity productsLine = ProductsInStockDBHelper.fullFindLines(sessFact, line.getItemId(), invoice.getCounterpartyId(), line.getExpireDate());

            // если товара от поставщика на складе нет - создаем новые остатки на складе
            if(productsLine == null){
                productsLine = ProductsInStockEntity.createProductsInStockEntityFromInvoiceLineEntity(line, invoice.getTtnDate(), invoice.getCounterpartyId());
                ProductsInStockDBHelper.saveEntity(sessFact, productsLine);
                return;
            }

            productsLine.setItemsCount(Double.valueOf(productsLine.getItemsCount() + line.getCount()));
            ProductsInStockDBHelper.updateEntity(sessFact, productsLine);
        }
    }

    // проверка наличия товаров по накладной
    private static boolean checkStock(SessionFactory sessFact, String invoiceNum){
        InvoicesHeadersEntity invoice = InvoicesHeaderDBHelper.getInvoiceHeaderEntityByNum(sessFact, invoiceNum);
        boolean isChecked = true;

        List<InvoicesLinesEntity> linesEntities = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, invoiceNum);
        for(InvoicesLinesEntity line : linesEntities){
            int itemId = line.getItemId();
            String expireDate = line.getExpireDate();
            int count = line.getCount();

            ProductsInStockEntity lineInStock = ProductsInStockDBHelper.fullFindLines(sessFact, itemId, invoice.getCounterpartyId(), expireDate);

            // проверка наличия строки накладной и количества товара по строке в остатках
            if(lineInStock == null ||
                    lineInStock.getItemsCount() - Double.valueOf(count) != 0) {
                isChecked = false;
                break;
            }
        }

        if(!isChecked)
            MessagesUtils.showAlert("Ошибка проведения операции",
                    "Проведение операции невозможно, товар для операции отсутствует в остатках на складе");

        return isChecked;
    }

    // проверка соответствия количества товара на складе и в накладной возврата/перемещения - для возврата и перемещения
    private static boolean checkCount(SessionFactory sessFact, String invoiceNum){
        boolean isChecked = true;

        int counterpartyId = InvoicesHeaderDBHelper.getInvoiceHeaderEntityByNum(
                sessFact, invoiceNum
                ).getType().equalsIgnoreCase(InvoicesTypes.DELIVERY.toString())
                ? -1
                : InvoicesHeaderDBHelper.getInvoiceHeaderEntityByNum(
                        sessFact, invoiceNum
                    ).getCounterpartyId();

        List<InvoicesLinesEntity> linesEntities = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, invoiceNum);
        for(InvoicesLinesEntity line : linesEntities){
            int itemId = line.getItemId();
            String expireDate = line.getExpireDate();

            double count = 0;
            List<ProductsInStockEntity> products = ProductsInStockDBHelper.findItemCountFromCounterparty(sessFact, itemId, counterpartyId, expireDate);
            for(ProductsInStockEntity product : products)
                count += product.getItemsCount();

            if(count < line.getCount()){
                isChecked = false;
                break;
            }
        }

        if(!isChecked)
            MessagesUtils.showAlert("Ошибка проведения операции",
                    "Проведение операции невозможно, товар для операции отсутствует в остатках на складе");

        return isChecked;
    }
}
