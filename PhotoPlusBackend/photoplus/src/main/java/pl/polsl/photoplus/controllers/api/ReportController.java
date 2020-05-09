package pl.polsl.photoplus.controllers.api;

import com.itextpdf.text.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.polsl.photoplus.annotations.validators.Date;
import pl.polsl.photoplus.services.controllers.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Validated
@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(final ReportService reportService) {

        this.reportService = reportService;
    }

    @GetMapping(path = "/profit", produces = {"application/pdf"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, 'report', 'single' )")
    public ResponseEntity<ByteArrayResource> getProfitReport(@RequestParam @Date final String beginDate,
                                                             @RequestParam @Date final String endDate) throws DocumentException {
        final LocalDate beginDateAsLocalDate = LocalDate.parse(beginDate);
        final LocalDate endDateAsLocalDate = LocalDate.parse(endDate);
        final ByteArrayResource resource = reportService.generateProfitReport(beginDateAsLocalDate, endDateAsLocalDate);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @GetMapping(path = "/product", produces = {"application/pdf"})
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, 'report', 'single' )")
    public ResponseEntity<ByteArrayResource> getProductReport(@RequestParam @NotNull final String code,
                                                              @RequestParam @Date final String beginDate,
                                                              @RequestParam @Date final String endDate) throws DocumentException {
        final LocalDate beginDateAsLocalDate = LocalDate.parse(beginDate);
        final LocalDate endDateAsLocalDate = LocalDate.parse(endDate);
        final ByteArrayResource resource = reportService.generateProductReport(code, beginDateAsLocalDate, endDateAsLocalDate);
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }


}
