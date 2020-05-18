package pl.polsl.photoplus.controllers.api;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.polsl.photoplus.annotations.validators.Image;
import pl.polsl.photoplus.model.dto.ImageModelDto;
import pl.polsl.photoplus.security.services.PermissionEvaluatorService;
import pl.polsl.photoplus.services.controllers.ImageService;

import java.io.IOException;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Validated
@RestController
@RequestMapping("/image")
public class ImageController {

    final private ImageService imageService;
    final PermissionEvaluatorService permissionEvaluatorService;

    public ImageController(final ImageService imageService, final PermissionEvaluatorService permissionEvaluatorService) {
        this.imageService = imageService;
        this.permissionEvaluatorService = permissionEvaluatorService;
    }

    @GetMapping(path = "/{code}", produces = MediaType.IMAGE_PNG_VALUE)
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, 'image', 'single' )")
    public ResponseEntity<ByteArrayResource> getSingle(@PathVariable("code") final String code) {
        final ImageModelDto img = imageService.getSingleObject(code);
        final ByteArrayResource resource = new ByteArrayResource(img.getBytes());
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PostMapping(params = "productCode")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, 'image', 'post' )")
    public ResponseEntity postImage(@Image final MultipartFile file, @RequestParam final String productCode) throws IOException {
        final String entityCode = imageService.save(new ImageModelDto(null, file.getOriginalFilename(), file.getBytes(), productCode));
        final HttpHeaders headers = new HttpHeaders();
        headers.add("Entity-Code", entityCode);
        final URI uri = linkTo(methodOn(this.getClass()).getSingle(entityCode)).toUri();
        headers.add("Location", uri.toASCIIString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{code}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, 'image', 'delete' )")
    public ResponseEntity delete(@PathVariable("code") final String code) {
        return new ResponseEntity(imageService.delete(code));
    }

    @PatchMapping("/{code}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, 'image', 'patch' )")
    public ResponseEntity patch(@Image final MultipartFile file,
                                @PathVariable("code") final String code) throws IOException {
        final ImageModelDto notPatched = imageService.getSingleObject(code);
        final ImageModelDto img = new ImageModelDto(code, file.getOriginalFilename(), file.getBytes(), notPatched.getProduct());
        return new ResponseEntity(imageService.patch(img, code));
    }
}
