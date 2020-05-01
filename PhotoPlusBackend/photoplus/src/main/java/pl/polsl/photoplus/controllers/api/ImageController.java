package pl.polsl.photoplus.controllers.api;

import org.springframework.core.io.ByteArrayResource;
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
import java.util.List;

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

    @PostMapping
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, 'image', 'post' )")
    public ResponseEntity postImage(@Image(service = ImageService.class) final MultipartFile file) throws IOException {
        return new ResponseEntity(imageService.save(List.of(
                new ImageModelDto(null, file.getOriginalFilename(), file.getBytes()))));
    }

    @DeleteMapping("/delete/{code}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, 'image', 'delete' )")
    public ResponseEntity delete(@PathVariable("code") final String code) {
        return new ResponseEntity(imageService.delete(code));
    }

    @PatchMapping("/{code}")
    @PreAuthorize("@permissionEvaluatorService.hasPrivilege(authentication, 'image', 'patch' )")
    public ResponseEntity patch(@Image(service = ImageService.class) final MultipartFile file,
                                @PathVariable("code") final String code) throws IOException {
        final ImageModelDto img = new ImageModelDto(code, file.getOriginalFilename(), file.getBytes());
        return new ResponseEntity(imageService.patch(img, code));
    }
}
