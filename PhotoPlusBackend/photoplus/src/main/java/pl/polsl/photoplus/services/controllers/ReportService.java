package pl.polsl.photoplus.services.controllers;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.Setter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import pl.polsl.photoplus.model.dto.*;
import pl.polsl.photoplus.model.entities.Order;
import pl.polsl.photoplus.model.entities.Product;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.AbstractMap.SimpleEntry;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Service
public class ReportService {

    private final ProductService productService;
    private final OrderService orderService;
    private final CategoryService categoryService;
    private final BatchService batchService;
    private final OrderItemService orderItemService;
    private final RatingService ratingService;
    private NumberFormat formatter = new DecimalFormat("#0.00");

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
        final Double profitBatchesOnly = getProfitBatchesOnly(beginDate, endDate);
        final Double profit = getProfit(beginDate, endDate);
        final Integer soldProductsNumber = getSoldProductsNumber(beginDate, endDate);
        final Integer ordersNumber = getOrdersNumber(beginDate, endDate);
        final Double averageOrderValue = getAverageOrderValue(beginDate, endDate);
        chunk = new Chunk("Profit: $" + formatter.format(profit) + "\n" +
                "Profit (on batches only): $" + formatter.format(profitBatchesOnly) + "\n" +
                "Number of sold products: " + soldProductsNumber + "\n" +
                "Number of placed orders: " + ordersNumber + "\n" +
                "Average order price: $" + formatter.format(averageOrderValue) + "\n", font);
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
        final List<SimpleEntry<String, Integer>> soldProductNumberFromCategory = getSoldProductNumberFromCategory(beginDate, endDate);
        for (final var pair : soldProductNumberFromCategory) {
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

    public ByteArrayResource generateProductReport(final String code, final LocalDate beginDate, final LocalDate endDate) throws DocumentException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.BLACK);
        Chunk chunk = new Chunk("PRODUCT REPORT \n FROM " + beginDate + " TO " + endDate, font);
        Paragraph para = new Paragraph(chunk);
        para.setAlignment(Paragraph.ALIGN_CENTER);
        para.setSpacingAfter(50);
        document.add(para);

        final Product product = productService.findByCodeOrThrowError(code, "GENERATE PRODUCT REPORT");
        final double averagePurchasePrice = getAveragePurchasePrice(product, beginDate, endDate);
        font = FontFactory.getFont(FontFactory.COURIER, 13, BaseColor.BLACK);
        chunk = new Chunk("Code: " + product.getCode() + "\n" +
                "Name: " + product.getName() + "\n" +
                "Price: $" + formatter.format(product.getPrice()) + "\n" +
                "Average supply cost per piece: $" + formatter.format(averagePurchasePrice) + "\n" +
                "Category: " + product.getCategory().getName() + "\n" +
                "Description: " + product.getDescription() + "\n", font);
        para = new Paragraph(chunk);
        para.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        para.setSpacingAfter(30);
        document.add(para);

        final Double averageRating = getAverageRating(code, beginDate, endDate);
        final Double averageProfit = getAverageProfit(product, beginDate, endDate);
        final Double averageProfitBatchesOnly = getAverageProfitBatchesOnly(product, beginDate, endDate);
        final Integer soldItemsNumber = getSoldItemsNumber(code, beginDate, endDate);
        chunk = new Chunk("Sold items number: " + soldItemsNumber + "\n" +
                "Average rating: " + new DecimalFormat("#0.0").format(averageRating) + "\n" +
                "Average profit per piece: $" + formatter.format(averageProfit) + "\n" +
                "Average profit per piece (on batches only): $" + formatter.format(averageProfitBatchesOnly) + "\n", font);
        para = new Paragraph(chunk);
        para.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        document.add(para);

