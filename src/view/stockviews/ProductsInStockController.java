package view.stockviews;

import dbhelpers.InvoicesHeaderDBHelper;
import dbhelpers.InvoicesLineDBHelper;
import dbhelpers.ProductsInStockDBHelper;
import entity.InvoicesHeadersEntity;
import entity.InvoicesLinesEntity;
import entity.ProductsInStockEntity;
import javafx.scene.control.ButtonType;
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
                // для поступлений и ввода начальных остатков - проведение
                // записываем в остатки
                addLinesFromStock(sessFact, invoiceNum);
                return true;
            } else if(invoiceType.equalsIgnoreCase(InvoicesTypes.RETURN.toString()) ||
                    invoiceType.equalsIgnoreCase(InvoicesTypes.DELIVERY.toString())){
                // для возвратов и перемещений - проведение
                // добавляем расход товара
                if(checkCount(sessFact, invoiceNum)){
                    addLinesFromStock(sessFact, invoiceNum);
                    return true;
                }else
                    return false;
            }
        } else if (status.equalsIgnoreCase(StatusTypes.NOENTERED.toString())){
            if (invoiceType.equalsIgnoreCase(InvoicesTypes.RECEIPT.toString()) ||
                    invoiceType.equalsIgnoreCase(InvoicesTypes.INITIAL.toString())){
                // для поступлений и ввода начальных остатков - отмена проведения
                // добавляем строки расхода
                if(checkCount(sessFact, invoiceNum)){
                    deleteLinesFromStock(sessFact, invoiceNum);
                    return true;
                }else{
                    return false;
                }
            } else if(invoiceType.equalsIgnoreCase(InvoicesTypes.RETURN.toString()) ||
                    invoiceType.equalsIgnoreCase(InvoicesTypes.DELIVERY.toString())){
                // для возвратов и перемещений - отмена проведения
                // удаляем строки расхода
                deleteLinesFromStock(sessFact, invoiceNum);
                return true;
            }
        }

        // непредвиденная ошибка
        MessagesUtils.showAlert("Ошибка проведения операции",
                "Проведение операции невозможно, обратитесь к разработчику для проверки целостности базы данных.");
        return false;
    }

    // запись товара в остатки на склад
    private static void addLinesFromStock(SessionFactory sessFact, String invoiceNum){
        InvoicesHeadersEntity invoice = InvoicesHeaderDBHelper.getInvoiceHeaderEntityByNum(sessFact, invoiceNum);

        List<InvoicesLinesEntity> linesEntities = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, invoiceNum);
        for(InvoicesLinesEntity line : linesEntities){
            ProductsInStockEntity productsInStockEntity =
                    ProductsInStockEntity.createProductsInStockEntityFromInvoiceLineEntity(
                            line, invoice.getTtnDate(), invoice.getCounterpartyId());

            // для возврата и перемещения строки с отрицательным количеством
            if(invoice.getType().equalsIgnoreCase(InvoicesTypes.DELIVERY.toString()) ||
                    invoice.getType().equalsIgnoreCase(InvoicesTypes.RETURN.toString()))
                productsInStockEntity.setItemsCount(productsInStockEntity.getItemsCount() * -1);

            ProductsInStockDBHelper.saveEntity(sessFact, productsInStockEntity);
        }
    }

    // удаление товара из остатков склада
    private static void deleteLinesFromStock(SessionFactory sessFact, String invoiceNum){
        ProductsInStockDBHelper.deleteByInvoiceNumber(sessFact, invoiceNum);
    }

    // проверка соответствия количества товара на складе и в накладной возврата/перемещения - для возврата и перемещения
    private static boolean checkCount(SessionFactory sessFact, String invoiceNum){
        boolean isChecked = true;
        ButtonType type;

        InvoicesHeadersEntity headersEntity = InvoicesHeaderDBHelper.getInvoiceHeaderEntityByNum(sessFact, invoiceNum);
        List<InvoicesLinesEntity> linesEntities = InvoicesLineDBHelper.getLinesByInvoiceNumber(sessFact, invoiceNum);
        for(InvoicesLinesEntity line : linesEntities){
            double count = ProductsInStockDBHelper.getItemsCount(sessFact, line, headersEntity);

            System.out.println(count);
            if(count < line.getCount()) {
                type = MessagesUtils.showConfirmAlert(
                        "Товары на складе",
                        "Недостаточно товаров на складе",
                        "товар: " + line.getItemName() + " на складе присутствует в количестве " + count + " - по накладной необходимо " + line.getCount()
                );

                if(type == ButtonType.CANCEL) {
                    isChecked = false;
                    break;
                }
            }
        }

        return isChecked;
    }
}
