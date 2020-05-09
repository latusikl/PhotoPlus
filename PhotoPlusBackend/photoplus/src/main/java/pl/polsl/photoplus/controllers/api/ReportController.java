package pl.polsl.photoplus.controllers.api;

import com.itextpdf.text.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.services.controllers.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(final ReportService reportService) {

        this.reportService = reportService;
    }

    @GetMapping(path = "/profit", produces = {"application/pdf"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, 'report', 'single' )")
    public ResponseEntity<ByteArrayResource> getProfitReport(@RequestParam("beginDate")
                                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                 final LocalDate beginDate,
                                                             @RequestParam("endDate")
                                                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                final LocalDate endDate) throws DocumentException {
        final ByteArrayResource resource = reportService.generateProfitReport(beginDate, endDate);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping(path = "/product", produces = {"application/pdf"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, 'report', 'single' )")
    public ResponseEntity<ByteArrayResource> getProductReport(@RequestParam("code") final String code,
                                                              @RequestParam("beginDate")
                                                                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                final LocalDate beginDate,
                                                              @RequestParam("endDate")
                                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                                                  final LocalDate endDate) throws DocumentException {
        final ByteArrayResource resource = reportService.generateProductReport(code, beginDate, endDate);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }


}
