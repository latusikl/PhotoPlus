package pl.polsl.photoplus.controllers.api;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.model.dto.BatchModelDto;
import pl.polsl.photoplus.model.entities.Product;
import pl.polsl.photoplus.services.controllers.BatchService;
import pl.polsl.photoplus.services.controllers.CategoryService;
import pl.polsl.photoplus.services.controllers.OrderService;
import pl.polsl.photoplus.services.controllers.ProductService;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ProductService productService;
    private final OrderService orderService;
    private final CategoryService categoryService;
    private final BatchService batchService;

    public ReportController(final ProductService productService, final OrderService orderService, final CategoryService categoryService, final BatchService batchService) {
        this.productService = productService;
        this.orderService = orderService;
        this.categoryService = categoryService;
        this.batchService = batchService;
    }

    @GetMapping(path = "/profit", produces = {"application/pdf"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, 'report', 'single' )")
    public ResponseEntity<ByteArrayResource> getProfitReport(@RequestParam("beginDate")
                                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                 final LocalDate beginDate,
                                                             @RequestParam("endDate")
                                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                final LocalDate endDate) throws DocumentException {
        final ByteArrayResource resource = generateProfitReport(beginDate, endDate);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    private ByteArrayResource generateProfitReport(final LocalDate beginDate, final LocalDate endDate) throws DocumentException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.BLACK);
        Chunk chunk = new Chunk("PROFIT REPORT", font);
        Paragraph para = new Paragraph(chunk);
        para.setAlignment(Paragraph.ALIGN_CENTER);
        para.setSpacingAfter(50);
        document.add(para);

        font = FontFactory.getFont(FontFactory.COURIER, 15, BaseColor.BLACK);
        final Double profit = getProfit(beginDate, endDate);
        chunk = new Chunk("Profit from " + beginDate + " to " + endDate + " : " + profit + "$", font);
        para = new Paragraph(chunk);
        para.setAlignment(Paragraph.ALIGN_JUSTIFIED);
        para.setSpacingAfter(50);
        document.add(para);

        document.close();

        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }

    private Double getProfit(final LocalDate beginDate, final LocalDate endDate) {
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


}