        document.close();
        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }

    private Double getProfitBatchesOnly(final LocalDate beginDate, final LocalDate endDate) {
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

    private Double getProfit(final LocalDate beginDate, final LocalDate endDate) {
        Double profit = 0.0;
        final List<ProductModelDto> products = productService.getAll();
        for(final var product: products) {
            Integer itemsLeft = getSoldItemsNumber(product.getCode(), beginDate, endDate);
            final List<BatchModelDto> batches = batchService.getAllByProduct(product.getCode());
            for(final var batch: batches) {
                final Integer soldFromBatch = batch.getSupplyQuantity() - batch.getStoreQuantity();
                if (itemsLeft > soldFromBatch) {
                    profit += soldFromBatch * (product.getPrice() - batch.getPurchasePrice());
                    itemsLeft -= batch.getStoreQuantity();
                } else {
                    profit += itemsLeft * (product.getPrice() - batch.getPurchasePrice());
                    break;
                }
            }
        }
        return profit;
    }

    private Integer getSoldProductsNumber(final LocalDate beginDate, final LocalDate endDate) {
        Integer soldProductsNumber = 0;
        final List<OrderItemModelDto> orderItemModelList = orderItemService.getAll();
        for (final var orderItem : orderItemModelList) {
            final Order order = orderService.findByCodeOrThrowError(orderItem.getOrderCode(), "generate report");
            if (order.getDate().compareTo(beginDate) >= 0 && order.getDate().compareTo(endDate) <= 0) {
                soldProductsNumber += orderItem.getQuantity();
            }
        }
        return soldProductsNumber;
    }

    private Integer getOrdersNumber(final LocalDate beginDate, final LocalDate endDate) {
        final List<OrderModelDto> orderList = orderService.getAll().stream().filter(order ->
                order.getDate().compareTo(beginDate) >= 0 && order.getDate().compareTo(endDate) <= 0).collect(Collectors.toList());
        return orderList.size();
    }

    private Double getAverageOrderValue(final LocalDate beginDate, final LocalDate endDate) {
        final List<OrderModelDto> orderList = orderService.getAll().stream().filter(order ->
                order.getDate().compareTo(beginDate) >= 0 && order.getDate().compareTo(endDate) <= 0).collect(Collectors.toList());
        if (orderList.isEmpty()) {
            return 0.0;
        }
        Double priceSum = 0.0;
        for (final var order : orderList) {
            priceSum += order.getPrice();
        }
        return priceSum / orderList.size();
    }

    private List<SimpleEntry<String, Integer>> getSoldProductNumberFromCategory(final LocalDate beginDate, final LocalDate endDate) {
        final List<SimpleEntry<String, Integer>> pairList = new ArrayList<>();
        final List<OrderModelDto> orderList = orderService.getAll().stream().filter(order ->
                order.getDate().compareTo(beginDate) >= 0 && order.getDate().compareTo(endDate) <= 0).collect(Collectors.toList());
        final List<CategoryModelDto> categoryList = categoryService.getAll();

        for (final var category : categoryList) {
            Integer soldProductsNumber = 0;

            for (final var order : orderList) {

                final List<OrderItemModelDto> orderItemList = orderItemService.getAllByOrderCode(order.getCode());

                for (final var orderItem : orderItemList) {
                    final Product product = productService.findByCodeOrThrowError(orderItem.getProductCode(),
                            "GET PROFIT");
                    if (product.getCategory().getCode().equals(category.getCode())) {
                        soldProductsNumber += orderItem.getQuantity();
                    }
                }
            }

            pairList.add(new SimpleEntry(category.getName(), soldProductsNumber));
        }
        return pairList;
    }

    private Double getAverageRating(final String productCode, final LocalDate beginDate, final LocalDate endDate) {
        final List<RatingModelDto> productRatingList = ratingService.getRatingsByProductCode(productCode).stream()
                .filter(rating ->
                        rating.getProductCode().equals(productCode) &&
                                rating.getDate().compareTo(beginDate) >= 0 &&
                                rating.getDate().compareTo(endDate) <= 0)
                .collect(Collectors.toList());
        if (productRatingList.isEmpty()) {
            return 0.0;
        }

        Double ratingSum = 0.0;
        for (final var rating : productRatingList) {
            ratingSum += rating.getRate();
        }

        return ratingSum / productRatingList.size();
    }

    private List<BatchModelDto> getBatchesByDateAndProduct(final Product product, final LocalDate beginDate, final LocalDate endDate) {
        return batchService.getAll().stream()
                .filter(batch ->
                        batch.getProductCode().equals(product.getCode()) &&
                                batch.getDate().compareTo(beginDate) >= 0 &&
                                batch.getDate().compareTo(endDate) <= 0
                )
                .collect(Collectors.toList());
    }

    private Double getAverageProfitBatchesOnly(final Product product, final LocalDate beginDate, final LocalDate endDate) {
        final List<BatchModelDto> batchList = getBatchesByDateAndProduct(product, beginDate, endDate);
        if (batchList.isEmpty()) {
            return 0.0;
        }

        Double profit = 0.0;
        Integer supplyQuantityFromAllBatches = 0;
        for (final BatchModelDto batch : batchList) {
            profit += (batch.getSupplyQuantity() - batch.getStoreQuantity()) * product.getPrice()
                    - batch.getSupplyQuantity() * batch.getPurchasePrice();
            supplyQuantityFromAllBatches += batch.getSupplyQuantity();
        }

        if (supplyQuantityFromAllBatches == 0) {
            return 0.0;
        }

        return profit / supplyQuantityFromAllBatches;
    }

    private Double getAverageProfit(final Product product, final LocalDate beginDate, final LocalDate endDate) {
        Double profit = 0.0;
        final Integer itemsSold = getSoldItemsNumber(product.getCode(), beginDate, endDate);
        if (itemsSold == 0) {
            return 0.0;
        }
        Integer itemsLeft = getSoldItemsNumber(product.getCode(), beginDate, endDate);
        final List<BatchModelDto> batches = batchService.getAllByProduct(product.getCode());
        for(final var batch: batches) {
            final Integer soldFromBatch = batch.getSupplyQuantity() - batch.getStoreQuantity();
            if (itemsLeft > soldFromBatch) {
                profit += soldFromBatch * (product.getPrice() - batch.getPurchasePrice());
                itemsLeft -= batch.getStoreQuantity();
            } else {
                profit += itemsLeft * (product.getPrice() - batch.getPurchasePrice());
                break;
            }
        }
        return profit / itemsSold;
    }

    private Double getAveragePurchasePrice(final Product product, final LocalDate beginDate, final LocalDate endDate) {
        final List<BatchModelDto> batchList = getBatchesByDateAndProduct(product, beginDate, endDate);
        if (batchList.isEmpty()) {
            return 0.0;
        }

        Double price = 0.0;
        Integer supplyQuantity = 0;
        for (final BatchModelDto batch : batchList) {
            price += batch.getPurchasePrice() * batch.getSupplyQuantity();
            supplyQuantity += batch.getSupplyQuantity();
        }

        if (supplyQuantity == 0) {
            return 0.0;
        }

        return price / supplyQuantity;
    }

    private Integer getSoldItemsNumber(final String code, final LocalDate beginDate, final LocalDate endDate) {
        Integer soldProductsNumber = 0;
        final List<OrderItemModelDto> orderItemModelList = orderItemService.getAllByProductCode(code);
        for (final var orderItem : orderItemModelList) {
            final Order order = orderService.findByCodeOrThrowError(orderItem.getOrderCode(), "GET SOLD ITEMS NUMBER");
            if (order.getDate().compareTo(beginDate) >= 0 && order.getDate().compareTo(endDate) <= 0) {
                soldProductsNumber += orderItem.getQuantity();
            }
        }
        return soldProductsNumber;
    }

}
