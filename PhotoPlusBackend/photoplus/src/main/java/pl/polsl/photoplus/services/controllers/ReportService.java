package pl.polsl.photoplus.services.controllers;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.util.Pair;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.*;
import pl.polsl.photoplus.model.entities.Product;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
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
    private final RatingService ratingService;

    public ReportService(final ProductService productService, final OrderService orderService,
                         final CategoryService categoryService, final BatchService batchService,
                         final OrderItemService orderItemService, final RatingService ratingService) {
        this.productService = productService;
        this.orderService = orderService;
        this.categoryService = categoryService;
        this.batchService = batchService;
        this.orderItemService = orderItemService;
        this.ratingService = ratingService;
    }

    public ByteArrayResource generateProfitReport(final LocalDate beginDate, final LocalDate endDate) throws DocumentException {
        this.beginDate = beginDate;
        this.endDate = endDate;
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
                "Average order price: " + averageOrderValue + "$\n", font);
        para = new Paragraph(chunk);
        para.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        para.setSpacingAfter(50);
        document.add(para);

        font = FontFactory.getFont(FontFactory.COURIER, 13, Font.BOLD);

        final PdfPTable table = new PdfPTable(2);
        PdfPCell cell = new PdfPCell(new Phrase("Category name", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        cell = new PdfPCell(new Phrase("Number of sold products", font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        font = FontFactory.getFont(FontFactory.COURIER, 11, BaseColor.BLACK);
        final List<Pair<String, Integer>> soldProductNumberFromCategory = getSoldProductNumberFromCategory();
        for(final var pair: soldProductNumberFromCategory){
            cell = new PdfPCell(new Phrase(pair.getKey(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(pair.getValue().toString(), font));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }

        document.add(table);
        document.close();
        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }

    public ByteArrayResource generateProductReport(final String code) throws DocumentException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.BLACK);
        Chunk chunk = new Chunk("PRODUCT REPORT FOR PRODUCT", font);
        Paragraph para = new Paragraph(chunk);
        para.setAlignment(Paragraph.ALIGN_CENTER);
        para.setSpacingAfter(50);
        document.add(para);

        final Product product = productService.findByCodeOrThrowError(code, "GENERATE PRODUCT REPORT");
        font = FontFactory.getFont(FontFactory.COURIER, 13, BaseColor.BLACK);
        chunk = new Chunk("Code: " + product.getCode() + "\n" +
                "Name: " + product.getName() + "\n" +
                "Price: " + product.getPrice() + "$\n" +
                "Category: " + product.getCategory().getName() + "\n" +
                "Description: " + product.getDescription() + "\n", font);
        para = new Paragraph(chunk);
        para.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        para.setSpacingAfter(30);
        document.add(para);

        final Double averageRating = getAverageRating(code);
        chunk = new Chunk("Average rating: " + averageRating + "\n", font);
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
        if (orderList.isEmpty()) {
            return 0.0;
        }
        Double priceSum = 0.0;
        for (final var order: orderList) {
            priceSum += order.getPrice();
        }
        return priceSum / orderList.size();
    }

    private List<Pair<String, Integer>> getSoldProductNumberFromCategory() {
        final List<Pair<String, Integer>> pairList = new ArrayList<>();
        final List<OrderModelDto> orderList = orderService.getAll().stream().filter(order ->
                order.getDate().compareTo(beginDate) >= 0 && order.getDate().compareTo(endDate) <= 0).collect(Collectors.toList());
        final List<CategoryModelDto> categoryList = categoryService.getAll();

        for (final var category: categoryList) {
            Integer soldProductsNumber = 0;

            for (final var order : orderList) {

                final List<OrderItemModelDto> orderItemList = orderItemService.getAllByOrderCode(order.getCode());

                    for (final var orderItem: orderItemList) {
                        final Product product = productService.findByCodeOrThrowError(orderItem.getProductCode(),
                                "GET PROFIT");
                        if (product.getCategory().getCode().equals(category.getCode())) {
                            soldProductsNumber += orderItem.getQuantity();
                        }
                    }
            }

            pairList.add(new Pair(category.getName(), soldProductsNumber));
        }
        return pairList;
    }

    private Double getAverageRating(final String productCode) {
        final List<RatingModelDto> productRatingList = ratingService.getRatingsByProductCode(productCode);
        if (productRatingList.isEmpty()) {
            return 0.0;
        }

        Double ratingSum = 0.0;
        for (final var rating: productRatingList) {
            ratingSum += rating.getRate();
        }

        return ratingSum / productRatingList.size();
    }
}
