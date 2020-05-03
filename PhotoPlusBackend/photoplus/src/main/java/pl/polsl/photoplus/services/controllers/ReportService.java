package pl.polsl.photoplus.services.controllers;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.BatchModelDto;
import pl.polsl.photoplus.model.dto.OrderItemModelDto;
import pl.polsl.photoplus.model.dto.OrderModelDto;
import pl.polsl.photoplus.model.entities.Product;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Service
public class ReportService {

    private LocalDate beginDate;
    private LocalDate endDate;
    private final ProductService productService;
    private final OrderService orderService;
    private final CategoryService categoryService;
    private final BatchService batchService;
    private final OrderItemService orderItemService;

    public ReportService(final ProductService productService, final OrderService orderService, final CategoryService categoryService, final BatchService batchService, final OrderItemService orderItemService) {
        this.productService = productService;
        this.orderService = orderService;
        this.categoryService = categoryService;
        this.batchService = batchService;
        this.orderItemService = orderItemService;
    }

    public ByteArrayResource generateProfitReport() throws DocumentException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.BLACK);
        Chunk chunk = new Chunk("PROFIT REPORT FROM " + beginDate + " TO " + endDate, font);
        Paragraph para = new Paragraph(chunk);
        para.setAlignment(Paragraph.ALIGN_CENTER);
        para.setSpacingAfter(50);
        document.add(para);

        font = FontFactory.getFont(FontFactory.COURIER, 15, BaseColor.BLACK);
        final Double profit = getProfit();
        final Integer soldProductsNumber = getSoldProductsNumber();
        final Integer ordersNumber = getOrdersNumber();
        final Double averageOrderValue = getAverageOrderValue();
        chunk = new Chunk("Profit: " + profit + "$\n" +
                "Number of sold products: " + soldProductsNumber + "\n" +
                "Number of placed orders: " + ordersNumber + "\n" +
                "Average order price: " + averageOrderValue + "\n", font);
        para = new Paragraph(chunk);
        para.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        para.setSpacingAfter(50);
        document.add(para);

        document.close();

        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }

    private Double getProfit() {
        Double profit = 0.0;
        final List<BatchModelDto> batchList = batchService.getAll();
        for (final BatchModelDto batch : batchList) {
            if (batch.getDate().compareTo(beginDate) >= 0 && batch.getDate().compareTo(endDate) <= 0) {
                profit -= batch.getSupplyQuantity() * batch.getPurchasePrice();
                final Product product = productService.findByCodeOrThrowError(batch.getProductCode(),
                        "GET PROFIT");
                profit += (batch.getSupplyQuantity() - batch.getStoreQuantity()) * product.getPrice();
            }
        }
        return profit;
    }

    private Integer getSoldProductsNumber() {
        Integer soldProductsNumber = 0;
        final List<BatchModelDto> batchList = batchService.getAll();
        for (final BatchModelDto batch : batchList) {
            if (batch.getDate().compareTo(beginDate) >= 0 && batch.getDate().compareTo(endDate) <= 0) {
                soldProductsNumber += batch.getSupplyQuantity() - batch.getStoreQuantity();
            }
        }
        return soldProductsNumber;
    }

    private Integer getOrdersNumber() {
        final List<OrderModelDto> orderList = orderService.getAll().stream().filter(order ->
                order.getDate().compareTo(beginDate) >= 0 && order.getDate().compareTo(endDate) <= 0).collect(Collectors.toList());
        return orderList.size();
    }

    private Double getAverageOrderValue() {
        final List<OrderModelDto> orderList = orderService.getAll().stream().filter(order ->
                order.getDate().compareTo(beginDate) >= 0 && order.getDate().compareTo(endDate) <= 0).collect(Collectors.toList());
        Double priceSum = 0.0;
        for (final var order: orderList) {
            final List<OrderItemModelDto> orderItemList = orderItemService.getAllByOrderCode(order.getCode());

            for (final var orderItem: orderItemList) {
                final Product product = productService.findByCodeOrThrowError(orderItem.getProductCode(),
                        "GET AVERAGE ORDER VALUE");
                priceSum += product.getPrice() * orderItem.getQuantity();
            }

        }
        return priceSum / orderList.size();
    }
}
