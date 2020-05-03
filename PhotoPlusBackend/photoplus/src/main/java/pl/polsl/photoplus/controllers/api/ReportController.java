package pl.polsl.photoplus.controllers.api;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.services.controllers.BatchService;
import pl.polsl.photoplus.services.controllers.CategoryService;
import pl.polsl.photoplus.services.controllers.OrderService;
import pl.polsl.photoplus.services.controllers.ProductService;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
    public ResponseEntity<ByteArrayResource> getSingle() throws DocumentException {
        ByteArrayResource resource = generateProfitReport();
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    private ByteArrayResource generateProfitReport() throws DocumentException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.BLACK);
        Chunk chunk = new Chunk("PROFIT REPORT", font);
        Paragraph para1 = new Paragraph(chunk);
        para1.setAlignment(Paragraph.ALIGN_CENTER);
        para1.setSpacingAfter(50);
        document.add(para1);


        document.close();

        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }


}
