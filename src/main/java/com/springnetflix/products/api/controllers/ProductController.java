package com.springnetflix.products.api.controllers;

import com.springnetflix.products.api.data.vo.ProductVO;
import com.springnetflix.products.api.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final PagedResourcesAssembler<ProductVO> assembler;

    @Autowired
    public ProductController(ProductService productService, PagedResourcesAssembler<ProductVO> assembler) {
        this.productService = productService;
        this.assembler = assembler;
    }

    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ProductVO findById(@PathVariable("id") Long id) {
        ProductVO productVO = productService.findById(id);
        productVO.add(linkTo(methodOn(ProductController.class).findById(id)).withSelfRel());
        return productVO;
    }

    @GetMapping(produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<?> findAll(@RequestParam(value = "page", defaultValue = "0") int page,
                                     @RequestParam(value = "limit", defaultValue = "10") int limit,
                                     @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

        Page<ProductVO> products = productService.findAll(pageable);
        products.stream().forEach(p -> p.add(linkTo(methodOn(ProductController.class).findById(p.getId())).withSelfRel()));

        PagedModel<EntityModel<ProductVO>> pagedModel = assembler.toModel(products);

        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }

    @PostMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
                 consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ProductVO create(@RequestBody ProductVO productVO) {
        ProductVO productVOCreated = productService.create(productVO);
        productVOCreated.add(linkTo(methodOn(ProductController.class).findById(productVOCreated.getId())).withSelfRel());
        return productVOCreated;
    }

    @PutMapping(produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ProductVO update(@RequestBody ProductVO productVO) {
        ProductVO productVOCreated = productService.update(productVO);
        productVOCreated.add(linkTo(methodOn(ProductController.class).findById(productVO.getId())).withSelfRel());
        return productVOCreated;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }
}
